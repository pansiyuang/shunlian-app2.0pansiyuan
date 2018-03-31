package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleEntity {
    public List<Tag> tag_list;
    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public List<Article> article_list;
    public List<Article> list;
    public List<Topic> topic_list;
    public Tag tag_obj;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Article {
        public String id;
        public String title;
        public String full_title;
        public String thumb;
        public String thumb_type;
        public List<Tag> tags;
        public String share_url;
        public String likes;
        public String favorites;
        public String status;
        public String comments;
        public String forwards;
        public String had_like;
        public List<Topic> topic_list;
        public String[] title_keywords;
        public String[] full_title_keywords;
        public String pub_time;
        public String pub_by;
        public boolean isSelect;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tag {
        public String id;
        public String name;
        public String description;
        public String bg_img;
        public String head_img;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Topic {
        public String id;
        public String subject;
        public String thumb;
        public String join_in_num;
    }
}
