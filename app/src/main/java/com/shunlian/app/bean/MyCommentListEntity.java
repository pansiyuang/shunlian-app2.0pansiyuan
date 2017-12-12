package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyCommentListEntity {

    public String total;
    public String page;
    public String page_size;
    public String max_page;

    public List<CommentListEntity.Data> list;
}
