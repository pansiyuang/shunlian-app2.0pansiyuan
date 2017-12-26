package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderdetailEntity {
    public Notice notice_status;
    public String order_sn;
    public String remark;
    public List<Good> order_goods;
    public Address receipt_address;
    public String status;
    public String store_id;
    public String store_name;
    public String total_amount;
    public String goods_amount;
    public String shipping_fee;
    public String promotion;
    public String voucher_amount;
    public String paytype;
    public String create_time;
    public String pay_time;
    public String send_time;
    public String receive_time;
    public String is_append;
    public String is_edit_praise;
    public String is_postpone;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Notice {
        public String status_text;
        public String status_small;
        public String surplus_time;

        @Override
        public String toString() {
            return "Notice{" +
                    "status_text='" + status_text + '\'' +
                    ", status_small='" + status_small + '\'' +
                    ", surplus_time='" + surplus_time + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        public String realname;
        public String mobile;
        public String address;
        public String receipt_address;

        @Override
        public String toString() {
            return "Address{" +
                    "realname='" + realname + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", address='" + address + '\'' +
                    ", receipt_address='" + receipt_address + '\'' +
                    '}';
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Good {
        public String title;
        public String thumb;
        public String comment_id;
        public String sku_desc;
        public String qty;
        public String market_price;
        public String price;
        public String offered;
        public String goods_id;
        public List<Gift> gift;

        @Override
        public String toString() {
            return "Good{" +
                    "title='" + title + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", sku_desc='" + sku_desc + '\'' +
                    ", qty='" + qty + '\'' +
                    ", market_price='" + market_price + '\'' +
                    ", price='" + price + '\'' +
                    ", offered='" + offered + '\'' +
                    ", gift=" + gift +
                    '}';
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Gift {
            public String title;
            public String thumb;

            @Override
            public String toString() {
                return "Gift{" +
                        "title='" + title + '\'' +
                        ", thumb='" + thumb + '\'' +
                        '}';
            }
        }
    }
}
