package com.shunlian.app.eventbus_bean;

/**
 * Created by Administrator on 2018/4/3.
 */

public class ArticleEvent {
    public String articleId;
    public String isLike;

    public ArticleEvent(String id, String status) {
        this.articleId = id;
        this.isLike = status;
    }
}
