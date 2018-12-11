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
public class GoodsDeatilEntity implements Parcelable {

    public String id;
    public String title;
    public String introduction;//商品简介
    public String limit_min_buy;//团购商品最少购买数
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
    public String quality_guarantee;
    public ArrayList<SimpTitle> services;
    public String credit;
    public String video;//视频地址
    public String type;// 普通商品0，优品1,团购商品2
    public String type_tag_pic;// 普通商品0，优品1,团购商品2
    public String get_gold_second;// 获取金蛋的停留时间

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
    public String cate_1_name;//一级分类
    public String cate_2_name;//二级分类

    public ArrayList<Voucher> voucher;

    public StoreInfo store_info;

    public ArrayList<Combo> combo;
    public ArrayList<Attrs> attrs;
    public ArrayList<Comments> comments;

    public TTAct tt_act;//天天特惠
    public SpecailAct common_activity;//专题活动

    public Act activity;
    //分享信息
    public UserInfo user_info;
    public String self_buy_earn;//自购赚多少，该字段有返回时才前台显示（该字段有可能不返回）

    public String share_buy_earn;
    public String share_buy_earn_btn;
    public String self_buy_earn_btn;
    public PlusDoor plus_door;

    public static class SimpTitle implements Parcelable {
        public String title;
        public String content;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.content);
        }

        public SimpTitle() {
        }

        protected SimpTitle(Parcel in) {
            this.title = in.readString();
            this.content = in.readString();
        }

        public static final Creator<SimpTitle> CREATOR = new Creator<SimpTitle>() {
            @Override
            public SimpTitle createFromParcel(Parcel source) {
                return new SimpTitle(source);
            }

            @Override
            public SimpTitle[] newArray(int size) {
                return new SimpTitle[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlusDoor extends SimpTitle implements Parcelable {
        public String is_plus;
        public UrlType url;
        public ArrayList<String> title_highlight;
        public ArrayList<String> content_highlight;

        public PlusDoor() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.is_plus);
            dest.writeParcelable(this.url, flags);
            dest.writeStringList(this.title_highlight);
            dest.writeStringList(this.content_highlight);
        }

        protected PlusDoor(Parcel in) {
            this.is_plus = in.readString();
            this.url = in.readParcelable(UrlType.class.getClassLoader());
            this.title_highlight = in.createStringArrayList();
            this.content_highlight = in.createStringArrayList();
        }

        public static final Creator<PlusDoor> CREATOR = new Creator<PlusDoor>() {
            @Override
            public PlusDoor createFromParcel(Parcel source) {
                return new PlusDoor(source);
            }

            @Override
            public PlusDoor[] newArray(int size) {
                return new PlusDoor[size];
            }
        };
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo implements Parcelable {
        public String nickname;
        public String avatar;
        public String share_url;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.nickname);
            dest.writeString(this.avatar);
            dest.writeString(this.share_url);
        }

        public UserInfo() {
        }

        protected UserInfo(Parcel in) {
            this.nickname = in.readString();
            this.avatar = in.readString();
            this.share_url = in.readString();
        }

        public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
            @Override
            public UserInfo createFromParcel(Parcel source) {
                return new UserInfo(source);
            }

            @Override
            public UserInfo[] newArray(int size) {
                return new UserInfo[size];
            }
        };
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SpecailAct implements Parcelable {
        public String act_id;
        public String detail_pic;
        public String if_act_price;
        public String if_time;
        public String tag_pic;
        public String actprice;
        public String activity_status;
        public String start_remain_seconds;
        public String end_remain_seconds;
        public String start_time;
        public String old_price;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.act_id);
            dest.writeString(this.detail_pic);
            dest.writeString(this.if_act_price);
            dest.writeString(this.if_time);
            dest.writeString(this.actprice);
            dest.writeString(this.tag_pic);
            dest.writeString(this.activity_status);
            dest.writeString(this.start_remain_seconds);
            dest.writeString(this.end_remain_seconds);
            dest.writeString(this.start_time);
            dest.writeString(this.old_price);
        }

        public SpecailAct() {
        }

        protected SpecailAct(Parcel in) {
            this.act_id = in.readString();
            this.detail_pic = in.readString();
            this.if_act_price = in.readString();
            this.if_time = in.readString();
            this.actprice = in.readString();
            this.tag_pic = in.readString();
            this.activity_status = in.readString();
            this.start_remain_seconds = in.readString();
            this.end_remain_seconds = in.readString();
            this.start_time = in.readString();
            this.old_price = in.readString();
        }

        public static final Parcelable.Creator<SpecailAct> CREATOR = new Parcelable.Creator<SpecailAct>() {
            @Override
            public SpecailAct createFromParcel(Parcel source) {
                return new SpecailAct(source);
            }

            @Override
            public SpecailAct[] newArray(int size) {
                return new SpecailAct[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Act implements Parcelable {
        public String desc;
        public Url url;
        public String title;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.desc);
            dest.writeParcelable(this.url, flags);
            dest.writeString(this.title);
        }

        public Act() {
        }

        protected Act(Parcel in) {
            this.desc = in.readString();
            this.url = in.readParcelable(Url.class.getClassLoader());
            this.title = in.readString();
        }

        public static final Parcelable.Creator<Act> CREATOR = new Parcelable.Creator<Act>() {
            @Override
            public Act createFromParcel(Parcel source) {
                return new Act(source);
            }

            @Override
            public Act[] newArray(int size) {
                return new Act[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Url implements Parcelable {
        public String type;
        public String item_id;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.type);
            dest.writeString(this.item_id);
        }

        public Url() {
        }

        protected Url(Parcel in) {
            this.type = in.readString();
            this.item_id = in.readString();
        }

        public static final Parcelable.Creator<Url> CREATOR = new Parcelable.Creator<Url>() {
            @Override
            public Url createFromParcel(Parcel source) {
                return new Url(source);
            }

            @Override
            public Url[] newArray(int size) {
                return new Url[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TTAct implements Parcelable {
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
        public String start_time;
        public String end_time;
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.goods_id);
            dest.writeString(this.title);
            dest.writeString(this.market_price);
            dest.writeString(this.store_id);
            dest.writeString(this.act_price);
            dest.writeString(this.session_status);
            dest.writeString(this.stock);
            dest.writeString(this.surplus_stock);
            dest.writeString(this.remind_status);
            dest.writeString(this.remind_count);
            dest.writeString(this.sale);
            dest.writeString(this.time);
            dest.writeString(this.content);
            dest.writeString(this.percent);
            dest.writeString(this.str_surplus_stock);
            dest.writeString(this.start_time);
        }

        public TTAct() {
        }

        protected TTAct(Parcel in) {
            this.id = in.readString();
            this.goods_id = in.readString();
            this.title = in.readString();
            this.market_price = in.readString();
            this.store_id = in.readString();
            this.act_price = in.readString();
            this.session_status = in.readString();
            this.stock = in.readString();
            this.surplus_stock = in.readString();
            this.remind_status = in.readString();
            this.remind_count = in.readString();
            this.sale = in.readString();
            this.time = in.readString();
            this.content = in.readString();
            this.percent = in.readString();
            this.str_surplus_stock = in.readString();
            this.start_time = in.readString();
        }

        public static final Parcelable.Creator<TTAct> CREATOR = new Parcelable.Creator<TTAct>() {
            @Override
            public TTAct createFromParcel(Parcel source) {
                return new TTAct(source);
            }

            @Override
            public TTAct[] newArray(int size) {
                return new TTAct[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActivityDetail implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.goods_id);
            dest.writeString(this.prom_title);
            dest.writeString(this.promotion_gift_id);
            dest.writeString(this.promotion_title);
            dest.writeString(this.gift_goodsid);
            dest.writeString(this.promotion_discount_id);
            dest.writeString(this.qty_type_condition);
            dest.writeString(this.qty_type_discount);
            dest.writeString(this.title);
            dest.writeString(this.type);
            dest.writeString(this.money_type_condition);
            dest.writeString(this.money_type_discount);
            dest.writeString(this.money_type_loop);
            dest.writeString(this.start_time);
            dest.writeString(this.end_time);
            dest.writeString(this.start_unixtime);
            dest.writeString(this.end_unixtime);
            dest.writeString(this.for_goods);
            dest.writeString(this.where_used);
        }

        public ActivityDetail() {
        }

        protected ActivityDetail(Parcel in) {
            this.goods_id = in.readString();
            this.prom_title = in.readString();
            this.promotion_gift_id = in.readString();
            this.promotion_title = in.readString();
            this.gift_goodsid = in.readString();
            this.promotion_discount_id = in.readString();
            this.qty_type_condition = in.readString();
            this.qty_type_discount = in.readString();
            this.title = in.readString();
            this.type = in.readString();
            this.money_type_condition = in.readString();
            this.money_type_discount = in.readString();
            this.money_type_loop = in.readString();
            this.start_time = in.readString();
            this.end_time = in.readString();
            this.start_unixtime = in.readString();
            this.end_unixtime = in.readString();
            this.for_goods = in.readString();
            this.where_used = in.readString();
        }

        public static final Parcelable.Creator<ActivityDetail> CREATOR = new Parcelable.Creator<ActivityDetail>() {
            @Override
            public ActivityDetail createFromParcel(Parcel source) {
                return new ActivityDetail(source);
            }

            @Override
            public ActivityDetail[] newArray(int size) {
                return new ActivityDetail[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail implements Parcelable {
        public String text;
        public ArrayList<String> pics;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.text);
            dest.writeStringList(this.pics);
        }

        public Detail() {
        }

        protected Detail(Parcel in) {
            this.text = in.readString();
            this.pics = in.createStringArrayList();
        }

        public static final Parcelable.Creator<Detail> CREATOR = new Parcelable.Creator<Detail>() {
            @Override
            public Detail createFromParcel(Parcel source) {
                return new Detail(source);
            }

            @Override
            public Detail[] newArray(int size) {
                return new Detail[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comments implements Parcelable {
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
        public String plus_role;
        public ArrayList<String> pics;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.star_level);
            dest.writeString(this.addtime);
            dest.writeString(this.buytime);
            dest.writeString(this.sku_desc);
            dest.writeString(this.content);
            dest.writeString(this.reply);
            dest.writeString(this.reply_time);
            dest.writeString(this.vip_level);
            dest.writeString(this.nickname);
            dest.writeString(this.avatar);
            dest.writeString(this.plus_role);
            dest.writeStringList(this.pics);
        }

        public Comments() {
        }

        protected Comments(Parcel in) {
            this.id = in.readString();
            this.star_level = in.readString();
            this.addtime = in.readString();
            this.buytime = in.readString();
            this.sku_desc = in.readString();
            this.content = in.readString();
            this.reply = in.readString();
            this.reply_time = in.readString();
            this.vip_level = in.readString();
            this.nickname = in.readString();
            this.avatar = in.readString();
            this.plus_role = in.readString();
            this.pics = in.createStringArrayList();
        }

        public static final Parcelable.Creator<Comments> CREATOR = new Parcelable.Creator<Comments>() {
            @Override
            public Comments createFromParcel(Parcel source) {
                return new Comments(source);
            }

            @Override
            public Comments[] newArray(int size) {
                return new Comments[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attrs implements Parcelable {
        public String label;
        public String value;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.label);
            dest.writeString(this.value);
        }

        public Attrs() {
        }

        protected Attrs(Parcel in) {
            this.label = in.readString();
            this.value = in.readString();
        }

        public static final Parcelable.Creator<Attrs> CREATOR = new Parcelable.Creator<Attrs>() {
            @Override
            public Attrs createFromParcel(Parcel source) {
                return new Attrs(source);
            }

            @Override
            public Attrs[] newArray(int size) {
                return new Attrs[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Combo implements Parcelable {
        public String combo_id;
        public String combo_thumb;
        public String combo_title;
        public String combo_price;
        public String max_combo_price;
        public String old_combo_price;
        public String max_old_combo_price;

        public ArrayList<Goods> goods;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.combo_id);
            dest.writeString(this.combo_thumb);
            dest.writeString(this.combo_title);
            dest.writeString(this.combo_price);
            dest.writeString(this.max_combo_price);
            dest.writeString(this.old_combo_price);
            dest.writeString(this.max_old_combo_price);
            dest.writeList(this.goods);
        }

        public Combo() {
        }

        protected Combo(Parcel in) {
            this.combo_id = in.readString();
            this.combo_thumb = in.readString();
            this.combo_title = in.readString();
            this.combo_price = in.readString();
            this.max_combo_price = in.readString();
            this.old_combo_price = in.readString();
            this.max_old_combo_price = in.readString();
            this.goods = new ArrayList<Goods>();
            in.readList(this.goods, Goods.class.getClassLoader());
        }

        public static final Parcelable.Creator<Combo> CREATOR = new Parcelable.Creator<Combo>() {
            @Override
            public Combo createFromParcel(Parcel source) {
                return new Combo(source);
            }

            @Override
            public Combo[] newArray(int size) {
                return new Combo[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StoreInfo implements Parcelable {
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
        public ArrayList<ActItem> push_goods;//灵活版的hot 和 push

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ActItem implements Parcelable {
            public String title;
            public ArrayList<Item> data;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.title);
                dest.writeTypedList(this.data);
            }

            public ActItem() {
            }

            protected ActItem(Parcel in) {
                this.title = in.readString();
                this.data = in.createTypedArrayList(Item.CREATOR);
            }

            public static final Creator<ActItem> CREATOR = new Creator<ActItem>() {
                @Override
                public ActItem createFromParcel(Parcel source) {
                    return new ActItem(source);
                }

                @Override
                public ActItem[] newArray(int size) {
                    return new ActItem[size];
                }
            };
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item implements Parcelable {
            public String id;
            public String title;
            public String thumb;
            public String price;
            public String whole_thumb;
            public String tag;
            public String self_buy_earn;
            public String share_buy_earn;
            public String cost_price;

            public Item() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.title);
                dest.writeString(this.thumb);
                dest.writeString(this.price);
                dest.writeString(this.whole_thumb);
                dest.writeString(this.tag);
                dest.writeString(this.self_buy_earn);
                dest.writeString(this.share_buy_earn);
                dest.writeString(this.cost_price);
            }

            protected Item(Parcel in) {
                this.id = in.readString();
                this.title = in.readString();
                this.thumb = in.readString();
                this.price = in.readString();
                this.whole_thumb = in.readString();
                this.tag = in.readString();
                this.self_buy_earn = in.readString();
                this.share_buy_earn = in.readString();
                this.cost_price = in.readString();
            }

            public static final Creator<Item> CREATOR = new Creator<Item>() {
                @Override
                public Item createFromParcel(Parcel source) {
                    return new Item(source);
                }

                @Override
                public Item[] newArray(int size) {
                    return new Item[size];
                }
            };
        }

        public StoreInfo() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.decoration_name);
            dest.writeString(this.star);
            dest.writeString(this.quality_logo);
            dest.writeString(this.goods_count);
            dest.writeString(this.decoration_banner);
            dest.writeString(this.attention_count);
            dest.writeString(this.description_consistency);
            dest.writeString(this.quality_satisfy);
            dest.writeString(this.is_attention);
            dest.writeString(this.store_id);
            dest.writeString(this.store_icon);
            dest.writeTypedList(this.hot);
            dest.writeTypedList(this.push);
            dest.writeList(this.push_goods);
        }

        protected StoreInfo(Parcel in) {
            this.decoration_name = in.readString();
            this.star = in.readString();
            this.quality_logo = in.readString();
            this.goods_count = in.readString();
            this.decoration_banner = in.readString();
            this.attention_count = in.readString();
            this.description_consistency = in.readString();
            this.quality_satisfy = in.readString();
            this.is_attention = in.readString();
            this.store_id = in.readString();
            this.store_icon = in.readString();
            this.hot = in.createTypedArrayList(Item.CREATOR);
            this.push = in.createTypedArrayList(Item.CREATOR);
            this.push_goods = new ArrayList<ActItem>();
            in.readList(this.push_goods, ActItem.class.getClassLoader());
        }

        public static final Creator<StoreInfo> CREATOR = new Creator<StoreInfo>() {
            @Override
            public StoreInfo createFromParcel(Parcel source) {
                return new StoreInfo(source);
            }

            @Override
            public StoreInfo[] newArray(int size) {
                return new StoreInfo[size];
            }
        };
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Voucher implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.voucher_id);
            dest.writeString(this.id);
            dest.writeString(this.store_id);
            dest.writeString(this.title);
            dest.writeString(this.start_time);
            dest.writeString(this.end_time);
            dest.writeString(this.denomination);
            dest.writeString(this.use_condition);
            dest.writeString(this.memo);
            dest.writeString(this.is_get);
            dest.writeString(this.goods_scope);
        }

        public Voucher() {
        }

        protected Voucher(Parcel in) {
            this.voucher_id = in.readString();
            this.id = in.readString();
            this.store_id = in.readString();
            this.title = in.readString();
            this.start_time = in.readString();
            this.end_time = in.readString();
            this.denomination = in.readString();
            this.use_condition = in.readString();
            this.memo = in.readString();
            this.is_get = in.readString();
            this.goods_scope = in.readString();
        }

        public static final Parcelable.Creator<Voucher> CREATOR = new Parcelable.Creator<Voucher>() {
            @Override
            public Voucher createFromParcel(Parcel source) {
                return new Voucher(source);
            }

            @Override
            public Voucher[] newArray(int size) {
                return new Voucher[size];
            }
        };
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsData implements Parcelable {
        //销量
        public String sales;
        public String sales_desc;
        //浏览量
        public String views;
        //好评率
        public String star_rate;
        //该商品评论数
        public String comments_num;
        public String want_num;//还想要的数量

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.sales);
            dest.writeString(this.sales_desc);
            dest.writeString(this.views);
            dest.writeString(this.star_rate);
            dest.writeString(this.comments_num);
            dest.writeString(this.want_num);
        }

        public GoodsData() {
        }

        protected GoodsData(Parcel in) {
            this.sales = in.readString();
            this.sales_desc = in.readString();
            this.views = in.readString();
            this.star_rate = in.readString();
            this.comments_num = in.readString();
            this.want_num = in.readString();
        }

        public static final Parcelable.Creator<GoodsData> CREATOR = new Parcelable.Creator<GoodsData>() {
            @Override
            public GoodsData createFromParcel(Parcel source) {
                return new GoodsData(source);
            }

            @Override
            public GoodsData[] newArray(int size) {
                return new GoodsData[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods implements Parcelable {
        public String id;
        public String tag_pic;
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
        public String market_price;                 //价格
        public String old_price;                 //套餐原价
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
        public int type;                     //0普通商品、1优品、2团购
        public int is_sell_out;
        public String sales_desc;
        public String desc;
        public int isSuperiorProduct;
        public String share_buy_earn;
        public String self_buy_earn;
        public String free_ship;             //是否 包邮，1是，0否
        public String send_area;             //发货地
        public String comment_num;           //评论数
        public String comment_rate;          //好评率
        public String from;
        public EveryDay every_day;
        public int index;
        public boolean isSelect;
        public String every_day_ing;
        public String reduced;
        public String limit_min_buy;//团购商品最少购买数
        public String big_label;//团购商品最少购买数
        public int share_num;
        public String share_url;

        public Goods() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.cart_id);
            dest.writeString(this.tag_pic);
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
            dest.writeString(this.old_price);
            dest.writeString(this.left);
            dest.writeString(this.market_price);
            dest.writeString(this.share_url);
            dest.writeTypedList(this.all_prom);
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
            dest.writeInt(this.type);
            dest.writeInt(this.is_sell_out);
            dest.writeString(this.sales_desc);
            dest.writeString(this.self_buy_earn);
            dest.writeString(this.free_ship);
            dest.writeString(this.send_area);
            dest.writeString(this.comment_num);
            dest.writeString(this.comment_rate);
            dest.writeString(this.from);
            dest.writeParcelable(this.every_day, flags);
            dest.writeInt(this.index);
            dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
            dest.writeString(this.every_day_ing);
            dest.writeString(this.reduced);
            dest.writeString(this.limit_min_buy);
            dest.writeString(this.big_label);
            dest.writeInt(this.share_num);
            dest.writeInt(this.isSuperiorProduct);
            dest.writeString(this.desc);
        }

        protected Goods(Parcel in) {
            this.id = in.readString();
            this.cart_id = in.readString();
            this.tag_pic = in.readString();
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
            this.old_price = in.readString();
            this.left = in.readString();
            this.market_price = in.readString();
            this.share_url = in.readString();
            this.all_prom = in.createTypedArrayList(AllProm.CREATOR);
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
            this.type = in.readInt();
            this.is_sell_out = in.readInt();
            this.sales_desc = in.readString();
            this.self_buy_earn = in.readString();
            this.free_ship = in.readString();
            this.send_area = in.readString();
            this.comment_num = in.readString();
            this.comment_rate = in.readString();
            this.from = in.readString();
            this.every_day = in.readParcelable(EveryDay.class.getClassLoader());
            this.index = in.readInt();
            this.isSelect = in.readByte() != 0;
            this.every_day_ing = in.readString();
            this.reduced = in.readString();
            this.limit_min_buy = in.readString();
            this.big_label = in.readString();
            this.share_num = in.readInt();
            this.isSuperiorProduct = in.readInt();
            this.desc = in.readString();
        }

        public static final Creator<Goods> CREATOR = new Creator<Goods>() {
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
    public static class EveryDay implements Parcelable {
        public String left_time;
        public String remind;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.left_time);
            dest.writeString(this.remind);
        }

        public EveryDay() {
        }

        protected EveryDay(Parcel in) {
            this.left_time = in.readString();
            this.remind = in.readString();
        }

        public static final Parcelable.Creator<EveryDay> CREATOR = new Parcelable.Creator<EveryDay>() {
            @Override
            public EveryDay createFromParcel(Parcel source) {
                return new EveryDay(source);
            }

            @Override
            public EveryDay[] newArray(int size) {
                return new EveryDay[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsInfo implements Parcelable {
        public String has_option;
        public String stock;
        public String thumb;
        public String price;
        public int type;
        public String max_price;
        public String limit_min_buy;
        public List<Specs> specs;
        public List<Sku> sku;

        public GoodsInfo() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.has_option);
            dest.writeString(this.stock);
            dest.writeString(this.thumb);
            dest.writeString(this.price);
            dest.writeInt(this.type);
            dest.writeString(this.max_price);
            dest.writeString(this.limit_min_buy);
            dest.writeTypedList(this.specs);
            dest.writeTypedList(this.sku);
        }

        protected GoodsInfo(Parcel in) {
            this.has_option = in.readString();
            this.stock = in.readString();
            this.thumb = in.readString();
            this.price = in.readString();
            this.type = in.readInt();
            this.max_price = in.readString();
            this.limit_min_buy = in.readString();
            this.specs = in.createTypedArrayList(Specs.CREATOR);
            this.sku = in.createTypedArrayList(Sku.CREATOR);
        }

        public static final Creator<GoodsInfo> CREATOR = new Creator<GoodsInfo>() {
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
            dest.writeList(this.values);
        }

        public Specs() {
        }

        protected Specs(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.show_type = in.readString();
            this.values = new ArrayList<Values>();
            in.readList(this.values, Values.class.getClassLoader());
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
        public String old_price;//套餐原价
        public String market_price;
        public String weight;
        public String stock;
        public String specs;
        public String thumb;
        public String share_buy_earn; //分享赚
        public String self_buy_earn; //自购省

        public Sku() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.price);
            dest.writeString(this.old_price);
            dest.writeString(this.market_price);
            dest.writeString(this.weight);
            dest.writeString(this.stock);
            dest.writeString(this.specs);
            dest.writeString(this.thumb);
            dest.writeString(this.share_buy_earn);
            dest.writeString(this.self_buy_earn);
        }

        protected Sku(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.price = in.readString();
            this.old_price = in.readString();
            this.market_price = in.readString();
            this.weight = in.readString();
            this.stock = in.readString();
            this.specs = in.readString();
            this.thumb = in.readString();
            this.share_buy_earn = in.readString();
            this.self_buy_earn = in.readString();
        }

        public static final Creator<Sku> CREATOR = new Creator<Sku>() {
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
    public static class AllProm implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.promotion_gift_id);
            dest.writeString(this.promotion_title);
            dest.writeString(this.gift_goodsid);
            dest.writeString(this.start_time);
            dest.writeString(this.end_time);
            dest.writeString(this.start_unixtime);
            dest.writeString(this.end_unixtime);
            dest.writeString(this.for_goods);
            dest.writeString(this.where_used);
            dest.writeString(this.gift_goodstitle);
            dest.writeString(this.prom_id);
            dest.writeString(this.prom_title);
            dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        }

        public AllProm() {
        }

        protected AllProm(Parcel in) {
            this.promotion_gift_id = in.readString();
            this.promotion_title = in.readString();
            this.gift_goodsid = in.readString();
            this.start_time = in.readString();
            this.end_time = in.readString();
            this.start_unixtime = in.readString();
            this.end_unixtime = in.readString();
            this.for_goods = in.readString();
            this.where_used = in.readString();
            this.gift_goodstitle = in.readString();
            this.prom_id = in.readString();
            this.prom_title = in.readString();
            this.isSelect = in.readByte() != 0;
        }

        public static final Parcelable.Creator<AllProm> CREATOR = new Parcelable.Creator<AllProm>() {
            @Override
            public AllProm createFromParcel(Parcel source) {
                return new AllProm(source);
            }

            @Override
            public AllProm[] newArray(int size) {
                return new AllProm[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Promotion implements Parcelable {
        public String prom_status;
        public String prom_id;               //活动id
        public String prom_label;            //活动label
        public String prom_type;             //活动类型
        public List<GoodsDeatilEntity.Goods> goods;            //活动商品
        public String hint;                  //活动内容
        public String title_label;           //优惠
        public String prom_title;            //优惠标题
        public String prom_reduce;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.prom_status);
            dest.writeString(this.prom_id);
            dest.writeString(this.prom_label);
            dest.writeString(this.prom_type);
            dest.writeTypedList(this.goods);
            dest.writeString(this.hint);
            dest.writeString(this.title_label);
            dest.writeString(this.prom_title);
            dest.writeString(this.prom_reduce);
        }

        public Promotion() {
        }

        protected Promotion(Parcel in) {
            this.prom_status = in.readString();
            this.prom_id = in.readString();
            this.prom_label = in.readString();
            this.prom_type = in.readString();
            this.goods = in.createTypedArrayList(Goods.CREATOR);
            this.hint = in.readString();
            this.title_label = in.readString();
            this.prom_title = in.readString();
            this.prom_reduce = in.readString();
        }

        public static final Parcelable.Creator<Promotion> CREATOR = new Parcelable.Creator<Promotion>() {
            @Override
            public Promotion createFromParcel(Parcel source) {
                return new Promotion(source);
            }

            @Override
            public Promotion[] newArray(int size) {
                return new Promotion[size];
            }
        };
    }

    public GoodsDeatilEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.introduction);
        dest.writeString(this.limit_min_buy);
        dest.writeParcelable(this.detail, flags);
        dest.writeString(this.free_shipping);
        dest.writeString(this.shipping_fee);
        dest.writeString(this.area);
        dest.writeString(this.market_price);
        dest.writeString(this.price);
        dest.writeString(this.max_price);
        dest.writeString(this.stock);
        dest.writeString(this.thumb);
        dest.writeString(this.status);
        dest.writeString(this.is_fav);
        dest.writeStringList(this.pics);
        dest.writeString(this.has_option);
        dest.writeString(this.return_7);
        dest.writeString(this.send_time);
        dest.writeString(this.quality_guarantee);
        dest.writeTypedList(this.services);
        dest.writeString(this.credit);
        dest.writeString(this.video);
        dest.writeString(this.type);
        dest.writeString(this.type_tag_pic);
        dest.writeString(this.get_gold_second);
        dest.writeString(this.is_preferential);
        dest.writeString(this.member_cart_count);
        dest.writeString(this.is_new);
        dest.writeString(this.is_explosion);
        dest.writeString(this.is_hot);
        dest.writeString(this.is_recommend);
        dest.writeTypedList(this.sku);
        dest.writeTypedList(this.specs);
        dest.writeParcelable(this.goods_data, flags);
        dest.writeTypedList(this.full_cut);
        dest.writeTypedList(this.full_discount);
        dest.writeTypedList(this.buy_gift);
        dest.writeString(this.cate_1_name);
        dest.writeString(this.cate_2_name);
        dest.writeTypedList(this.voucher);
        dest.writeParcelable(this.store_info, flags);
        dest.writeTypedList(this.combo);
        dest.writeTypedList(this.attrs);
        dest.writeTypedList(this.comments);
        dest.writeParcelable(this.tt_act, flags);
        dest.writeParcelable(this.common_activity, flags);
        dest.writeParcelable(this.activity, flags);
        dest.writeParcelable(this.user_info, flags);
        dest.writeString(this.self_buy_earn);
        dest.writeString(this.share_buy_earn);
        dest.writeString(this.share_buy_earn_btn);
        dest.writeString(this.self_buy_earn_btn);
        dest.writeParcelable(this.plus_door, flags);
    }

    protected GoodsDeatilEntity(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.introduction = in.readString();
        this.limit_min_buy = in.readString();
        this.detail = in.readParcelable(Detail.class.getClassLoader());
        this.free_shipping = in.readString();
        this.shipping_fee = in.readString();
        this.area = in.readString();
        this.market_price = in.readString();
        this.price = in.readString();
        this.max_price = in.readString();
        this.stock = in.readString();
        this.thumb = in.readString();
        this.status = in.readString();
        this.is_fav = in.readString();
        this.pics = in.createStringArrayList();
        this.has_option = in.readString();
        this.return_7 = in.readString();
        this.send_time = in.readString();
        this.quality_guarantee = in.readString();
        this.services = in.createTypedArrayList(SimpTitle.CREATOR);
        this.credit = in.readString();
        this.video = in.readString();
        this.type = in.readString();
        this.type_tag_pic = in.readString();
        this.get_gold_second = in.readString();
        this.is_preferential = in.readString();
        this.member_cart_count = in.readString();
        this.is_new = in.readString();
        this.is_explosion = in.readString();
        this.is_hot = in.readString();
        this.is_recommend = in.readString();
        this.sku = in.createTypedArrayList(Sku.CREATOR);
        this.specs = in.createTypedArrayList(Specs.CREATOR);
        this.goods_data = in.readParcelable(GoodsData.class.getClassLoader());
        this.full_cut = in.createTypedArrayList(ActivityDetail.CREATOR);
        this.full_discount = in.createTypedArrayList(ActivityDetail.CREATOR);
        this.buy_gift = in.createTypedArrayList(ActivityDetail.CREATOR);
        this.cate_1_name = in.readString();
        this.cate_2_name = in.readString();
        this.voucher = in.createTypedArrayList(Voucher.CREATOR);
        this.store_info = in.readParcelable(StoreInfo.class.getClassLoader());
        this.combo = in.createTypedArrayList(Combo.CREATOR);
        this.attrs = in.createTypedArrayList(Attrs.CREATOR);
        this.comments = in.createTypedArrayList(Comments.CREATOR);
        this.tt_act = in.readParcelable(TTAct.class.getClassLoader());
        this.common_activity = in.readParcelable(SpecailAct.class.getClassLoader());
        this.activity = in.readParcelable(Act.class.getClassLoader());
        this.user_info = in.readParcelable(UserInfo.class.getClassLoader());
        this.self_buy_earn = in.readString();
        this.share_buy_earn = in.readString();
        this.share_buy_earn_btn = in.readString();
        this.self_buy_earn_btn = in.readString();
        this.plus_door = in.readParcelable(PlusDoor.class.getClassLoader());
    }

    public static final Creator<GoodsDeatilEntity> CREATOR = new Creator<GoodsDeatilEntity>() {
        @Override
        public GoodsDeatilEntity createFromParcel(Parcel source) {
            return new GoodsDeatilEntity(source);
        }

        @Override
        public GoodsDeatilEntity[] newArray(int size) {
            return new GoodsDeatilEntity[size];
        }
    };
}
