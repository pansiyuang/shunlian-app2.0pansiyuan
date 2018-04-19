package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpClassEntity {
    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public String cate;
    public List<Article> list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Article {
        public String id;
        public String image;
        public String title;
        public String author;
        public String desc;
        public String h5_link;
    }

}
