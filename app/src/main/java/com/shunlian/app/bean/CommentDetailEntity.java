package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDetailEntity {
    public String article_id;
    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public List<FindCommentListEntity.ItemComment> reply_list;
}
