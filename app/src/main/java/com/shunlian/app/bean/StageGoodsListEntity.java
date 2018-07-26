package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2018/7/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StageGoodsListEntity {

    public PagerBean pager;
    public List<ItemGoods> goods_list;
    public CouponListEntity.VoucherList voucher_info;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PagerBean {
        public String page;
        public String page_size;
        public String count;
        public String total_page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemGoods {
        public String goods_id;
        public String thumb;
        public String title;
        public String price;
        public String store_id;
        public int comments_num;
        public String good_comments_num;
        public String location;
        public String free_shpping;
        public String is_new;
        public String is_hot;
        public String is_pop;
        public String voucher;
        public String discount;
        public String gift;
        public String id;
        public String market_price;
        public String is_explosion;
        public String is_recommend;
        public String has_coupon;
        public String has_discount;
        public String has_gift;
        public String type;
        public String free_ship;
        public String send_area;
        public String introduction;
        public String sales;
        public String views;
        public int comment_num;
        public String comment_rate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Level {
        public String level;
        public String path;
    }
}
