package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpSearchEntity {
    public List<Content> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        public String id;
        public String title;
        public String content;
//        public List<String> title_keywords;
//        public List<String> content_keywords;

    }
}
