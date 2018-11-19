package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BubbleEntity{
    public List<Content> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        public String avatar;
        public String text;
        public Link url;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Link {
            public String type;
            public String item_id;
        }
    }

}
