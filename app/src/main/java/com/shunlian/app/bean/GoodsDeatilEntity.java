package com.shunlian.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsDeatilEntity {

    public String id;
    public String title;
    public Detail detail;
    public String free_shipping;
    public String shipping_fee;//运费
    public String area;
    public String market_price;
    public String price;
    public String max_price;
    public String stock;
    public String thumb;
    public String status;//1为上架 0为下架
    public String is_fav;
    public ArrayList<String> pics;
    public String has_option;
    public String return_7;
    public String send_time;

    public String is_preferential;//店铺优惠  没有值的时候为空字符串
    public String member_cart_count;//详情页用户购物车角标数字
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

    public TTAct tt_act;//天天特惠

    public Act activity;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Act{
        public String desc;
        public String link;
        public String title;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TTAct{
        public String id;
        public String goods_id;
        public String title;
        public String market_price;
        public String store_id;
        public String act_price;
        public String session_status;
        public String stock;
        public String surplus_stock;
        public String remind_status;
        public String remind_count;
        public String sale;
        public String time;
        public String content;
        public String percent;
        public String str_surplus_stock;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActivityDetail {
        public String goods_id;//商品id
        public String prom_title;
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
    public static class Detail {
        public String text;
        public ArrayList<String> pics;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comments {
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
    public static class Attrs {
        public String label;
        public String value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Combo {
        public String combo_id;
        public String combo_thumb;
        public String combo_title;
        public String combo_price;
        public String max_combo_price;
        public String old_combo_price;
        public String max_old_combo_price;

        public ArrayList<Goods> goods;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StoreInfo {
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
        public String store_icon;     //店铺icon
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
        public String id;
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
        //该商品评论数
        public String comments_num;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods implements Parcelable {
        public String id;
        public String cart_id;                  //是否被编辑
        public String store_id;              //店铺id
        public String store_name;            //店铺名字
        public String goods_id;              //商品id
        public String qty;                   //购物车数量
        public String sku_id;                //购物中的sku_id
        public String goods_title;           //商品title
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
        public String cate_id;
        public String cate_name;
        public String sales;
        public String prom_id;
        public String is_new;                //是否 新品，1是，0否
        public String is_hot;                //是否 热卖，1是，0否
        public String is_explosion;          //是否 爆款，1是，0否
        public String is_recommend;          //是否 推荐，1是，0否
        public String has_coupon;            //是否 有优惠券，1是，0否
        public String has_discount;          //是否 有折扣，1是，0否
        public String has_gift;              //是否 有赠品，1是，0否
        public String free_ship;             //是否 包邮，1是，0否
        public String send_area;             //发货地
        public String comment_num;           //评论数
        public String comment_rate;          //好评率
        public String from;
        public int index;
        public boolean isSelect;
        public boolean isCheck;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.cart_id);
            dest.writeString(this.store_id);
            dest.writeString(this.store_name);
            dest.writeString(this.goods_id);
            dest.writeString(this.qty);
            dest.writeString(this.sku_id);
            dest.writeString(this.goods_title);
            dest.writeString(this.title);
            dest.writeString(this.stock);
            dest.writeString(this.thumb);
            dest.writeString(this.is_check);
            dest.writeString(this.prom_type);
            dest.writeParcelable(this.goods_info, flags);
            dest.writeString(this.sku);
            dest.writeString(this.price);
            dest.writeString(this.left);
            dest.writeList(this.all_prom);
            dest.writeString(this.cate_id);
            dest.writeString(this.cate_name);
            dest.writeString(this.sales);
            dest.writeString(this.prom_id);
            dest.writeString(this.is_new);
            dest.writeString(this.is_hot);
            dest.writeString(this.is_explosion);
            dest.writeString(this.is_recommend);
            dest.writeString(this.has_coupon);
            dest.writeString(this.has_discount);
            dest.writeString(this.has_gift);
            dest.writeString(this.free_ship);
            dest.writeString(this.send_area);
            dest.writeString(this.comment_num);
            dest.writeString(this.comment_rate);
        }

        public Goods() {
        }

        protected Goods(Parcel in) {
            this.id = in.readString();
            this.cart_id = in.readString();
            this.store_id = in.readString();
            this.store_name = in.readString();
            this.goods_id = in.readString();
            this.qty = in.readString();
            this.sku_id = in.readString();
            this.goods_title = in.readString();
            this.title = in.readString();
            this.stock = in.readString();
            this.thumb = in.readString();
            this.is_check = in.readString();
            this.prom_type = in.readString();
            this.goods_info = in.readParcelable(GoodsInfo.class.getClassLoader());
            this.sku = in.readString();
            this.price = in.readString();
            this.left = in.readString();
            this.all_prom = new ArrayList<AllProm>();
            in.readList(this.all_prom, AllProm.class.getClassLoader());
            this.cate_id = in.readString();
            this.cate_name = in.readString();
            this.sales = in.readString();
            this.prom_id = in.readString();
            this.is_new = in.readString();
            this.is_hot = in.readString();
            this.is_explosion = in.readString();
            this.is_recommend = in.readString();
            this.has_coupon = in.readString();
            this.has_discount = in.readString();
            this.has_gift = in.readString();
            this.free_ship = in.readString();
            this.send_area = in.readString();
            this.comment_num = in.readString();
            this.comment_rate = in.readString();
        }

        public static final Parcelable.Creator<Goods> CREATOR = new Parcelable.Creator<Goods>() {
            @Override
            public Goods createFromParcel(Parcel source) {
                return new Goods(source);
            }

            @Override
            public Goods[] newArray(int size) {
                return new Goods[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsInfo implements Parcelable {
        public String has_option;
        public String stock;
        public String thumb;
        public String price;
        public String max_price;
        public List<Specs> specs;
        public List<Sku> sku;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.has_option);
            dest.writeString(this.price);
            dest.writeString(this.max_price);
            dest.writeList(this.specs);
            dest.writeList(this.sku);
        }

        public GoodsInfo() {
        }

        protected GoodsInfo(Parcel in) {
            this.has_option = in.readString();
            this.price = in.readString();
            this.max_price = in.readString();
            this.specs = new ArrayList<Specs>();
            in.readList(this.specs, Specs.class.getClassLoader());
            this.sku = new ArrayList<Sku>();
            in.readList(this.sku, Sku.class.getClassLoader());
        }

        public static final Parcelable.Creator<GoodsInfo> CREATOR = new Parcelable.Creator<GoodsInfo>() {
            @Override
            public GoodsInfo createFromParcel(Parcel source) {
                return new GoodsInfo(source);
            }

            @Override
            public GoodsInfo[] newArray(int size) {
                return new GoodsInfo[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Specs implements Parcelable {
        public String id;
        public String name;
        public String show_type;
        public List<Values> values;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.show_type);
            dest.writeTypedList(this.values);
        }

        public Specs() {
        }

        protected Specs(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.show_type = in.readString();
            this.values = in.createTypedArrayList(Values.CREATOR);
        }

        public static final Parcelable.Creator<Specs> CREATOR = new Parcelable.Creator<Specs>() {
            @Override
            public Specs createFromParcel(Parcel source) {
                return new Specs(source);
            }

            @Override
            public Specs[] newArray(int size) {
                return new Specs[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Values implements Parcelable {
        public String id;
        public String name;
        public String memo;
        public boolean isSelect;


        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.memo);
            dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        }

        public Values() {
        }

        protected Values(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.memo = in.readString();
            this.isSelect = in.readByte() != 0;
        }

        public static final Parcelable.Creator<Values> CREATOR = new Parcelable.Creator<Values>() {
            @Override
            public Values createFromParcel(Parcel source) {
                return new Values(source);
            }

            @Override
            public Values[] newArray(int size) {
                return new Values[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sku implements Parcelable {
        public String id;
        public String name;
        public String price;
        public String market_price;
        public String weight;
        public String stock;
        public String specs;
        public String thumb;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.price);
            dest.writeString(this.market_price);
            dest.writeString(this.weight);
            dest.writeString(this.stock);
            dest.writeString(this.specs);
            dest.writeString(this.thumb);
        }

        public Sku() {
        }

        protected Sku(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.price = in.readString();
            this.market_price = in.readString();
            this.weight = in.readString();
            this.stock = in.readString();
            this.specs = in.readString();
            this.thumb = in.readString();
        }

        public static final Parcelable.Creator<Sku> CREATOR = new Parcelable.Creator<Sku>() {
            @Override
            public Sku createFromParcel(Parcel source) {
                return new Sku(source);
            }

            @Override
            public Sku[] newArray(int size) {
                return new Sku[size];
            }
        };
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
        public boolean isSelect;
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
