package com.shunlian.app.utils;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by zhang on 2017/4/14 13 : 03.
 */

public class DeviceInfoUtil {

    public static String getGprsIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public static String getWifiIpAddress(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return TransformUtil.intToIp(ipAddress);
    }

    /**
     * 网络已经连接，然后去判断是wifi连接还是GPRS连接
     * 设置一些自己的逻辑调用
     */
    public static String getDeviceIp(Context context) {
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfoMobile != null){
            NetworkInfo.State gprs = networkInfoMobile.getState();
            if (gprs == NetworkInfo.State.CONNECTED || gprs == NetworkInfo.State.CONNECTING) {
                return getGprsIpAddress();
            }
        }

        NetworkInfo networkInfoWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfoWifi != null){
            NetworkInfo.State wifi = networkInfoWifi.getState();
            //判断为wifi状态下才加载广告，如果是GPRS手机网络则不加载！
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                return getWifiIpAddress(context);
            }
        }
        return null;
    }

    public static String getDeviceRatio(Activity activity) {
        Display mDisplay = activity.getWindowManager().getDefaultDisplay();
        String deviceRatio=String.valueOf(mDisplay.getWidth())+"x"+String.valueOf(mDisplay.getHeight());
        return deviceRatio;
    }

    /**
     * 屏幕宽
     * @param context
     * @return
     */
    public static int getDeviceWidth(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }

    /**
     * 屏幕高
     * @param context
     * @return
     */
    public static int getDeviceHeight(Context context) {
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        return heightPixels;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getUserAgent(Context context) {
        WebView webview;
        webview = new WebView(context);
        webview.layout(0, 0, 0, 0);
        WebSettings settings = webview.getSettings();
        return settings.getUserAgentString();
    }
}
