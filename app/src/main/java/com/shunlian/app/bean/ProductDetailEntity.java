package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetailEntity {
    public String member_code;
    public int is_plus;
    public String product_id;
    public String title;
    public String thumb;
    public String price;
    public String stock;
    public String market_price;
    public List<String> pics;
    public String has_option;
    public Detail detail;
    public List<GoodsDeatilEntity.Specs> specs;
    public List<GoodsDeatilEntity.Sku> sku;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        public String text;
        public List<String> pics;
    }
}
