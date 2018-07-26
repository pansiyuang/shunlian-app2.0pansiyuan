package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherEntity {
    public String id;
    public String store_name;
    public String store_label;
    public String denomination;
    public String use_condition;
    public String limit_data;
    public String is_store;
    public String is_get;
    public String desc;
    public String store_id;
    public String lazy_id;
    public String jump_type;
    public List<Goods> goods_list;
    public Pager pager;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public String page;
        public String page_size;
        public String count;
        public String total_page;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods {
        public String goods_id;
        public String thumb;
        public String title;
        public String price;
        public String comments_num;
        public String good_comments_num;
        public String location;
        public String free_shpping;
        public String is_new;
        public String is_hot;
        public String is_pop;
        public String voucher;
        public String discount;
        public String gift;

    }
}
