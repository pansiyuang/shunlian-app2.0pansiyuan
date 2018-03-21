package com.shunlian.app.view;

import com.shunlian.app.bean.ArticleEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface IChosenView extends IView {
    void getNiceList(ArticleEntity articleEntity, int currentPage, int totalPage);

    void likeArticle(String articleId);

    void unLikeArticle(String articleId);

    void getOtherTopics(List<ArticleEntity.Topic> topic_list);
}
