package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.ArticleDetailEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IArticleDetailView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/19.
 */

public class ArticleDetailPresenter extends BasePresenter<IArticleDetailView> {

    public ArticleDetailPresenter(Context context, IArticleDetailView iView) {
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

    public void getArticleDetail(String articleId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", articleId);
        sortAndMD5(map);
        Call<BaseEntity<ArticleDetailEntity>> baseEntityCall = getApiService().niceDetail(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ArticleDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ArticleDetailEntity> entity) {
                super.onSuccess(entity);
                ArticleDetailEntity articleDetailEntity = entity.data;
                iView.getArticleDetail(articleDetailEntity);
            }
        });
    }

    public void favoriteArticle(String articleId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", articleId);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().favoriteArticle(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.favoriteSuccess();
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void unFavoriteArticle(String articleId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", articleId);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().unFavoriteArticle(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.unFavoriteSuccess();
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }
}
