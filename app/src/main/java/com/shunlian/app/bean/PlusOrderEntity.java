package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlusOrderEntity {
    public Pager pager;
    public String current_type;
    public List<PlusOrder> list;

    @JsonIgnoreProperties(ignoreUnknown = true)

    public static class Pager {
        public int page;
        public int page_size;
        public int count;
        public int total_page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlusOrder {
        public String product_order_id;
        public int status;
        public String status_desc;
        public String product_id;
        public String title;
        public String thumb;
        public String price;
        public long qty;
        public String sku_desc;
        public String shipping_fee;
        public long total;
    }
}
