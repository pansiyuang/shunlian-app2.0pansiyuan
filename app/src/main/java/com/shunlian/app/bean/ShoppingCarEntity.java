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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Enabled {
        public String store_name;                 //店铺名字
        public String store_id;                   //店铺id
        public List<Goods> goods;                 //店铺商品

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Goods {
            public String id;                    //购物车id
            public String store_id;              //店铺id
            public String store_name;            //店铺名字
            public String goods_id;              //商品id
            public String qty;                   //购物车数量
            public String sku_id;                //购物中的sku_id
            public String title;                 //商品title
            public String thumb;                 //商品封面图
            public String sku;                   //sku信息
            public String price;                 //价格
            public String left;                  // 剩余数量提醒，  大于等于三的时候不提醒，该值为null

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
