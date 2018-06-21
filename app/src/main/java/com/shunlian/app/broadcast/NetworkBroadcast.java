package com.shunlian.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;

/**
 * Created by Administrator on 2017/11/28.
 */

public class NetworkBroadcast extends BroadcastReceiver {


    private UpdateUIListenner updateUIListenner;
    //网络状态
    private final String WIFI = "wifi";
    private final String mobileNetwork = "mobile_network";
    //保存网络状态的key
    private final String networkKey = "network_state";
    //网络提示
    private final String networkTip = "已切换到4G/3G/2G";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            if (updateUIListenner != null) {
                updateUIListenner.updateUI(true);
            }
        } else {
            if (updateUIListenner != null) {
                updateUIListenner.updateUI(false);
            }
            int netWorkStatus = NetworkUtils.getNetWorkStatus(context);
            switch (netWorkStatus) {
                case NetworkUtils.NETWORK_CLASS_4_G:
                case NetworkUtils.NETWORK_CLASS_3_G:
                case NetworkUtils.NETWORK_CLASS_2_G:
                    String networkState = SharedPrefUtil
                            .getSharedPrfString(networkKey, WIFI);
                    if (!mobileNetwork.equals(networkState)) {
                        Common.staticToast(networkTip);
                        SharedPrefUtil.saveSharedPrfString(networkKey,mobileNetwork);
                    }
                    break;
                case NetworkUtils.NETWORK_WIFI:
                    SharedPrefUtil.saveSharedPrfString(networkKey,WIFI);
                    break;
            }
            EasyWebsocketClient client = EasyWebsocketClient.getInstance(context);
            if (client.getClient() != null) {
                //LogUtil.httpLogW("网络发生了改变,Websocket是否断开：" + client.getClient().isClosed() + " 状态为：" + client.getStatus());
                if (client.getClient().isClosed()) {
                    client.resetSocket();
                }
            }
        }
    }


    /**
     * 监听广播接收器的接收到的数据
     *
     * @param updateUIListenner
     */
    public void setOnUpdateUIListenner(UpdateUIListenner updateUIListenner) {
        this.updateUIListenner = updateUIListenner;

    }

    public interface UpdateUIListenner {
        void updateUI(boolean isShow);
    }
}
