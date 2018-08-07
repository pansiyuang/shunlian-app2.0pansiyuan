package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/24.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuperProductEntity {
    public List<SuperProduct> data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SuperProduct {
        public String type;
        public List<BannerEntity> data;
        public String thumb;
        public String title;
        public String desc;
        public String price;
        public String earned;
        public String stock;
        public int is_new;
        public int is_explosion;
        public int is_hot;
        public int is_recommend;
        public Url url;
        public Share share;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BannerEntity {
        public String thumb;
        public Url url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Url {
        public String type;
        public String item_id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Share {
        public String title;
        public String content;
        public String pic;
        public String nick_name;
        public String portrait;
        public String share_url;
    }
}
