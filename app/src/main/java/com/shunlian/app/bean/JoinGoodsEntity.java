package com.shunlian.app.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class JoinGoodsEntity {
    public List<GoodsDeatilEntity.Goods> join_goods;
    public String prom_id;
    public String title;
    public String label;
}
