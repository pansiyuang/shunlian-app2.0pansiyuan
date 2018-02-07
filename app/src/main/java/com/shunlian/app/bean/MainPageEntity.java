package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/2/1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainPageEntity {

    public List<Banner> carousel;

    public DaySpecial every_day_special;

    public NewGoods new_goods;

    public RecommendBrands recommend_brands;

    public RecommendBrands recommend_goods;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecommendBrands {
        public String page;
        public String total;
        public String count;
        public String page_size;
        public List<Data> data;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public String pic;
        public String type;
        public String item_id;
        public String title;
        public String price;
        public String market_price;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NewGoods {
        public String pic;
        public String type;
        public String item_id;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DaySpecial {
        public String item_id;
        public String count_down;
        public String pic;
        public String type;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Banner {
        public String pic;
        public String type;
        public String item_id;
    }
}
