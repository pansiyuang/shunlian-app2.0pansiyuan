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
}
