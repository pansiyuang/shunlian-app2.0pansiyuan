package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponListEntity {


    public SaleDetailEntity.Page pager;

    public List<VoucherList> voucher_list;

    public NumInfo num_info;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VoucherList{
        public String id;
        public String title;
        public String store_id;
        public String voucher_type;
        public String voucher_type_desc;
        public String stauts;
        public String start_time;
        public String end_time;
        public String valid_time;
        public String expire_after;
        public String use_condition;
        public String denomination;
        public String where_used;
        public String goods_scope;
        public String memo;
        public String expired;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NumInfo{

        public String not_used;
        public String used;
        public String expired;
        public String help_id;
    }
}
