package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceDetailEntity {
    public String page;
    public String page_size;
    public String count;
    public String total_page;
    public List<Balance> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Balance {
        public String type;
        public String sn;
        public String id;
        public String amount;
        public String create_time;
    }
}
