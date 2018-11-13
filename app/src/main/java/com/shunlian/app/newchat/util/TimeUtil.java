package com.shunlian.app.newchat.util;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间转换工具
 */
public class TimeUtil {

    static String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    private TimeUtil() {
    }

    /**
     * 时间转化为显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        if (calendar.before(inputTime)) {
            //今天23:59在输入时间之前，解决一些时间误差，把当天时间显示到这里
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + Common.getApplicationContext().getResources().getString(R.string.year)
                    + "MM" + Common.getApplicationContext().getResources().getString(R.string.month)
                    + "dd" + Common.getApplicationContext().getResources().getString(R.string.day));
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            return Common.getApplicationContext().getResources().getString(R.string.time_yesterday);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + Common.getApplicationContext().getResources().getString(R.string.month) + "d" + Common.getApplicationContext().getResources().getString(R.string.day));
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + Common.getApplicationContext().getResources().getString(R.string.year) + "MM" + Common.getApplicationContext().getResources().getString(R.string.month) + "dd" + Common.getApplicationContext().getResources().getString(R.string.day));
                return sdf.format(currenTimeZone);

            }

        }

    }

    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getChatTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        if (!calendar.after(inputTime)) {
            //当前时间在输入时间之前
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + Common.getApplicationContext().getResources().getString(R.string.year) + "MM" + Common.getApplicationContext().getResources().getString(R.string.month) + "dd" + Common.getApplicationContext().getResources().getString(R.string.day));
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return Common.getApplicationContext().getResources().getString(R.string.time_yesterday) + " " + sdf.format(currenTimeZone);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + Common.getApplicationContext().getResources().getString(R.string.month) + "d" + Common.getApplicationContext().getResources().getString(R.string.day) + " HH:mm");
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + Common.getApplicationContext().getResources().getString(R.string.year) + "MM" + Common.getApplicationContext().getResources().getString(R.string.month) + "dd" + Common.getApplicationContext().getResources().getString(R.string.day) + " HH:mm");
                return sdf.format(currenTimeZone);
            }

        }

    }

    /**
     * 时间戳格式转换
     */
    public static String getNewChatTime(long timestamp) {
        String result;
        long currentTime = timestamp * 1000;
        Calendar curCalendar = Calendar.getInstance();
        Calendar showCalendar = Calendar.getInstance();
        showCalendar.setTimeInMillis(currentTime);

        String timeFormat;
        String yearTimeFormat;
        String am_pm = "";
        int hour = showCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            am_pm = "凌晨 ";
        } else if (hour >= 6 && hour < 12) {
            am_pm = "上午 ";
        } else if (hour == 12) {
            am_pm = "中午 ";
        } else if (hour > 12 && hour < 18) {
            am_pm = "下午 ";
        } else if (hour >= 18) {
            am_pm = "晚上 ";
        }
        timeFormat = "M月d日 " + am_pm + "HH:mm:ss";
        yearTimeFormat = "yyyy年M月d日 " + am_pm + "HH:mm";

        boolean yearTemp = curCalendar.get(Calendar.YEAR) == showCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int curMonth = curCalendar.get(Calendar.MONTH);
            int showMonth = showCalendar.get(Calendar.MONTH);
            // 表示是同一个月
            if (curMonth == showMonth) {
                int temp = curCalendar.get(Calendar.DATE) - showCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = am_pm + getHourAndMin(currentTime);
                        break;
                    case 1:
                        result = "昨天" + am_pm + getHourAndMin(currentTime);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int curDayOfMonth = curCalendar.get(Calendar.WEEK_OF_MONTH);
                        int showDayOfMonth = showCalendar.get(Calendar.WEEK_OF_MONTH);
                        // 表示是同一周
                        if (showDayOfMonth == curDayOfMonth) {
                            int dayOfWeek = showCalendar.get(Calendar.DAY_OF_WEEK);
                            //判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                            if (dayOfWeek != 1) {
                                result = dayNames[showCalendar.get(Calendar.DAY_OF_WEEK) - 1] + getHourAndMin(currentTime);
                            } else {
                                result = getTime(currentTime, timeFormat);
                            }
                        } else {
                            result = getTime(currentTime, timeFormat);
                        }
                        break;
                    default:
                        result = getTime(currentTime, timeFormat);
                        break;
                }
            } else {
                result = getTime(currentTime, timeFormat);
            }
        } else {
            result = getYearTime(currentTime, yearTimeFormat);
        }
        return result;
    }

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
     * 当天的显示时间格式
     *
     * @param time time
     * @return HH:mm
     */
    public static String getyMdHMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat( "MM" + Common.getApplicationContext().getResources().getString(R.string.month) + "dd" + Common.getApplicationContext().getResources().getString(R.string.day) +"HH点");
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
