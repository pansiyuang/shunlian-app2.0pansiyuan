package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/12/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonEntity {
    public String fid;//商品收藏id
}
