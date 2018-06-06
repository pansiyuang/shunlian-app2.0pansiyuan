package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawListEntity {
    public Pager pager;
    public List<Record> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public String page;
        public String page_size;
        public String count;
        public String total_page;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Record {
        public String id;
        public String title;
        public String transaction_id;
        public String withdraw_account;
        public String create_time;
        public String amount;
        public boolean is_fail;

    }
}
