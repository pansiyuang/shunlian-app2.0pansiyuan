package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackArtEntity {
    public String mobile;
    public String content;
    public String app_version;
    public String machine_info;
    public String platform;
    public String goods_id ;
    public List<String> image;
    public String type;

}
