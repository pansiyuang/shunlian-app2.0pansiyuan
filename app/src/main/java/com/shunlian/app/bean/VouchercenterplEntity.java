package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VouchercenterplEntity {
    public List<MData> seller_voucher;
    public String count;
    public String page;
    public String page_size;
    public String total_page;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MData {
        public String id;
        public String use_condition;
        public String denomination;
        public String goods_scope;
        public String count;
        public String give_count;
        public String limit_count;
        public String start_time;
        public String end_time;
        public String store_id;
        public String title;
        public String already_get;
        public String new_or_old;
        public String if_get;
        public String valid_date;
        public String thumb;
        public String lazy_id;
        public String jump_type;
    }
}
