package com.shunlian.app.utils;

import android.app.Activity;

/**
 * Created by Administrator on 2017/10/27.
 */

public class MyOnClickListener {
    private static long lastClickTime;
    private static long lastRequestTime;

    /**
     * 禁止重复请求接口
     * @return
     */
    public static boolean isFastRequest() {
        long time = System.currentTimeMillis();
        if ( time - lastRequestTime < 500) {
            return true;
        }
        lastRequestTime = time;
        return false;
    }

    /**
     * 禁止快速点击
     * @return
     */
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 是否可点击
     * @param activity
     * @return
     */
    public static boolean isClickable(Activity activity) {
        if (isFastClick()) {
            return false;
        }
        if (!NetworkUtils.isNetworkAvailable(activity)){
            return false;
        }
        return true;
    }

    public static boolean isWIFIState(Activity activity) {
        if (isFastClick()) {
            return false;
        }
        if (!HttpJudge.isWifi(activity)){
            return false;
        }
        return true;
    }
}
