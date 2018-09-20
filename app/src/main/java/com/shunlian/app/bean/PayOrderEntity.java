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
    public String paytype;
    public String zero_pay;
    public String pay_url;

    public XiaoXiaopay xiaoxiaopay;
    public Unionpay unionpay;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class XiaoXiaopay{
        public String signValue;
        public String nonceStr;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Unionpay{
        public String tn;
        public String query_url;
    }
}
