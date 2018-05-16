package com.shunlian.app.newchat.websocket;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/5/15.
 */

public class WebsoketManager implements EasyWebsocketClient.OnClientConnetListener {

    //当前连接状态
    private static Status mStatus = Status.INIT;
    private static WebsoketManager manager;
    private static Context mContext;
    private ObjectMapper objectMapper;
    private List<EasyWebsocketClient.OnMessageReceiveListener> messageReceiveListeners;
    //超时时间
    private static long timeout = 15 * 1000;
    //重连次数
    private int reconnectCount = 0;
    //是否在聊天页面
    private boolean isChating = false;
    private EasyWebsocketClient.TimeOutThread timeOutThread;
    private MessageCountManager messageCountManager;
    private Timer timer;
    private MyTimerTask myTimerTask;
    private Handler mHandler = new Handler();
    private UserInfoEntity userInfoEntity;
    private UserInfoEntity.Info.User mUser;
    private EasyWebsocketClient mSingleton;
    private String currentPageType = "nomal";

    public WebsoketManager getInstance(Context context) {
        mContext = context;
        if (manager == null) {
            synchronized (WebsoketManager.class) {
                if (manager == null) {
                    manager = new WebsoketManager();
                }
            }
        }
        return manager;
    }

    public WebsoketManager() {
        objectMapper = new ObjectMapper();
        messageReceiveListeners = new ArrayList<>();
        messageCountManager = MessageCountManager.getInstance(mContext);
    }

    /**
     * 聊天初始化
     */
    public void initWebsocketClient() {
        try {
            mSingleton = new EasyWebsocketClient(new URI("ws://123.207.107.21:8086"), new Draft_17());
            mSingleton.setTimeOut(timeout);
            mSingleton.setOnClientConnetListener(this);
            mSingleton.connect();
        } catch (java.net.URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnecting() {
        mStatus = Status.CONNECTING;
    }

    @Override
    public void onOpen() {
        connetService();
        startHeartPin();
        mStatus = Status.CONNECTED;
    }

    @Override
    public void onFragment() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onTimeOut() {

    }

    /**
     * 连接上服务器
     */
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
        if (mSingleton != null && mSingleton.getStatus() == Status.CONNECTED) {
            mSingleton.send(jsonObject.toString());
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


    public class MyTimerTask extends TimerTask {

        public MyTimerTask() {
        }

        @Override
        public void run() {
            sendPin();
        }
    }

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
            if (mSingleton != null && mStatus == Status.CONNECTED) {
                LogUtil.httpLogW("发送的json：" + jsonObject.toString());
                mSingleton.send(jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetSocket() {
        if (!NetworkUtils.isNetworkOpen(mContext)) {
            reconnectCount = 0;
            LogUtil.httpLogW("重连失败网络不可用");
            return;
        }

        if (reconnectCount > 3) {
            LogUtil.httpLogW(String.format("准备开始第%d次重连,重连间隔%d", reconnectCount, 1000));
            cancelReconnect();
            return;
        }
        mHandler.postDelayed(mReconnectTask, 1000);
    }

    private Runnable mReconnectTask = () -> {
        LogUtil.httpLogW("尝试重连服务器...." + mStatus);

        if (mStatus != Status.CONNECTING) {//不是正在重连状态
            reconnectCount++;
            mStatus = Status.CONNECTING;
        }
        try {
            mSingleton = null;
            mSingleton = new EasyWebsocketClient(new URI("ws://123.207.107.21:8086"), new Draft_17());
            mSingleton.setTimeOut(timeout);
            mSingleton.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            LogUtil.httpLogW("链接服务器失败....");
        }
    };

    private void cancelReconnect() {
        reconnectCount = 0;
        mHandler.removeCallbacks(mReconnectTask);
    }
}
