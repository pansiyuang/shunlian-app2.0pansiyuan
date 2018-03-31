package com.shunlian.app.view;

import com.shunlian.app.bean.ArticleEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public interface IContentView extends IView {
    void getFavoriteArticles(List<ArticleEntity.Article> articleList, int page, int totalPage);

    void refreshFinish();

    void praiseExperience();

    void removeSuccess();

    void removeItem(String articleId);
}
