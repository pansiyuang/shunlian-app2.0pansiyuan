package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MergeOrderEntity {
    public int count;
    public String prom_title;
    public long count_down;
    public String prom_text;
    public String total;
    public int page;
    public int page_size;
    public int total_page;
    public List<GoodsDeatilEntity.Goods> list;

}
