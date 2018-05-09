package com.shunlian.app.newchat.websocket;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.entity.BaseEntity;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.MessageEntity;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.StatusEntity;
import com.shunlian.app.newchat.entity.SwitchStatusEntity;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * websocket客户端
 *
 * @author lucher
 */
public class EasyWebsocketClient extends WebSocketClient {

    //当前连接状态
    private static Status mStatus = Status.INIT;

    //自身单例
    private static EasyWebsocketClient mSingleton;

    private static Context mContext;

    //是否在聊天页面
    private boolean isChating = false;

    //超时时间
    private static long timeout = 15 * 1000;
    //重连次数
    private int reconnectCount = 0;
    //重连最小时间间隔
    private long minInterval = 3000;
    //重连最大时间间隔
    private long maxInterval = 60000;
    private TimeOutThread timeOutThread;
    private Timer timer;
    private MyTimerTask myTimerTask;
    private Handler mHandler = new Handler();
    private UserInfoEntity userInfoEntity;
    private UserInfoEntity.Info.User mUser;
    private static ObjectMapper objectMapper;
    private OnClientConnetListener mListener;
    private String infoStr;
    private String currentPageType = "nomal";
    private static List<EasyWebsocketClient.OnMessageReceiveListener> messageReceiveListeners;
    private static MessageCountManager messageCountManager;
    private MemberStatus currentMemberStatus = MemberStatus.Member;
    //    private GoodsItemEntity.Data.Item goodsItem;
//    private ShopHomeEntity.Data.ShopInfo shopInfo;
//    private OrderItemEntity.Data orderItemEntity;


    /**
     * 单例模式获取实例
     * 服务端地址,如:ws://192.168.1.144:8080/WebsocketServer/websocket/
     *
     * @return
     */
    public static EasyWebsocketClient getClient() {
        return mSingleton;
    }

    public static EasyWebsocketClient initWebsocketClient(Context context) {
        mContext = context.getApplicationContext();
        objectMapper = new ObjectMapper();
        messageReceiveListeners = new ArrayList<>();
        messageCountManager = MessageCountManager.getInstance(context);

        try {
            mSingleton = null;
            mSingleton = new EasyWebsocketClient(new URI("ws://123.207.107.21:8086"), new Draft_17());//ws://api.shunliandongli.com/v1/im2.alias
            mSingleton.setTimeOut(timeout);
            mSingleton.connect();
        } catch (java.net.URISyntaxException e) {
            e.printStackTrace();
        }
        return mSingleton;
    }

    private EasyWebsocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    /**
     * 设置超时时间
     *
     * @param timeout
     */
    public void setTimeOut(long timeout) {
        this.timeout = timeout;
    }

    /**
     * 获取当前状态
     *
     * @return
     */
    public Status getStatus() {
        return mStatus;
    }


    @Override
    public void connect() {
        mStatus = Status.CONNECTING;
        onConnecting();
        super.connect();
    }

    @Override
    public boolean connectBlocking() throws InterruptedException {
        mStatus = Status.CONNECTING;
        onConnecting();
        return super.connectBlocking();
    }

    /**
     * 连接中
     */
    public void onConnecting() {
        timeOutThread = new TimeOutThread();//开启超时任务线程
        timeOutThread.start();

        if (mListener != null) {
            mListener.onConnecting();
        }
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
    public void onOpen(ServerHandshake handshakedata) {
        timeOutThread.cancel();
        connetService();
        startHeartPin();
        mStatus = Status.CONNECTED;
        cancelReconnect();
        if (mListener != null) {
            mListener.onOpen();
        }
    }

    @Override
    public void onMessage(String message) {
        LogUtil.longW("接收到消息:" + message);
        try {
            BaseEntity baseEntity = objectMapper.readValue(message, BaseEntity.class);
            switch (baseEntity.message_type) {
                case "init":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.initMessage();
                        }
                    }
                    infoStr = message;
                    setUserInfoEntity(infoStr);
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
                        EventBus.getDefault().post(new NewMessageEvent(1));
                        if (messageCountManager.isLoad()) {
                            int count = messageCountManager.getAll_msg();
                            messageCountManager.setAll_msg(count + 1);
                        }
                    }
                    break;
                case "pingjia":
                    if (!isEmpty(messageReceiveListeners)) {
                        for (OnMessageReceiveListener listener : messageReceiveListeners) {
                            listener.evaluateMessage(message);
                        }
                    }
                    if (!getIsChating()) {
                        EventBus.getDefault().post(new NewMessageEvent(1));
                        if (messageCountManager.isLoad()) {
                            int count = messageCountManager.getAll_msg();
                            messageCountManager.setAll_msg(count + 1);
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
    public void onFragment(Framedata fragment) {
        LogUtil.httpLogW("接收到片段:" + new String(fragment.getPayloadData().array()));
        if (mListener != null) {
            mListener.onFragment();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LogUtil.httpLogW("连接关闭:" + code + "," + reason + "," + remote);
        if (mStatus != Status.TIMEOUT) {
            mStatus = Status.DISCONNECTED;
            timeOutThread.cancel();
            stopPin();

            if (mListener != null) {
                mListener.onClose();
            }
        }
        if (timer != null) {
            timer.cancel();
        }

        resetSocket();
    }

    @Override
    public void onError(Exception ex) {
        LogUtil.httpLogW("连接错误:" + ex.getMessage());
        if (mStatus != Status.TIMEOUT) {
            ex.printStackTrace();
            mStatus = Status.ERROR;
            timeOutThread.cancel();
            stopPin();

            if (mListener != null) {
                mListener.onError();
            }

            resetSocket();
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
        LogUtil.httpLogW("connetService:" + jsonObject.toString());
        send(jsonObject.toString());
    }


    /**
     * 连接超时检测线程
     *
     * @author lucher
     */
    public class TimeOutThread extends Thread {

        //是否取消
        private boolean cancel;

        @Override
        public synchronized void run() {
            try {
                wait(timeout);
                if (!cancel) {
                    close();
                    mSingleton = null;
                    mStatus = Status.TIMEOUT;
                    if (mListener != null) {
                        mListener.onTimeOut();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 取消
         */
        public void cancel() {
            cancel = true;
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
                    LogUtil.httpLogW("心跳包发送");
                    break;
            }
            LogUtil.httpLogW("发送的json：" + jsonObject.toString());
            send(jsonObject.toString());
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
                LogUtil.httpLogW("用户状态初始化为:" + currentMemberStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserInfoEntity getUserInfoEntity() {
        return userInfoEntity;
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
        if (mStatus == Status.CONNECTED) {
            LogUtil.httpLogW("切换身份:" + jsonObject.toString());
            send(jsonObject.toString());
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

    public boolean isSelf(BaseMessage baseMessage) {
        boolean isSelf = false;
        if (userInfoEntity == null) {
            isSelf = false;
        }

        if (baseMessage == null) {
            isSelf = false;
        }

        if (baseMessage.from_user_id.equals(userInfoEntity.info.user.user_id)) {
            isSelf = true;
        }

        if (baseMessage.to_user_id.equals(userInfoEntity.info.user.user_id)) {
            isSelf = false;
        }
        return isSelf;
    }


    public void resetSocket() {
        if (!NetworkUtils.isNetworkOpen(mContext)) {
            reconnectCount = 0;
            LogUtil.httpLogW("重连失败网络不可用");
            return;
        }
        if (mStatus != Status.CONNECTING) {//不是正在重连状态
            reconnectCount++;
            mStatus = Status.CONNECTING;
            long reconnectTime = minInterval;
            if (reconnectCount > 3) {
                long temp = minInterval * (reconnectCount - 2);
                reconnectTime = temp > maxInterval ? maxInterval : temp;
            }
            LogUtil.httpLogW(String.format("准备开始第%d次重连,重连间隔%d", reconnectCount, reconnectTime));
            mHandler.postDelayed(mReconnectTask, reconnectTime);
        }
    }

    private Runnable mReconnectTask = new Runnable() {
        @Override
        public void run() {
            LogUtil.httpLogW("尝试重连服务器....");
            try {
                mSingleton = new EasyWebsocketClient(new URI("ws://123.207.107.21:8086"), new Draft_17());//ws://api.shunliandongli.com/v1/im2.alias
                mSingleton.setTimeOut(timeout);
                mSingleton.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                LogUtil.httpLogW("链接服务器失败....");
            }
        }
    };

    private void cancelReconnect() {
        reconnectCount = 0;
        mHandler.removeCallbacks(mReconnectTask);
    }

    public void setOnClientConnetListener(OnClientConnetListener listener) {
        this.mListener = listener;
    }

    public void addOnMessageReceiveListener(OnMessageReceiveListener listener) {
        messageReceiveListeners.add(listener);
    }

    public void removeOnMessageReceiveListener(OnMessageReceiveListener listener) {
        messageReceiveListeners.remove(listener);
    }

    public interface OnMessageReceiveListener {
        void initMessage();

        void receiveMessage(String msg);

        void evaluateMessage(String msg);

        void roleSwitchMessage(String msg);

        void onLine();

        void logout();
    }

    public interface OnClientConnetListener {
        void onConnecting();

        void onOpen();

        void onFragment();

        void onClose();

        void onError();

        void onTimeOut();
    }
}
