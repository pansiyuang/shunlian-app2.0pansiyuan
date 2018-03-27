package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/1/23.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CircleAddCommentEntity {
    public DiscoveryCommentListEntity.Mdata.Commentlist list;
}
