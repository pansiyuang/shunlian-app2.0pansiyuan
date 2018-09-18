package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShareEntity implements Serializable{
    public Share share;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Share {
        public String share_url;
        public String title;
        public String content;
        public String pic;
        public String url;//专题页图文分享图片
    }
}
