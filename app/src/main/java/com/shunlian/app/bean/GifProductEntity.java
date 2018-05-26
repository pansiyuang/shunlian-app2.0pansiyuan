package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GifProductEntity {
    public List<Product> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        public String product_id;
        public String title;
        public String thumb;
        public String price;
        public String market_price;
        public int sell_out;
    }
}
