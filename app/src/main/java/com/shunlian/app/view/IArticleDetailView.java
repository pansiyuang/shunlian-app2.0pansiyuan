package com.shunlian.app.view;

import com.shunlian.app.bean.ArticleDetailEntity;

/**
 * Created by Administrator on 2018/3/19.
 */

public interface IArticleDetailView extends IView {
    void getArticleDetail(ArticleDetailEntity detailEntity);

    void favoriteSuccess();

    void unFavoriteSuccess();
}
