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

    @Override
    public String toString() {
        return "RefundListEntity{" +
                "count='" + count + '\'' +
                ", total_page='" + total_page + '\'' +
                ", page='" + page + '\'' +
                ", page_size='" + page_size + '\'' +
                ", refund_list=" + refund_list +
                '}';
    }

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
        public String type_icon;
        public String status_msg;

        @Override
        public String toString() {
            return "RefundList{" +
                    "refund_id='" + refund_id + '\'' +
                    ", store_name='" + store_name + '\'' +
                    ", refund_amount='" + refund_amount + '\'' +
                    ", goods_num='" + goods_num + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", title='" + title + '\'' +
                    ", sku_desc='" + sku_desc + '\'' +
                    ", price='" + price + '\'' +
                    ", qty='" + qty + '\'' +
                    ", type_icon='" + type_icon + '\'' +
                    ", status_msg='" + status_msg + '\'' +
                    '}';
        }
    }
}
