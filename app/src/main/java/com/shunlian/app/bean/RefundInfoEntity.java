package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundInfoEntity implements Serializable {
    public String og_Id;
    public String is_last;
    public String title;
    public String sku_desc;
    public String store_name;
    public String thumb;
    public String price;
    public String qty;
    public String return_price;
    public String shipping_fee;
    public List<RefundChoice> refund_choice;
    public List<Reason> reason;
    public String serviceType;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefundChoice implements Serializable {
        public String icon;
        public String type;
        public String hint;
        public String tip;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Reason implements Serializable {
        public String reason_id;
        public String reason_info;
    }
}
