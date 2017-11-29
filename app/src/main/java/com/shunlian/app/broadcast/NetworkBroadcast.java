package com.shunlian.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.NetworkUtils;

/**
 * Created by Administrator on 2017/11/28.
 */

public class NetworkBroadcast extends BroadcastReceiver {


    private UpdateUIListenner updateUIListenner;

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
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
            switch (netWorkStatus){
                case NetworkUtils.NETWORK_CLASS_4_G:
                case NetworkUtils.NETWORK_CLASS_3_G:
                case NetworkUtils.NETWORK_CLASS_2_G:
                    Common.staticToast("已切换到4G/3G/2G");
                    break;
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
