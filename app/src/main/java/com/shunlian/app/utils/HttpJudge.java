package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


/**
 * Created by Administrator on 2016/5/18 0018.
 */
public class HttpJudge {
    public static boolean isNetworkAvailable(Activity activity) {
        if (activity != null) {
            // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return false;
            } else {
                // 获取NetworkInfo对象
                NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
                if (networkInfo != null && networkInfo.length > 0) {
                    for (int i = 0; i < networkInfo.length; i++) {
//                        System.out.println(i + "===状态===" + networkInfo[i].getState());
//                        System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                        // 判断当前网络状态是否为连接状态
                        if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            Toast.makeText(activity, "网络连接失败，请检查您的网络", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //此方法是被只能获取context的所调用
    public static boolean isNetworkOpen(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return false;
            } else {
                // 获取NetworkInfo对象
                NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
                if (networkInfo != null && networkInfo.length > 0) {
                    for (int i = 0; i < networkInfo.length; i++) {
//                        System.out.println(i + "===状态===" + networkInfo[i].getState());
//                        System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                        // 判断当前网络状态是否为连接状态
                        if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            Toast.makeText(context, "网络连接失败，请检查您的网络", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * make true current connect service is wifi
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
