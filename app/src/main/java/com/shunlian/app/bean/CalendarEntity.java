package com.shunlian.app.bean;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/1/23.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarEntity {
    public String year;
    public String month;
    public String date;
    public String current_year;
    public String current_month;
    public String current_date;
    public String has_data;

    public String getCurrentDate() {
        String y = year;
        String m = month;
        String day = date;
        if (TextUtils.isEmpty(y) || TextUtils.isEmpty(m) || TextUtils.isEmpty(day)) {
            return null;
        }
        return y + "-" + Integer.valueOf(m) + "-" + Integer.valueOf(day);
    }
}
