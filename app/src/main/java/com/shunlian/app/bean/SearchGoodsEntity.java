package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchGoodsEntity {
    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public List<GoodsDeatilEntity.Goods> goods_list;
    public RefStore ref_store;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefStore {
        public String store_id;
        public String store_name;
        public String store_logo;
        public String praise_rate;
        public String head_banner;
        public List<Comment> pj;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comment {
        public String name;
        public String score;
    }
}
