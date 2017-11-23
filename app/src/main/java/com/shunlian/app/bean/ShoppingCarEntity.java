package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/11/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCarEntity {
    public List<Enabled> enabled;
    public List<Disabled> disabled;
    public String total_amount;
    public String total_count;
    public String total_reduce;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Enabled {
        public String store_name;                 //店铺名字
        public String store_id;                   //店铺id
        public List<Voucher> store_voucher;       //店铺优惠券
        public String all_check;                  //是否选择
        public boolean isEdit;                    //编辑
        public List<Promotion> promotion;         //是否选择

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Voucher {

        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Promotion {
            public String prom_id;               //活动id
            public String prom_label;            //活动label
            public String prom_type;             //活动类型
            public List<Goods> goods;            //活动商品
            public String hint;                  //活动内容
            public String title_label;           //优惠
            public String prom_title;            //优惠标题
            public String prom_reduce;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Goods {
                public String cart_id;                  //是否被编辑
                public String store_id;              //店铺id
                public String store_name;            //店铺名字
                public String goods_id;              //商品id
                public String qty;                   //购物车数量
                public String sku_id;                //购物中的sku_id
                public String title;                 //商品title
                public String stock;                 //商品库存
                public String thumb;                 //商品封面图
                public String is_check;              //是否选择
                public String prom_type;             //活动类型
                public GoodsInfo goods_info;
                public String sku;                   //购物中的sku
                public String price;                 //价格
                public String left;                  // 剩余数量提醒，  大于等于三的时候不提醒，该值为null
                public List<AllProm> all_prom;

                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class GoodsInfo {
                    public String has_option;
                    public String price;
                    public String max_price;
                    public List<Specs> specs;

                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Specs {
                        public String id;
                        public String name;
                        public String show_type;
                        public List<values> values;

                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class values {
                            public String id;
                            public String name;
                            public String memo;
                        }
                    }
                }

                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class AllProm {
                    public String promotion_gift_id;
                    public String promotion_title;
                    public String gift_goodsid;
                    public String start_time;
                    public String end_time;
                    public String start_unixtime;
                    public String end_unixtime;
                    public String for_goods;
                    public String where_used;
                    public String gift_goodstitle;
                    public String prom_id;
                    public String prom_title;
                }
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)

    public static class Disabled {
        public String id;                         //失效id
        public String goods_id;                   //失效商品id
        public String goods_title;                //失效商品标题
        public String thumb;                      //失效商品图片
        public String status;                     //失效商品的状态 2为没有库存  3为已经下架
    }
}
