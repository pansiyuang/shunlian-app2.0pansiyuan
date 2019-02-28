package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotsaleHomeEntity {
    public List<Suspension> hot_list;
    public List<AD> like_list;
    public List<Cate> cate_list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cate {
        public String cate_id;
        public String cate_name;
        public List<HotsaleEntity.Suspension> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Suspension {
        public String cate_id;
        public String cate_name;
        public String comments;
        public List<Link> list;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Link{
            public String id;
            public String title;
            public String thumb;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AD {
        public String id;
        public String title;
        public String thumb;
        public String price;
        public String market_price;
        public String self_buy_earn;
    }
}
