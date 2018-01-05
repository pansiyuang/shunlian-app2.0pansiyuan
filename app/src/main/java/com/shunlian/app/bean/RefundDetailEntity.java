package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

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
        public String status_desc;
        public String time_desc;
        public String rest_second;
        public List<Msg> msg_list;
        public List<Opt> opt_list;
//        public List<Gift> gift;
//        public List<ReturnAddress> return_address;
//        public List<MemberAddress> member_address;


        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Msg {
            public String label;
            public String title;
            public String description;

            @Override
            public String toString() {
                return "Msg{" +
                        "label='" + label + '\'' +
                        ", title='" + title + '\'' +
                        ", description='" + description + '\'' +
                        '}';
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Opt {
            public String code;
            public String name;
            public String is_highlight;

            @Override
            public String toString() {
                return "Opt{" +
                        "code='" + code + '\'' +
                        ", name='" + name + '\'' +
                        ", is_highlight='" + is_highlight + '\'' +
                        '}';
            }
        }
    }
}
