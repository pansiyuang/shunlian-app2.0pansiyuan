package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyOrderEntity {

    public String status;
    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public List<Orders> orders;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Orders {
        public String id;
        public String total_amount;
        public String store_name;
        public String shipping_fee;
        public String store_id;
        public String status_text;
        public String is_append;
        public String status;
        public String is_edit_praise;
        public String qty;
        public List<OrderGoodsBean> order_goods;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderGoodsBean {
        public String goods_id;
        public String title;
        public String thumb;
        public String qty;
        public String market_price;
        public double price;
        public String sku_desc;
        public String offered;

    }
}
