package com.shunlian.app.bean;




/**
 * Created by Administrator on 2017/12/5.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PosterEntity {
    public String url;
    public String card_url;
    public String title;
    public String desc;

}
