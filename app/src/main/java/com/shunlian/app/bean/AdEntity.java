package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdEntity implements Serializable{
    public String show;
    public AD list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AD implements Serializable{
        public String id;
        public String ad_img;
        public String button;
        public Link link;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Link implements Serializable{
            public String type;
            public String item_id;
        }
    }
}
