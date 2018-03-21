package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISearchArticleView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/19.
 */

public class SearchArticlePresenter extends BasePresenter<ISearchArticleView> {
    public static final int PAGE_SIZE = 20;
    private String currentKeyWord;

    public SearchArticlePresenter(Context context, ISearchArticleView iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {

    }

    public void getSearchArticleList(boolean isShowLoading, String keyword) {
        currentKeyWord = keyword;
        Map<String, String> map = new HashMap<>();
        map.put("keyword", keyword);
        if (isShowLoading) {
            currentPage = 1;
        }
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<ArticleEntity>> baseEntityCall = getApiService().searchArticle(getRequestBody(map));
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ArticleEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ArticleEntity> entity) {
                super.onSuccess(entity);
                ArticleEntity articleEntity = entity.data;
                isLoading = false;
                iView.getSearchArticleList(articleEntity.article_list, Integer.valueOf(articleEntity.page), Integer.valueOf(articleEntity.total_page));
                currentPage = Integer.parseInt(entity.data.page);
                allPage = Integer.parseInt(entity.data.total_page);
                currentPage++;
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getSearchArticleList(false, currentKeyWord);
            }
        }
    }
}
