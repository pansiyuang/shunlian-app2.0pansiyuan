package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetailEntity {
    public String product_id;
    public String title;
    public String thumb;
    public String price;
    public String stock;
    public String market_price;
    public List<String> pics;
    public int has_option;
    public Detail detail;
    public List<Space> specs;
    public List<Sku> sku;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        public String text;
        public List<String> pics;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Space {
        public String id;
        public String name;
        public int show_type;
        public List<Value> values;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Value {
        public String id;
        public String name;
        public String memo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sku {
        public String id;
        public String name;
        public String price;
        public String market_price;
        public String weight;
        public String stock;
        public String specs;
        public String thumb;
    }
}
