package com.shunlian.app.utils;

import android.util.Log;

import com.shunlian.app.BuildConfig;

/**
 * Created by MBENBEN on 2016/7/15 09 : 59.
 */
public class LogUtil {
    private static final boolean isOpenLog = true;

    public static void augusLogW(String msg) {
        if (isOpenLog) {
            Log.w("augus", msg);
        }
    }

    public static void zhLogW(String msg) {
        if (isOpenLog) {
            Log.d("zhang", msg);
        }
    }

    public static void httpLogW(String msg) {
        if (isOpenLog) {
            Log.w("http", msg);
        }
    }

    public static void httpLogW(Throwable tr) {
        if (isOpenLog) {
            Log.w("http", tr);
        }
    }

    public static void httpLogW(String msg, Throwable tr) {
        if (isOpenLog) {
            Log.w("http", msg, tr);
        }
    }

    public static void testLogW(String tag, String msg) {
        if (isOpenLog) {
            Log.w(tag, msg);
        }
    }

    public static void logError(String msg){
        if (isOpenLog) {
            Log.e("error", msg);
        }
    }

    /**
     * 截断输出日志
     *
     * @param msg
     */
    public static void longW(String msg) {
        if (!isOpenLog || msg == null || msg.length() == 0)
            return;
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.w("http", msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.w("http", logContent);
            }
            Log.w("http", msg);// 打印剩余日志
        }
    }
}
