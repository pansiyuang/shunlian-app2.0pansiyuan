package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneRecordEntity {
    public Pager pager;
    public List<MData> list;



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public String page;
        public String page_size;
        public String count;
        public String total_page;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String card_number;
        public String card_addr;
        public String face_price;
        public String payment_money;
        public String status_name;
        public String finish_time;
        public String image;

    }
}
