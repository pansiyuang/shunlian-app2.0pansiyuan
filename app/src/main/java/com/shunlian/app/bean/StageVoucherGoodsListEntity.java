package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2018/7/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StageVoucherGoodsListEntity {

    public CouponListEntity.VoucherList voucher_info;

    public List<GoodsList> goods_list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsList {
        public String name;
        public String avatar;
        public Level level;
        public List<ItemGoods> goods_list;
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
