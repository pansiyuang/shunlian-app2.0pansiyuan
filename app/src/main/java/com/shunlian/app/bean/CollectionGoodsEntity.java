package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectionGoodsEntity {

    public List<Cates> cates;
    public List<Goods> goods;
    public String total_page;
    public String count;
    public String page;
    public String page_size;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cates {
        public String id;
        public String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods {
        public String favo_id;
        public String goods_id;
        public String title;
        public String thumb;
        public String price;
        public String favo_price;
        public String free_shipping;
        public String cate_id;
        public String name;
        public String favo_num;
        public String max_dis;
        public String has_coupon;
        public String has_discount;
        public String has_gift;
        public String status;//为0  则表示失效
        public boolean isSelect;//是否选中
    }
}
