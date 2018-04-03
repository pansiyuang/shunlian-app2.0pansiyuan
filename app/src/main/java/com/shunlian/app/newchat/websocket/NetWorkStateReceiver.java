package com.shunlian.app.newchat.websocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import static com.shunlian.app.newchat.websocket.EasyWebsocketClient.initWebsocketClient;

/**
 * Created by Administrator on 2017/9/27.
 */

public class NetWorkStateReceiver extends BroadcastReceiver {
    private EasyWebsocketClient mClient;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

//        BaseApplication myApp = (BaseApplication) BaseApplication.getContext();
//        String pin = myApp.spUserInfo.getString("api_pin", "");

//        if (TextUtils.isEmpty(pin)) {
//            return;
//        }
        //网络连接状态
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (EasyWebsocketClient.getClient() == null) {
                initWebsocketClient(context);
            } else {
                mClient = EasyWebsocketClient.getClient();
                if (mClient.getStatus() == Status.CONNECTED || mClient.getStatus() == Status.CONNECTING) {
                    return;
                }
                initWebsocketClient(context);
            }
        }
    }
}
