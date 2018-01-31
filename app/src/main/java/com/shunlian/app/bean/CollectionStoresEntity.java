package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectionStoresEntity {

    public List<Cates> cates;
    @JsonProperty(value = "shop")//关键字重名
    public List<Store> stores;
    public String total_page;
    public String count;
    public String page;
    public String page_size;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cates {
        public String id;
        public String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Store {
        public String id;
        public String store_id;
        public String store_name;
        public String store_avatar;
        public String grade_id;
        public String nice_rate;
        public String star;
        public String new_count;
        public String status;
        public List<NewGood> new_goods;
        public boolean isSelect;//是否选中

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NewGood {
            public String id;
            public String thumb;
            public String price;
        }
    }
}
