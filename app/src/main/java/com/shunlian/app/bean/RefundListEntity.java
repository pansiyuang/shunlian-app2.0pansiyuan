package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundListEntity {

    public String count;
    public String total_page;
    public String page;
    public String page_size;

    public List<RefundList> refund_list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefundList {

        public String refund_id;
        public String store_name;
        public String refund_amount;
        public String goods_num;
        public String thumb;
        public String title;
        public String sku_desc;
        public String price;
        public String qty;
    }
}
