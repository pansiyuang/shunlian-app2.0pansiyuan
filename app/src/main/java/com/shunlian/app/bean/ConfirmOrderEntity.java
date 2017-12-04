package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmOrderEntity {

    public List<Enabled> enabled;
    public List<GoodsDeatilEntity.Goods> disabled;
    public String disabled_ids;
    public String total_shipping_fee;
    public Address address;

    @Override
    public String toString() {
        return "ConfirmOrderEntity{" +
                "enabled=" + enabled +
                ", disabled=" + disabled +
                ", disabled_ids='" + disabled_ids + '\'' +
                ", total_shipping_fee='" + total_shipping_fee + '\'' +
                ", address=" + address +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Enabled{
        public String store_id;
        public String store_name;
        public List<PromotionInfo> promotion_info;
        public List<Voucher> voucher;
        public List<GoodsDeatilEntity.Goods> goods;
        public String shippingFee;

        @Override
        public String toString() {
            return "Enabled{" +
                    "store_id='" + store_id + '\'' +
                    ", store_name='" + store_name + '\'' +
                    ", promotion_info=" + promotion_info +
                    ", voucher=" + voucher +
                    ", goods=" + goods +
                    ", shippingFee='" + shippingFee + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PromotionInfo{
        public String prom_id;
        public String prom_label;
        public String prom_title;
        public String prom_hint;
        public String prom_reduce;

        @Override
        public String toString() {
            return "PromotionInfo{" +
                    "prom_id='" + prom_id + '\'' +
                    ", prom_label='" + prom_label + '\'' +
                    ", prom_title='" + prom_title + '\'' +
                    ", prom_hint='" + prom_hint + '\'' +
                    ", prom_reduce='" + prom_reduce + '\'' +
                    '}';
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address{

        public String id;
        public String district_id;
        public String realname;
        public String mobile;
        public String address;

        @Override
        public String toString() {
            return "Address{" +
                    "id='" + id + '\'' +
                    ", district_id='" + district_id + '\'' +
                    ", realname='" + realname + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Voucher{
        public String title;
        public String voucher_id;
        public String denomination;
    }
}
