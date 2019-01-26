package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProbablyLikeEntity {

    public String total_amount;
    public String order_id;
    public Address address;

    public List<MayBuyList> may_be_buy_list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MayBuyList{
        public String id;
        public String title;
        public String price;
        public String thumb;
        public String market_price;
        public String self_buy_earn;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address{
        public String district_id;
        public String realname;
        public String mobile;
        public String province;
        public String city;
        public String area;
        public String address;
        public String district_addr;
        public String district_ids;
    }

}
