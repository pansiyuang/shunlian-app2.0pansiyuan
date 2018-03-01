package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddGoodsEntity {
    public List<GoodsDeatilEntity.Goods> list;
    public String total_page;
    public String page;
    public String count;
    public String page_size;
}
