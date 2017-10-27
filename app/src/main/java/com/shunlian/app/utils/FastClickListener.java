package com.shunlian.app.utils;

import android.app.Activity;

/**
 * Created by Administrator on 2017/10/27.
 */

public class FastClickListener {
    private static long lastClickTime;

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
}
