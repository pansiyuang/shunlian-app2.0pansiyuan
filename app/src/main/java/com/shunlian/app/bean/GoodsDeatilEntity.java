package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsDeatilEntity {

    public String id;
    public String title;
    public Detail detail;
    public String free_shipping;
    public String area;
    public String market_price;
    public String price;
    public String max_price;
    public String stock;
    public String thumb;
    public String is_fav;
    public ArrayList<String> pics;


    public String is_new; //是否新品
    public String is_explosion;//是否爆款
    public String is_hot;//是否热卖
    public String is_recommend;//是否推荐

    public ArrayList<Sku> sku;//属性组合列表
    public ArrayList<Specs> specs;
    public GoodsData goods_data;
    public ArrayList<ActivityDetail> full_cut;//满减
    public ArrayList<ActivityDetail> full_discount;//满折
    public ArrayList<ActivityDetail> buy_gift;//买赠


    public ArrayList<Voucher> voucher;

    public StoreInfo store_info;

    public ArrayList<Combo> combo;
    public ArrayList<Attrs> attrs;
    public ArrayList<Comments> comments;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActivityDetail{
        public String goods_id;//商品id
        public String promotion_gift_id; //买赠活动id
        public String promotion_title;     //买赠活动标题
        public String gift_goodsid;    //赠送的商品id
        public String promotion_discount_id; //满减活动id
        public String qty_type_condition;   //打折条件 （10件）
        public String qty_type_discount;   //折扣值 （6.5折）
        public String title;   //满减活动标题
        public String type;  //活动类型 MONEY 满减    QTY 满折
        public String money_type_condition; //满减条件
        public String money_type_discount; //减免额度
        public String money_type_loop; //是否阶梯减免
        public String start_time; //开始时间
        public String end_time;//结束时间d
        public String start_unixtime;//开始时间的unix时间戳
        public String end_unixtime;// 结束时间的unix时间戳
        public String for_goods; // 是否全部商品  ALL为全部  CUSTOM为指定
        public String where_used;   //使用位置，1为通用 2为app
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail{
        public String text;
        public ArrayList<String> pics;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comments{
        public String id;
        public String star_level;
        public String addtime;
        public String buytime;
        public String sku_desc;
        public String content;
        public String reply;
        public String reply_time;
        public String vip_level;
        public String nickname;
        public String avatar;
        public ArrayList<String> pics;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attrs{
        public String label;
        public String value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Combo{
        public String combo_id;
        public String combo_thumb;
        public String combo_title;
        public String combo_price;
        public String max_combo_price;
        public String old_combo_price;
        public String max_old_combo_price;

        public ArrayList<Goods> goods;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Goods{
            public String goods_id;
            public String thumb;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StoreInfo{
        public String decoration_name;//店铺名字
        public String star; //星级
        public String quality_logo;  //是否有品质标志1是0否
        public String goods_count;    //商品数量
        public String decoration_banner;    //店铺背景图
        public String attention_count;      //收藏数
        public String description_consistency;      //描述相符度
        public String quality_satisfy;     //质量满意度
        public String is_attention;     //1是已经收藏， 未登录就返回0
        public String store_id;     //店铺id
        public ArrayList<Item> hot;//店铺热销
        public ArrayList<Item> push;//店主推荐

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item {
            public String id;
            public String title;
            public String thumb;
            public String price;
            public String whole_thumb;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Voucher {
        public String voucher_id;
        public String store_id;
        public String title;
        public String start_time;
        public String end_time;
        public String denomination;
        public String use_condition;
        public String memo;
        public String is_get; //1为已经领取 未登录就返回0
        public String goods_scope;//ALL为全店 ASSIGN为指定
    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsData {
        //销量
        public String sales;
        //浏览量
        public String views;
        //好评率
        public String star_rate;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sku {
        public String id;
        public String price;
        public String market_price;
        public String weight;
        public String stock;
        public String specs;
        public String name;

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Specs {
        public String id;
        public String name;
        public String show_type;
        public ArrayList<Values> values;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Values {
            public String id;
            public String name;
            public String memo;
            public String thumb;
            private boolean isSelect;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }


}
