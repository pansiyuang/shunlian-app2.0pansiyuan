package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProbabyLikeGoodsEntity {
    public List<Match> matched;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Match {
        public String goods_id;
        public String thumb;
        public String hint;
        public List<Goods> goods;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods {
        public String goods_id;
        public String title;
        public String thumb;
        public String price;
        public String market_price;
        public int index;
        public boolean isParent;
    }
}
