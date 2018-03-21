package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/3/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDetailEntity {
    public String had_favorites;
    public String h5_detail_url;
    public String share_url;
}
