package com.shunlian.app.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

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
}
