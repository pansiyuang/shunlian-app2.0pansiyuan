package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/12/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundDetailEntity {
    public RefundDetail refund_detail;

    @Override
    public String toString() {
        return "RefundDetailEntity{" +
                "refund_detail=" + refund_detail +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefundDetail {
        public String refund_id;
        public String store_name;
        public String refund_amount;
        public String goods_num;
        public String refund_sn;
        public String buyer_message;
        public String add_time;
        public String thumb;
        public String title;
        public String sku_desc;
        public String price;
        public String qty;
        public String html_description;

        @Override
        public String toString() {
            return "RefundDetail{" +
                    "refund_id='" + refund_id + '\'' +
                    ", store_name='" + store_name + '\'' +
                    ", refund_amount='" + refund_amount + '\'' +
                    ", goods_num='" + goods_num + '\'' +
                    ", refund_sn='" + refund_sn + '\'' +
                    ", buyer_message='" + buyer_message + '\'' +
                    ", add_time='" + add_time + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", title='" + title + '\'' +
                    ", sku_desc='" + sku_desc + '\'' +
                    ", price='" + price + '\'' +
                    ", qty='" + qty + '\'' +
                    '}';
        }
    }
}
