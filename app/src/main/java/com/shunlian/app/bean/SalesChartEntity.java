package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesChartEntity {

    public List<Item> list;
    public String total_child_store;
    public String total_grand_child_store;
    public String total_consume;
    public String max_value;
    public String start_date;
    public String end_date;
    public String tip;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        public String date;
        public String child_store;
        public String grand_child_store;
        public String consume_money;
        public String profit;
    }
}
