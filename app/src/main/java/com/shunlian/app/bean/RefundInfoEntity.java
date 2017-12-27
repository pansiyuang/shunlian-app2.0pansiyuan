package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundInfoEntity {
    public String price;
    public String qty;
    public String shipping_fee;
    public List<RefundChoice> refund_choice;
    public List<Reason> reason;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class RefundChoice {
        public String type;
        public String hint;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Reason {
        public String reason_id;
        public String reason_info;
    }
}
