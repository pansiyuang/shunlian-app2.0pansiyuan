package com.shunlian.app.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间转换工具
 */
public class TimeUtil {

    /**
     * 当天的显示时间格式
     *
     * @param time time
     * @return HH:mm
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date(time));
    }

    /**
     * 不同一周的显示时间格式
     *
     * @param time       time
     * @param timeFormat timeFormat
     * @return String
     */
    public static String getTime(long time, String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }

    /**
     * 不同年的显示时间格式
     *
     * @param time           time
     * @param yearTimeFormat yearTimeFormat
     * @return String
     */
    public static String getYearTime(long time, String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }

    /*
     *毫秒转HH:mm:ss"
     */
    public static String timeFormHHmmss(long time) {
        long currentTime =  time - TimeZone.getDefault().getRawOffset();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。

        String hms = formatter.format(currentTime);

        return  hms;
    }

}
