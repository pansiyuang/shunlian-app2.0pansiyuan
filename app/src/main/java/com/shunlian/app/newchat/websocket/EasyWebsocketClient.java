package com.shunlian.app.newchat.websocket;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.App;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.entity.BaseEntity;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.MessageEntity;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.StatusEntity;
import com.shunlian.app.newchat.entity.SwitchStatusEntity;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.util.ChatToast;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.drafts.Draft_17;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * websocket客户端
 *
 * @author lucher
 */
public class EasyWebsocketClient implements Client.OnClientConnetListener {

    //当前连接状态
    private static Status mStatus = Status.INIT;

    //自身单例
    private static EasyWebsocketClient mSingleton;
    private Client mClient;

    private static Context mContext;
    private boolean isInit = false;

    //是否在聊天页面
    private boolean isChating = false;
    //重连次数
    private int reconnectCount = 0;
    private Timer timer;
    private MyTimerTask myTimerTask;
    private Handler mHandler = new Handler();
    private UserInfoEntity userInfoEntity;
    private UserInfoEntity.Info.User mUser;
    private ObjectMapper objectMapper;
    private String currentPageType = "nomal";
    private List<EasyWebsocketClient.OnMessageReceiveListener> messageReceiveListeners;
    private MessageCountManager messageCountManager;
    private MemberStatus currentMemberStatus = MemberStatus.Member;
    private OnSwitchStatusListener switchStatusListener;

    /**
     * 单例模式获取实例
     * 服务端地址,如:ws://192.168.1.144:8080/WebsocketServer/websocket/
     *
     * @return
     */
    public static EasyWebsocketClient getInstance(Context context) {
        mContext = context;
        if (mSingleton == null) {
            synchronized (MessageCountManager.class) {
                if (mSingleton == null) {
                    mSingleton = new EasyWebsocketClient();
                }
            }
        }
        return mSingleton;
    }

    /**
     * 初始化聊天功能
     */
    public void initChat() {
        if (isInit) { //初始化过了就无需初始化了
            return;
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        if (messageReceiveListeners == null) {
            messageReceiveListeners = new ArrayList<>();
        } else {
            messageReceiveListeners.clear();
        }
        messageCountManager = MessageCountManager.getInstance(mContext);
        buildeWebsocketClient();
    }

    public void buildeWebsocketClient() {
        try {
            mClient = null;
            mClient = new Client(new URI(InterentTools.HTTPADDR_IM), new Draft_17());//ws://123.207.107.21:8086
            mClient.setOnClientConnetListener(this);
            mClient.connect();
            isInit = true;
        } catch (java.net.URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Client getClient() {
        return mClient;
    }

    /**
     * 获取当前状态
     *
     * @return
     */
    public Status getStatus() {
        return mStatus;
    }

    public class MyTimerTask extends TimerTask {
        public MyTimerTask() {
        }

        @Override
        public void run() {
            sendPin();
        }
    }

    /**
     * 判断集合内容是否为空
     *
     * @param list
     * @return
     */
    protected boolean isEmpty(List list) {
        if (list == null) {
            return true;
        }

        if (list.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onOpen() {
        LogUtil.httpLogW("Websocket onOpen()");
        mStatus = Status.CONNECTED;
        connetService();
        startHeartPin();
        cancelReconnect();
    }

    @Override
    public void onMessage(String message) {
        LogUtil.longW("Websocket 接收到消息:" + message);
        try {
            BaseEntity baseEntity = objectMapper.readValue(message, BaseEntity.class);
            switch (baseEntity.message_type) {
                case "init":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.initMessage();
                        }
                    }
                    setUserInfoEntity(message);
                    break;
                case "receive_message":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.receiveMessage(message);
                        }
                    }
                    MessageEntity messageEntity = objectMapper.readValue(message, MessageEntity.class);
                    MsgInfo msgInfo = messageEntity.msg_info;
                    String msg = msgInfo.message;
                    BaseMessage baseMessage = objectMapper.readValue(msg, BaseMessage.class);
                    baseMessage.setSendTime(msgInfo.send_time);

                    if (!getIsChating()) {
                        if (messageCountManager.isLoad()) {
                            int count = messageCountManager.getAll_msg();
                            messageCountManager.setAll_msg(count + 1);
                        }
                        EventBus.getDefault().post(new NewMessageEvent(1));

                        // TODO: 2018/6/15  新增新消息全局弹框
//                        new Thread() {
//                            public void run() {
//                                Looper.prepare();
//                                ChatToast.MakeText(App.getContext(), "", true).show();
//                                Looper.loop();// 进入loop中的循环，查看消息队列
//                            }
//                        }.start();
                    }
                    break;
                case "pingjia":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.evaluateMessage(message);
                        }
                    }
                    break;
                case "role_switch":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.roleSwitchMessage(message);
                        }
                    }
                    SwitchStatusEntity switchStatusEntity = objectMapper.readValue(message, SwitchStatusEntity.class);
                    if (switchStatusEntity.status.equals("0")) {
                        updateRoleType(switchStatusEntity.to_role);
                    }
                    new Thread() {
                        public void run() {
                            Looper.prepare();
                            Toast.makeText(mContext, switchStatusEntity.msg, Toast.LENGTH_SHORT).show();
                            Looper.loop();// 进入loop中的循环，查看消息队列
                        }
                    }.start();
                    break;
                case "zhuanjie_service":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.transferMessage(message);
                        }
                    }
                    break;
                case "service_user_add":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.transferMemberAdd(message);
                        }
                    }
                    break;
                case "online":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.onLine();
                        }
                    }
                    StatusEntity online = objectMapper.readValue(message, StatusEntity.class);
                    break;
                case "logout":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.logout();
                        }
                    }
                    StatusEntity logout = objectMapper.readValue(message, StatusEntity.class);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String errorStr) {
        LogUtil.httpLogW("Websocket 连接关闭:" + code + "," + errorStr);
        if (reconnectCount < 3) {
            mStatus = Status.CONNECTING;
            resetSocket();
        } else {
            if (mStatus != Status.TIMEOUT) {
                mStatus = Status.DISCONNECTED;
                stopPin();
            }
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    @Override
    public void onTimeOut() {
        LogUtil.httpLogW("Websocket 连接超时");
    }

    @Override
    public void onError(Exception ex) {
        LogUtil.httpLogW("Websocket 连接错误:" + ex.getMessage());
        if (reconnectCount < 3) {
            mStatus = Status.CONNECTING;
            resetSocket();
        } else {
            if (mStatus != Status.TIMEOUT) {
                ex.printStackTrace();
                mStatus = Status.ERROR;
            }
        }
    }

    public void connetService() {
        if (!NetworkUtils.isNetworkOpen(mContext)) {
            Common.staticToast("请检查你的网络状态");
            return;
        }
        String token = SharedPrefUtil.getSharedPrfString("token", "");
        String roleType = SharedPrefUtil.getSharedPrfString("role_type", "member");
        if (TextUtils.isEmpty(token)) {
            return;
        }
        //初始化聊天必须是member身份
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "init");
            jsonObject.put("token", token);
            jsonObject.put("role_type", "member");
            jsonObject.put("client", "android");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mClient != null && mStatus == Status.CONNECTED) {
            mClient.send(jsonObject.toString());
        }
    }


    /**
     * 开始发送心跳包
     */
    public void startHeartPin() {

        if (myTimerTask == null) {
            myTimerTask = new MyTimerTask();
        } else {
            myTimerTask.cancel();
            myTimerTask = new MyTimerTask();  // 新建一个任务
        }

        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(myTimerTask, 0, 1000 * 15);
    }

    /**
     * 停止心跳
     */
    public void stopPin() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

//    public void sendGoodsPin(GoodsItemEntity.Data.Item goodsItem) {
//        currentPageType = "goods";
//        this.goodsItem = goodsItem;
//        startHeartPin();
//    }
//
//    public void sendShopPin(ShopHomeEntity.Data.ShopInfo shopInfo) {
//        currentPageType = "shop";
//        this.shopInfo = shopInfo;
//        startHeartPin();
//    }
//
//    public void sendOrderItemPin(OrderItemEntity.Data orderItemEntity) {
//        currentPageType = "order";
//        this.orderItemEntity = orderItemEntity;
//        startHeartPin();
//    }

    public void sendNomalPin() {
        currentPageType = "nomal";
        startHeartPin();
    }

    public void sendPin() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "ping");
            JSONObject dataJson = new JSONObject();
            dataJson.put("pageType", currentPageType);
            switch (currentPageType) {
                case "goods":
//                    dataJson.put("goodsImage", goodsItem.getThumb());
//                    dataJson.put("title", goodsItem.getTitle());
//                    dataJson.put("price", goodsItem.getMarketprice());
//                    dataJson.put("goodsId", goodsItem.getGoodsId());
//                    jsonObject.put("data", dataJson);
                    LogUtil.httpLogW("浏览商品中...");
                    break;
                case "order":
//                    dataJson.put("address", orderItemEntity.getAddress());
//                    dataJson.put("mobile", orderItemEntity.getMobile());
//                    dataJson.put("price", orderItemEntity.getPrice());
//                    dataJson.put("realname", orderItemEntity.getRealname());
//                    dataJson.put("status", orderItemEntity.getStatus());
//                    dataJson.put("ordersn", orderItemEntity.getOrdersn());
                    jsonObject.put("data", dataJson);
                    LogUtil.httpLogW("查看订单中...");
                    break;
                case "shop":
//                    dataJson.put("sellerid", shopInfo.getShopId());
//                    dataJson.put("seller_name", shopInfo.getShop_name());
//                    dataJson.put("img", shopInfo.getBanner());
                    jsonObject.put("data", dataJson);
                    LogUtil.httpLogW("浏览店铺中...");
                    break;
                case "nomal":
                    LogUtil.httpLogW("Websocket 心跳包发送:" + getStatus());
                    break;
            }
            if (mClient != null && mStatus == Status.CONNECTED) {
                LogUtil.httpLogW("Websocket 发送的json：" + jsonObject.toString());
                mClient.send(jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserInfoEntity(String str) {
        try {
            userInfoEntity = objectMapper.readValue(str, UserInfoEntity.class);

            LogUtil.httpLogW("setUserInfoEntity:" + currentMemberStatus);
            if (currentMemberStatus != MemberStatus.Member) {
                switchStatus(currentMemberStatus);
            } else {
                switch (userInfoEntity.info.role_type) {
                    case "seller":
                        currentMemberStatus = MemberStatus.Seller;
                        break;
                    case "admin":
                        currentMemberStatus = MemberStatus.Admin;
                        break;
                    default:
                        currentMemberStatus = MemberStatus.Member;
                        break;
                }
                SharedPrefUtil.saveSharedPrfString("user_id", userInfoEntity.info.user.user_id);
                LogUtil.httpLogW("Websocket 用户状态初始化为:" + currentMemberStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserInfoEntity getUserInfoEntity() {
        return userInfoEntity;
    }

    public void send(String msg) {
        if (!NetworkUtils.isNetworkOpen(mContext)) {
            Common.staticToast("网络断开,请检查网络");
            return;
        }
        if (mClient == null) {
            Common.staticToast("服务器连接断开,发送失败");
            return;
        }
        if (mStatus == Status.CONNECTING) {
            Common.staticToast("服务器连接中,稍后重试");
            return;
        }
        if (mStatus == Status.CONNECTED) {
            mClient.send(msg);
        }
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public UserInfoEntity.Info.User getUser() {
        if (userInfoEntity == null) {
            return null;
        }
        if (userInfoEntity.info == null) {
            return null;
        }
        MemberStatus status = getMemberStatus();
        switch (status) {
            case Seller:
                mUser = userInfoEntity.info.bind.bind_seller.user;
                break;
            case Admin:
                mUser = userInfoEntity.info.bind.bind_admin.user;
                break;
            case Member:
                mUser = userInfoEntity.info.user;
                break;
        }
        return mUser;
    }

    /**
     * 更新用户
     *
     * @param roleType
     */
    public void updateRoleType(String roleType) {
        if (userInfoEntity == null) {
            return;
        }
        userInfoEntity.info.role_type = roleType;
        SharedPrefUtil.saveSharedPrfString("role_type", roleType);
        switch (roleType) {
            case "seller":
                mUser = userInfoEntity.info.bind.bind_seller.user;
                currentMemberStatus = MemberStatus.Seller;
                break;
            case "admin":
                mUser = userInfoEntity.info.bind.bind_admin.user;
                currentMemberStatus = MemberStatus.Admin;
                break;
            default:
                mUser = userInfoEntity.info.user;
                currentMemberStatus = MemberStatus.Member;
                break;
        }
        SharedPrefUtil.saveSharedPrfString("user_id", mUser.user_id);
        if (switchStatusListener != null) {
            switchStatusListener.switchSuccess(roleType);
        }
    }

    public MemberStatus getMemberStatus() {
        return currentMemberStatus;
    }

    /**
     * 切换身份
     */
    public void switchStatus(MemberStatus status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "role_switch");
            switch (status) {
                case Member:
                    jsonObject.put("to_role", "member");
                    break;
                case Seller:
                    jsonObject.put("to_role", "seller");
                    break;
                case Admin:
                    jsonObject.put("to_role", "admin");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mClient != null && mStatus == Status.CONNECTED) {
            LogUtil.httpLogW("Websocket 切换身份:" + jsonObject.toString());
            mClient.send(jsonObject.toString());
        }
    }


    /**
     * 判断是否是普通身份
     *
     * @return
     */
    public boolean isMember() {
        return currentMemberStatus == MemberStatus.Member;
    }

    /**
     * 判断是否是平台客服
     *
     * @return
     */
    public boolean isAdmin() {
        return currentMemberStatus == MemberStatus.Admin;
    }

    /**
     * 判断是否是平台客服管理
     */
    public boolean isAdminManager() {
        if (userInfoEntity == null) {
            return false;
        }
        UserInfoEntity.Info info = userInfoEntity.info;
        if (info == null) {
            return false;
        }
        UserInfoEntity.Info.Bind bind = info.bind;
        if (bind == null) {
            return false;
        }
        UserInfoEntity.BindAdmin bindAdmin = bind.bind_admin;
        if (bindAdmin == null) {
            return false;
        }
        return bindAdmin.is_manage;
    }

    /**
     * 判断是否是商家客服
     *
     * @return
     */
    public boolean isSeller() {
        return currentMemberStatus == MemberStatus.Seller;
    }

    /**
     * 判断是否是商家客服管理
     */
    public boolean isSellerManager() {
        if (userInfoEntity == null) {
            return false;
        }
        UserInfoEntity.Info info = userInfoEntity.info;
        if (info == null) {
            return false;
        }
        UserInfoEntity.Info.Bind bind = info.bind;
        if (bind == null) {
            return false;
        }
        UserInfoEntity.BindSeller bindSeller = bind.bind_seller;
        if (bindSeller == null) {
            return false;
        }
        return bindSeller.is_manage;
    }

    /**
     * 判断是否绑定过商家客服
     *
     * @return
     */
    public boolean isBindSeller() {
        if (userInfoEntity == null) {
            return false;
        }

        if (userInfoEntity.info == null) {
            return false;
        }

        if (userInfoEntity.info.bind == null) {
            return false;
        }

        if (userInfoEntity.info.bind.bind_seller == null) {
            return false;
        }
        UserInfoEntity.BindSeller bindSeller = userInfoEntity.info.bind.bind_seller;
        return bindSeller.is_bind;
    }

    /**
     * 判断是否绑定过平台客服
     *
     * @return
     */
    public boolean isBindAdmin() {
        if (userInfoEntity == null) {
            return false;
        }

        if (userInfoEntity.info == null) {
            return false;
        }

        if (userInfoEntity.info.bind == null) {
            return false;
        }

        if (userInfoEntity.info.bind.bind_admin == null) {
            return false;
        }
        UserInfoEntity.BindAdmin bind_admin = userInfoEntity.info.bind.bind_admin;
        return bind_admin.is_bind;
    }

    public boolean getIsChating() {
        return isChating;
    }

    public void setChating(boolean chating) {
        isChating = chating;
    }

    public void resetSocket() {
        if (!NetworkUtils.isNetworkOpen(mContext)) {
            reconnectCount = 0;
            LogUtil.httpLogW("Websocket 重连失败网络不可用");
            mStatus = Status.DISCONNECTED;
            return;
        }

        if (reconnectCount > 3) {
            LogUtil.httpLogW(String.format("Websocket 准备开始第%d次重连,重连间隔%d", reconnectCount, 1000));
            cancelReconnect();
            return;
        }
        LogUtil.httpLogW("Websocket resetSocket()");
        mHandler.postDelayed(mReconnectTask, 1000);
    }

    private Runnable mReconnectTask = () -> {
        LogUtil.httpLogW("Websocket 尝试重连服务器...." + mStatus);
        if (mStatus != Status.CONNECTING) {//不是正在重连状态
            mStatus = Status.CONNECTING;
        }
        reconnectCount++;
        buildeWebsocketClient();
    };

    private void cancelReconnect() {
        reconnectCount = 0;
        mHandler.removeCallbacks(mReconnectTask);
    }

    /**
     * 断开IM链接
     */
    public void logout() {
        isInit = false;
        mStatus = Status.DISCONNECTED;
        stopPin();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void addOnMessageReceiveListener(OnMessageReceiveListener listener) {
        if (messageReceiveListeners == null) {
            return;
        }
        messageReceiveListeners.add(listener);
    }

    public void removeOnMessageReceiveListener(OnMessageReceiveListener listener) {
        if (messageReceiveListeners == null) {
            return;
        }
        messageReceiveListeners.remove(listener);
    }

    public void setOnSwitchStatusListener(OnSwitchStatusListener listener) {
        switchStatusListener = listener;
    }

    public interface OnMessageReceiveListener {
        void initMessage();

        void receiveMessage(String msg);

        void evaluateMessage(String msg);

        void roleSwitchMessage(String msg);

        void transferMessage(String msg);

        void transferMemberAdd(String msg);

        void onLine();

        void logout();
    }

    public interface OnSwitchStatusListener {

        void switchSuccess(String roleType);

    }
}
