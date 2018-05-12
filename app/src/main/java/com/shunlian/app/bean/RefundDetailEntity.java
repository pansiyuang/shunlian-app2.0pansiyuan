package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundDetailEntity implements Serializable{
    public RefundDetail refund_detail;

    @Override
    public String toString() {
        return "RefundDetailEntity{" +
                "refund_detail=" + refund_detail +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefundDetail implements Serializable {
        public String order_id;
        public String refund_id;
        public String refund_type;
        public String store_name;
        public String store_id;
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
        public String express;
        public String s_express;
        public String rest_second;
        public List<Msg> msg_list;
        public List<Opt> opt_list;
        public Gift gift;
        public ReturnAddress return_address;
        public MemberAddress member_address;
        public Edit edit;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Edit implements Serializable{
            public String og_Id;
            public String store_name;
            public String refund_type;
            public String edit_apply_type;
            public Gift gift;
            public String thumb;
            public String title;
            public String sku_desc;
            public String price;
            public String qty;
            public String refund_amount;
            public String goods_num;
            public String refund_remark_seller;
            public String buyer_message;
            public String reason_id;
            public List<Reason> user_status;
            public UserStatus current_user_status;
            public String time_desc;
            public String rest_second;
            public String refund_remark_admin;
            public String is_last;
            public String return_price;
            public String shipping_fee;
            public String real_amount;
            public List<Reason> reason;
            public List<RefundChoice> refund_choice;
            public List<String> member_evidence_seller;
            public List<String> member_evidence_admin;
            public String serviceType;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class RefundChoice implements Serializable{
                public String icon;
                public String type;
                public String hint;
                public String tip;

            }
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class UserStatus implements Serializable{
                public String id;
                public String desc;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Reason implements Serializable{
                public String reason_id;
                public String reason_info;

            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ReturnAddress implements Serializable{
            public String id;
            public String name;
            public String phone;
            public String address;

            @Override
            public String toString() {
                return "ReturnAddress{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", phone='" + phone + '\'' +
                        ", address='" + address + '\'' +
                        '}';
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class MemberAddress implements Serializable{
            public String id;
            public String name;
            public String phone;
            public String address;

            @Override
            public String toString() {
                return "MemberAddress{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", phone='" + phone + '\'' +
                        ", address='" + address + '\'' +
                        '}';
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Gift implements Serializable{
            public String goods_id;
            public String title;
            public String thumb;

            @Override
            public String toString() {
                return "Gift{" +
                        "goods_id='" + goods_id + '\'' +
                        ", title='" + title + '\'' +
                        ", thumb='" + thumb + '\'' +
                        '}';
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Msg implements Serializable{
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
        public static class Opt implements Serializable{
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
