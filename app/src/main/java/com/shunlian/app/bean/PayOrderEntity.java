package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/1/2.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayOrderEntity {

    public String alipay;
    public String pay_sn;
    public String order_id;
    public String unionpay;
    public String paytype;

    public XiaoXiaopay xiaoxiaopay;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class XiaoXiaopay{
        public String signValue;
        public String nonceStr;
    }
}
