package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IChosenView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/14.
 */

public class ChosenPresenter extends BasePresenter<IChosenView> {
    public static final int PAGE_SIZE = 20;

    public ChosenPresenter(Context context, IChosenView iView) {
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

    public void getArticleList(boolean isShowLoading) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<ArticleEntity>> baseEntityCall = getApiService().niceList(map);
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ArticleEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ArticleEntity> entity) {
                super.onSuccess(entity);
                ArticleEntity articleEntity = entity.data;
                isLoading = false;
                iView.getNiceList(articleEntity, Integer.valueOf(articleEntity.page), Integer.valueOf(articleEntity.total_page));
                currentPage = Integer.parseInt(entity.data.page);
                allPage = Integer.parseInt(entity.data.total_page);
                currentPage++;
            }
        });
    }

    public void articleLike(final String articleId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", articleId);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().userLike(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.likeArticle(articleId);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast("点赞失败");
            }
        });
    }

    public void articleUnLike(final String articleId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", articleId);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().userUnLike(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.unLikeArticle(articleId);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast("取消点赞失败");
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getArticleList(false);
            }
        }
    }
}
