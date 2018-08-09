package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoreNewsEntity {
    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public List<Goods> goods_list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods {
        public String id;
        public String title;
        public String thumb;
        public String price;
        public String is_new;
        public String is_hot;
        public String is_recommend;
        public String is_explosion;
        public String has_coupon;
        public String has_discount;
        public String has_gift;
        public String free_ship;
        public String send_area;
        public String sales;
        public String views;
        public String stock;
        public String introduction;
        public String comment_num;
        public String comment_rate;
    }
}
