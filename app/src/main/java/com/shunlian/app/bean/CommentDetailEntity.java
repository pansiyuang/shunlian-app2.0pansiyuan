package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDetailEntity {
    public Pager page;
    public FindCommentListEntity.ItemComment info;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public String page;
        public String allPage;
        public String total;
        public String pageSize;
    }
}
