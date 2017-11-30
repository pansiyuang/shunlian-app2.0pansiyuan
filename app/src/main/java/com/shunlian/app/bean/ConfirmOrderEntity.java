package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmOrderEntity {

    public List<Enabled> enabled;
    public List<Enabled> disabled;
    public String disabled_ids;
    public String total_shipping_fee;
    public Address address;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Enabled{
        public String store_id;
        public String store_name;
        public List<PromotionInfo> promotion_info;
        public List<GoodsDeatilEntity.Voucher> voucher;
        public List<GoodsDeatilEntity.Goods> goods;
        public String shippingFee;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PromotionInfo{
        public String prom_id;
        public String prom_label;
        public String prom_title;
        public String prom_hint;
        public String prom_reduce;
    }

    public static class Address{

        public String id;
        public String district_id;
        public String realname;
        public String mobile;
        public String address;
    }
}
