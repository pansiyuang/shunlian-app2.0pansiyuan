package com.shunlian.app.newchat.websocket;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.App;
import com.shunlian.app.newchat.entity.BaseEntity;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.MessageEntity;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.StatusEntity;
import com.shunlian.app.newchat.entity.SwitchStatusEntity;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.HttpDialog;

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
    private TimeOutThread timeOutThread;
    private Timer timer;
    private MyTimerTask myTimerTask;
    private UserInfoEntity userInfoEntity;
    private UserInfoEntity.Info.User mUser;
    private static ObjectMapper objectMapper;
    private OnClientConnetListener mListener;
    private OnMessageReceiveListener receiveListener;
    private String infoStr;
    private String currentPageType = "nomal";
    //    private GoodsItemEntity.Data.Item goodsItem;
//    private ShopHomeEntity.Data.ShopInfo shopInfo;
//    private OrderItemEntity.Data orderItemEntity;
    private static List<UserInfoEntity.Info.Friend> mFriendList;
    private int unReadCount = 0;


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
        try {
            mSingleton = null;
            mFriendList = null;
            mSingleton = new EasyWebsocketClient(new URI("ws://123.207.107.21:8086"), new Draft_17());//ws://api.shunliandongli.com/v1/im2.alias
            mFriendList = new ArrayList<>();
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

    public void resetSocket() {
        if (mStatus == Status.DISCONNECTED) {
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
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        timeOutThread.cancel();
        connetService();
        startHeartPin();
        mStatus = Status.CONNECTED;

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
                    if (receiveListener != null) {
                        receiveListener.initMessage();
                    }
                    infoStr = message;
                    setUserInfoEntity(infoStr);
                    break;
                case "receive_message":
                    if (receiveListener != null) {
                        receiveListener.receiveMessage(message);
                    }

                    MessageEntity messageEntity = objectMapper.readValue(message, MessageEntity.class);
                    MsgInfo msgInfo = messageEntity.msg_info;
                    String msg = msgInfo.message;
                    BaseMessage baseMessage = objectMapper.readValue(msg, BaseMessage.class);
                    baseMessage.setSendTime(msgInfo.send_time);

                    if (!baseMessage.from_user_id.equals("-1")) { //-1为系统消息
//                        //通知刷新好友列表
//                        updateFriendList(baseMessage);
                    }
                    break;
                case "pingjia":
                    if (receiveListener != null) {
                        receiveListener.evaluateMessage(message);
                    }
                    break;
                case "role_switch":
                    if (receiveListener != null) {
                        receiveListener.roleSwitchMessage(message);
                    }
                    SwitchStatusEntity switchStatusEntity = objectMapper.readValue(message, SwitchStatusEntity.class);
                    updateRoleType(switchStatusEntity.to_role);
                    new Thread() {
                        public void run() {
                            Looper.prepare();
                            Toast.makeText(mContext, switchStatusEntity.msg, Toast.LENGTH_SHORT).show();
                            Looper.loop();// 进入loop中的循环，查看消息队列
                        }
                    }.start();
                    break;
                case "online":
                    if (receiveListener != null) {
                        receiveListener.onLine();
                    }

                    StatusEntity online = objectMapper.readValue(message, StatusEntity.class);
                    updateFriendStatus(online);
                    break;
                case "logout":
                    if (receiveListener != null) {
                        receiveListener.logout();
                    }

                    StatusEntity logout = objectMapper.readValue(message, StatusEntity.class);
                    updateFriendStatus(logout);
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
            mSingleton = null;
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
            SharedPrefUtil.saveSharedPrfString("user_id", userInfoEntity.info.user.user_id);
            SharedPrefUtil.saveSharedPrfString("role_type", userInfoEntity.info.role_type);
//            setFriendList(userInfoEntity);
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
        String userId;
        switch (roleType) {
            case "seller":
                userId = userInfoEntity.info.bind.bind_seller.user.user_id;
                break;
            case "admin":
                userId = userInfoEntity.info.bind.bind_admin.user.user_id;
                break;
            default:
                userId = userInfoEntity.info.user.user_id;
                break;
        }
        SharedPrefUtil.saveSharedPrfString("user_id", userId);
    }

    public String getCurrentRoleType() {
        if (userInfoEntity == null) {
            return "member";
        }
        if (userInfoEntity.info == null) {
            return "member";
        }
        UserInfoEntity.Info info = userInfoEntity.info;
        return info.role_type;
    }

    public MemberStatus getMemberStatus() {
        if (userInfoEntity == null) {
            return MemberStatus.Member;
        }
        if (userInfoEntity.info == null) {
            return MemberStatus.Member;
        }
        UserInfoEntity.Info info = userInfoEntity.info;
        switch (info.role_type) {
            case "seller":
                return MemberStatus.Seller;
            case "admin":
                return MemberStatus.Admin;
            default:
                return MemberStatus.Member;
        }
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
        if (userInfoEntity == null) {
            return true;
        }
        if (userInfoEntity.info == null) {
            return true;
        }
        UserInfoEntity.Info info = userInfoEntity.info;
        return "member".equals(info.role_type);
    }

    /**
     * 判断是否是平台客服
     *
     * @return
     */
    public boolean isAdmin() {
        if (userInfoEntity == null) {
            return false;
        }
        if (userInfoEntity.info == null) {
            return false;
        }
        UserInfoEntity.Info info = userInfoEntity.info;
        return "admin".equals(info.role_type);
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
        if (userInfoEntity == null) {
            return false;
        }
        UserInfoEntity.Info info = userInfoEntity.info;
        return "seller".equals(info.role_type);
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


    /**
     * 通知刷新页面
     *
     * @param baseMessage
     */

    public void updateFriendList(BaseMessage baseMessage) {
        int unReadNum;
        //检查当前聊天列表是否存在
        if (!checkFriendInList(baseMessage)) {
            addFriend2List(baseMessage);
        }
        if (mFriendList != null && mFriendList.size() != 0) {
            for (int i = 0; i < mFriendList.size(); i++) {
                //好友发给自己的消息
                if (baseMessage.from_join_id.equals(mFriendList.get(i).join_id)) {
                    LogUtil.httpLogW("好友给自己发消息");
                    if (!getIsChating()) { //不在聊天页面
                        unReadNum = mFriendList.get(i).unread_count + 1;
                        baseMessage.setuReadNum(unReadNum);
                        mFriendList.get(i).unread_count = unReadNum;
                        mFriendList.get(i).line_status = "1";
                        unReadCount++;
                        //通知页面刷新
//                        EventBus.getDefault().post(new UpdateListEvent(EventType.FRIENDLIST).setBaseMessage(baseMessage));
                    }
                    //聊天列表是否存在当前item
                    break;
                }
            }
            LogUtil.httpLogW("消息有添加");
//            EventBus.getDefault().postSticky(new UnReadCountEvent(EventType.UNREADCOUNT).setCount(unReadCount));
        }
    }

    /**
     * 刷新item为全部已读
     */

    public void updateFriendList(String uid) {
        if (mFriendList != null && mFriendList.size() != 0) {
            for (int i = 0; i < mFriendList.size(); i++) {
                if (uid.equals(mFriendList.get(i).join_id)) {
                    int count = mFriendList.get(i).unread_count;
                    mFriendList.get(i).unread_count = 0;

                    unReadCount -= count;
                    //通知页面刷新

//                    EventBus.getDefault().post(new UpdateListEvent(EventType.FRIENDLIST).setUserId(uid));
                    break;
                }
            }
            LogUtil.httpLogW("消息已读啦");
            setChating(true);
//            EventBus.getDefault().postSticky(new UnReadCountEvent(EventType.UNREADCOUNT).setCount(unReadCount));
        }
    }

    /**
     * 修改用户状态
     * <p>
     * 用户状态 1在线，2空闲，3离开，4隐身，5离线
     */

    public void updateFriendStatus(StatusEntity statusEntity) {
        if (mFriendList == null || mFriendList.size() == 0) {
            return;
        }
        for (int i = 0; i < mFriendList.size(); i++) {
            if (statusEntity.id.equals(mFriendList.get(i).join_id)) {
                if ("logout".equals(statusEntity.message_type)) {
                    mFriendList.get(i).line_status = "5";
                } else if ("online".equals(statusEntity.message_type)) {
                    mFriendList.get(i).line_status = "1";
                }
//                EventBus.getDefault().post(new LineStatusEvent(LINESTATUS).setStatusEntity(statusEntity));
                break;
            }
        }
    }

    /**
     * 用户上线
     */
    public void setFriendList(UserInfoEntity userInfoEntity) {
        mFriendList = userInfoEntity.info.friends;
        List<UserInfoEntity.Info.Uread.UreadList> list = userInfoEntity.info.uread.uread_list;
        for (int i = 0; i < mFriendList.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).uid.equals(mFriendList.get(i).join_id)) {
                    mFriendList.get(i).unread_count = Integer.valueOf(list.get(j).num);
                }
            }
            unReadCount += mFriendList.get(i).unread_count;
        }
        LogUtil.httpLogW("消息初始化数量：" + unReadCount);
//        EventBus.getDefault().postSticky(new UnReadCountEvent(EventType.UNREADCOUNT).setCount(unReadCount));
    }

    /**
     * 检查当前用户是否在聊天列表中
     */

    public boolean checkFriendInList(BaseMessage baseMessage) {
        String friendId;
        boolean isInclude = false;
        if (isSelf(baseMessage)) {
            friendId = baseMessage.to_join_id;
        } else {
            friendId = baseMessage.from_join_id;
        }
        if (!TextUtils.isEmpty(friendId) && mFriendList != null) {
            for (int i = 0; i < mFriendList.size(); i++) {
                if (mFriendList.get(i).join_id.equals(friendId)) {
                    return true;
                }
            }
        }
        return isInclude;
    }

    public void addFriend2List(BaseMessage baseMessage) {
        //当前用户不在聊天列表
        UserInfoEntity.Info.Friend friend = new UserInfoEntity.Info.Friend();
        if (!isSelf(baseMessage)) { //对方发送的消息
            friend.headurl = baseMessage.from_headurl;
            friend.join_id = baseMessage.from_join_id;
            friend.line_status = "1";
            friend.user_id = baseMessage.from_user_id;
            friend.nickname = baseMessage.from_nickname;
            friend.update_time = String.valueOf(baseMessage.getSendTime());
        } else { //自己发送的消息
            friend.headurl = baseMessage.to_headurl;
            friend.join_id = baseMessage.to_join_id;
            friend.line_status = "5";
            friend.user_id = baseMessage.to_user_id;
            friend.nickname = baseMessage.to_nickname;
            friend.update_time = String.valueOf(baseMessage.getSendTime());
        }
        mFriendList.add(0, friend);
        LogUtil.httpLogW("添加用户");
//        EventBus.getDefault().post(new UpdateListEvent(EventType.FRIENDLIST).setmList(mFriendList));
    }


    public List<UserInfoEntity.Info.Friend> getFriendList() {
        return mFriendList;
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

    public void setOnClientConnetListener(OnClientConnetListener listener) {
        this.mListener = listener;
    }

    public void setOnMessageReceiveListener(OnMessageReceiveListener listener) {
        this.receiveListener = listener;
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
