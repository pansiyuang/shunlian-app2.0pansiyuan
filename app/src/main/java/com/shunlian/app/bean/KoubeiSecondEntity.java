package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KoubeiSecondEntity {
    public String cate_id;
    public String cate_name;
    public String cate_rate;
    public List<Content> commend_list;
    public List<Content> hot_list;
    public List<HotsaleHomeEntity.AD> like_list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        public String id;
        public String text;
        public String title;
        public String thumb;
        public String star_rate;
        public String share_url;
        public String praise;
        public String price;
        public String praise_flag;
        public String share_buy_earn;
        public String self_buy_earn;
        public List<Comment> comments;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Comment {
            public String content;
            public String avatar;
            public String nickname;
        }
    }

}
