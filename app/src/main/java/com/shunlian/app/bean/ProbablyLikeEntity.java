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

}
