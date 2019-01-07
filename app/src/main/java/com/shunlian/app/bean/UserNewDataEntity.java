package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDataEntity {
    @JsonProperty(value = "data")//关键字重名
    public List<MData> datas;
    public String default_keyword;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String module;
        public GoodsDeatilEntity.Goods moreGoods;
        public String bg_color;
        public String text_color;
        public String bg_pic;
        public String pic;
        public String bg_pic_tmp;
        public String number;
        public String start_time;
        public String end_time;
        public Ttth ttth;
        public Ttth pptm;
        public Ttth asxp;
        public Ttth kbrx;
        public String title;
        public String content;
        public String price;
        public String pic_tmp;
        public String name;
        public String total;
        public String sort_type;
        public Url url;
        public Share share;
        @JsonProperty(value = "data")//关键字重名
        public List<MMData> datass;
        public List<Cate> cates;
        public String  market_price;
        public String  share_buy_earn;
        public String self_buy_earn;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Cate {
            public String id;
            public String name;
            public String level;
            public String sort_type;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Share {
            public String share_url;
            public String title;
            public String content;
            public String logo;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Ttth {
            public String title;
            public String t_color;
            public String content;
            public String c_color;
            public String count_down;
            public String thumb;
            public Url url;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class MMData {
            public String thumb;
            public String height;
            public String width;
            public String price;
            public String description;
            public String is_show;
            public Url url;
            public String start_time;
            public String end_time;
            public String title;
            public String url_name;
            public String thumb_tmp;
            public String  share_buy_earn;
            public String self_buy_earn;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Url {
        public String type;
        public String item_id;
        public String channe_id;
    }
}
