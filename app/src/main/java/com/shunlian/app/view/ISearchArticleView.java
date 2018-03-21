package com.shunlian.app.view;

import com.shunlian.app.bean.ArticleEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public interface ISearchArticleView extends IView {

    void getSearchArticleList(List<ArticleEntity.Article> articleList, int page, int totalPage);
}
