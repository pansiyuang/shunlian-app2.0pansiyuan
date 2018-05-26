package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/5/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLUSConfirmEntity {

    public ProductBean product;
    public AddressBean address;
    public String total_amount;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductBean {
        public String product_id;
        public String stock;
        public String price;
        public String title;
        public String thumb;
        public String qty;
        public String sku_id;
        public String sku;
        public String shipping_fee;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressBean {
        public String id;
        public String district_id;
        public String realname;
        public String mobile;
        public String detail_address;
    }
}
