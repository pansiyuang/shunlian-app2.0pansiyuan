package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CateEntity {
    public List<Cate> cates;
    public String hint;
    public String sub_amount;
    public String sub_count;
    public int cartTotal;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cate {
        public String cate_id;
        public String cate_name;
    }
}
