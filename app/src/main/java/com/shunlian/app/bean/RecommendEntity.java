package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/8/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendEntity {
    public List<Goods> hot_goods;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods {
        public String id;
        public String title;
        public String thumb;
        public String price;
        public String self_buy_earn;
        public String share_buy_earn;
    }
}
