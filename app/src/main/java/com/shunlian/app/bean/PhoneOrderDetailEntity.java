package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneOrderDetailEntity implements Serializable{
    public String order_sn;
    public String card_number;
    public String card_addr;
    public String face_price;
    public String payment_money;
    public String status_name;
    public String desc_name;
    public String store_name;
    public String image;
    public List<String> trade;
}
