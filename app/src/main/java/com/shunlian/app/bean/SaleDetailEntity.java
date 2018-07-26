package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleDetailEntity {


    public Page pager;
    public String total_sales;
    public String total_sales_info;
    public String total_profit;
    public String total_profit_info;
    public String total;
    public List<Item> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Page{
        public String page;
        public String page_size;
        public String count;
        public String total_page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item{
        public String sale_money;
        public String sale_type;
        public String sale_type_desc;
        public String sale_time;
        public String estimate_profit;

        //奖励
        public String type;
        public String type_desc;
        public String grant_time;
        public String money;
    }


}
