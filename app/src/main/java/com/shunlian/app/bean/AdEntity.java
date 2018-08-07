package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdEntity implements Serializable{
    public String show;
    public String is_tag;
    public String plus_role;
    public List<String> tag;
    public AD list;
    public String suspensionShow;
    public Suspension suspension;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Suspension implements Serializable {
        public String id;
        public String image;
        public Link link;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Link implements Serializable{
            public String type;
            public String item_id;
        }
    }

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
