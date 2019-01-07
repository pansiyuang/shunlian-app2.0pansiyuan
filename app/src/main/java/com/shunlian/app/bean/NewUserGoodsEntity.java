package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewUserGoodsEntity {
    public String unpaid;
    public List<Goods> list;
    public String total_page;
    public String count;
    public String page;
    public int page_size;
    public boolean isNew;//是否是新人 true：是 false:否  （type=2不返回该字段）
    public int   cartTotal;//购物车数量（type=2不返回该字段）
    public int   show;//是否已领取 1：已领取过
    public  Goods recommend;//推荐的商品
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cates {
        public String id;
        public String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods {
        public String   id;//商品id
        public String   title;//"简丹 淡水珍珠项链近圆女款白色 相思雨 7-8mm 60cm",//标题
        public String   thumb;//"http://img01.sldlcdn.com/attachment/images/2015/11/x6a4Um30WmY6pmCcwGKgSw0IEwQw9S.jpg?_300x300.jpg",//主图
        public String   price; //价格 新人价格为0
        public String   market_price;//市场价
        public String   has_option;//是否开启规格1是0否
        public String   status;//状态：1上架0下架
        public String  goods_id;//购物车列表下的商品id
        public String   sku;//"黑色+3XL",
        public String   qty;//数量
        public int   is_add_cart;//是不是加入购物车 1:加入 0 未加入
        public boolean is_recommend= false;
    }
}
