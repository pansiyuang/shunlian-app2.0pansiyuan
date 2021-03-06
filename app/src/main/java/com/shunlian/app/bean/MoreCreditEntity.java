package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhanghe on 2018/7/13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoreCreditEntity {

    public String card_address;
    public List<ListBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListBean{
        public String face_price;//面值
        public String sale_price;//销售价
        public String isBuy;////是否能购买 1：能   0：不能（界面上需要灰掉）
        public String message;
    }
}
