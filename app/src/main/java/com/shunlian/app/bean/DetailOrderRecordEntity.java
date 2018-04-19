package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailOrderRecordEntity {

    public PagerBean pager;
    public List<Item> list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PagerBean {
        public String page;
        public String page_size;
        public String count;
        public String total_page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        public String order_sn;
        public String status_desc;
        public String order_time;
        public List<OrderGoods> order_goods;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderGoods{
        public String goods_id;
        public String thumb;
        public String title;
        public String qty;
        public String price;
        public String sku;
        public String child_type;
        public String estimate_profit;
    }
}
