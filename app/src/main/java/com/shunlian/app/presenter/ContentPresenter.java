package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ExperienceEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IContentView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ContentPresenter extends BasePresenter<IContentView> {
    public static final int PAGE_SIZE = 20;

    public ContentPresenter(Context context, IContentView iView) {
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

    public void initPage() {
        currentPage = 1;
    }

    public void getFavoriteArticles(boolean isShowLoading) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<ArticleEntity>> baseEntityCall = getAddCookieApiService().favoriteArticles(getRequestBody(map));
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ArticleEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ArticleEntity> entity) {
                super.onSuccess(entity);
                ArticleEntity articleEntity = entity.data;
                isLoading = false;
                currentPage = Integer.parseInt(entity.data.page);
                allPage = Integer.parseInt(entity.data.total_page);
                iView.getFavoriteArticles(articleEntity.list, currentPage, allPage);
                if (currentPage == 1) {
                    iView.refreshFinish();
                }
                currentPage++;
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
            }
        });
    }

    public void praiseExperience(String experienceId, String status) {
        Map<String, String> map = new HashMap<>();
        map.put("experience_id", experienceId);
        map.put("status", status);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().praiseExperience(getRequestBody(map));
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.praiseExperience();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }


    public void removeArticles(final String articleId, final boolean isDelItem) {
        Map<String, String> map = new HashMap<>();
        map.put("id", articleId);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().unFavoriteArticle(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                if (isDelItem) {
                    iView.removeItem(articleId);
                } else {
                    iView.removeSuccess();
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getFavoriteArticles(false);
            }
        }
    }
}
