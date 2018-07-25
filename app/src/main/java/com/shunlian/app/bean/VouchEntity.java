package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VouchEntity {
    public String id;
    public String store_name;
    public String store_label;
    public String denomination;
    public String use_condition;
    public String limit_data;
    public String is_store;
    public String is_get;
    public String desc;
    public List<Goods> goods_list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Goods {
        public String goods_id;
        public String thumb;
        public String title;
        public String price;
        public String comments_num;
        public String good_comments_num;
        public String location;
        public String free_shpping;
        public String is_new;
        public String is_hot;
        public String is_pop;
        public String voucher;
        public String discount;
        public String git;

    }
}
