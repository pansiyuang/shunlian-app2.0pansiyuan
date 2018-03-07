package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInRespondEntity {
    public String message;
    public String credit;
    public String total_credit;

    @Override
    public String toString() {
        return "CheckInRespondEntity{" +
                "message='" + message + '\'' +
                ", credit='" + credit + '\'' +
                ", total_credit='" + total_credit + '\'' +
                '}';
    }
}
