package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatCommentEntity {
    public List<ReleaseCommentEntity> order_goods;
    public String order_sn;
}
