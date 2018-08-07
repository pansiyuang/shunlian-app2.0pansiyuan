package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmOrderEntity {

    public List<Enabled> enabled;
    public List<NoDelivery> no_delivery;//不在配送范围内
    public List<GoodsDeatilEntity.Goods> disabled;
    public String user_stage_voucher;//1表示平台最优，应该默认选择平台优惠券
    public List<Voucher> stage_voucher;//平台优惠券
    public String disabled_ids;
    public String total_shipping_fee;
    public String total_amount;//商品总额
    public String total_count;// 商品总数
    public String total_reduce;//总减免
    public String pay_amount;//最终的价格（需要客户端减去选取的优惠券）
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
        public String prom_lock;//促销是否强制勾选  1强制
        public List<PromotionInfo> promotion_info;
        public List<Voucher> voucher;
        public List<GoodsDeatilEntity.Goods> goods;
        public String shippingFee;
        public String promotion_total_hint;
        public String sub_total;
        public String sub_count;
        public String remark;
        public String store_discount_price;//店铺折后价总价
        public int selectVoucherId;//选择优惠券id，默认是0
        public int selectPromotionId = -1;//选择促销活动id，默认不选择是-1

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
        public List<Goods> goods;
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
    public static class Address implements Serializable{

        public String id;
        public String realname;
        public String mobile;
        public String province;
        public String city;
        public String area;
        public String address;
        public String district_addr;
        public String isdefault;
        public List<String> district_ids;
        public String detail_address;
        public String district_id;
        public boolean isSelect;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Voucher{
        public String title;
        public String voucher_id;
        public String denomination;
        public String voucher_hint;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods{
        public String title;
        public String thumb;
        public String price;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NoDelivery{
        public String areas;
        public String hint;
        public String title;
        public String thumb;
        public String price;
        public String qty;
        public String sku_id;
        public String status;
        public String sku;
        public String goods_id;
    }
}
