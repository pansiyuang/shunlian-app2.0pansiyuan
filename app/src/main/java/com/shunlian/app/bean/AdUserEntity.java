package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdUserEntity implements Serializable{
    public boolean  isNew;//是否是新人 true：是 false:否
    public List<AD> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AD implements Serializable{
        public String id;
        public String ad_img;
        public String ad_sn;
        public Link link;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Link implements Serializable{
            public String type;
            public String item_id;
        }
    }
}
