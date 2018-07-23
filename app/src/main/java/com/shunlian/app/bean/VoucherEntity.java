package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/7/20.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherEntity {
    public String id;
    public String store_name;
    public String store_label;
    public int denomination;
    public String use_condition;
    public String limit_data;
}
