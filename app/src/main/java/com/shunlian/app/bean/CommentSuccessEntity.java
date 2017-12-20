package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentSuccessEntity {

    public List<Comment> comment;
    public List<Comment> append;



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comment{
        public String goods_id;
        public String title;
        public String thumb;
        public String price;
        public String comment_id;
        public String order_sn;
    }
}
