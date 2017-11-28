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
        public String all_check;                  //是否选择  1是已经勾选  0是未勾选
        public boolean isEditGood;                    //编辑
        public boolean isEditAll;                 //编辑所有项目
        public List<Promotion> promotion;         //是否选择

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Voucher {

        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Promotion {
            public String prom_id;               //活动id
            public String prom_label;            //活动label
            public String prom_type;             //活动类型
            public List<GoodsDeatilEntity.Goods> goods;            //活动商品
            public String hint;                  //活动内容
            public String title_label;           //优惠
            public String prom_title;            //优惠标题
            public String prom_reduce;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)

    public static class Disabled {
        public String id;                         //失效id
        public String goods_id;                   //失效商品id
        public String goods_title;                //失效商品标题
        public String sku;                        //失效商品标题
        public String thumb;                      //失效商品图片
        public String status;                     //失效商品的状态 2为没有库存  3为已经下架
    }
}
