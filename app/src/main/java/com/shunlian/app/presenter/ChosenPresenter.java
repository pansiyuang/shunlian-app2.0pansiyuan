package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
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
    public static final int PAGE_SIZE = 10;
    public static final int MODE_ARTICLE = 10001;
    public static final int MODE_TAG_DETAIL = 10002;
    private int currentMode;
    private String currentTagId;
    private ShareInfoParam shareInfoParam;

    public ChosenPresenter(Context context, IChosenView iView) {
        super(context, iView);
        shareInfoParam = new ShareInfoParam();
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

    public void getArticleList(boolean isShowLoading) {
        currentMode = MODE_ARTICLE;
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
                if (currentPage == 1) {
                    iView.refreshFinish();
                }
                //分享专用
                GoodsDeatilEntity.UserInfo user_info = articleEntity.user_info;
                if (shareInfoParam == null)
                    shareInfoParam = new ShareInfoParam();
                if (user_info != null) {
                    shareInfoParam.userAvatar = user_info.avatar;
                    shareInfoParam.userName = user_info.nickname;
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

    public void getTagDetail(boolean isShowLoading, String tagId) {
        currentMode = MODE_TAG_DETAIL;
        currentTagId = tagId;
        Map<String, String> map = new HashMap<>();
        map.put("tag_id", tagId);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<ArticleEntity>> baseEntityCall = getApiService().tagDetail(map);
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ArticleEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ArticleEntity> entity) {
                super.onSuccess(entity);
                ArticleEntity articleEntity = entity.data;
                isLoading = false;
                iView.getNiceList(articleEntity, Integer.valueOf(articleEntity.page), Integer.valueOf(articleEntity.total_page));
                currentPage = Integer.parseInt(entity.data.page);
                allPage = Integer.parseInt(entity.data.total_page);
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

    public void articleLike(final String articleId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", articleId);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().userLike(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
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
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().userUnLike(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
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

    public void getOthersTopicList(String index) {
        Map<String, String> map = new HashMap<>();
        map.put("index", index);
        sortAndMD5(map);
        Call<BaseEntity<ArticleEntity>> baseEntityCall = getApiService().othersTopics(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ArticleEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ArticleEntity> entity) {
                super.onSuccess(entity);
                ArticleEntity articleEntity = entity.data;
                iView.getOtherTopics(articleEntity.topic_list);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                if (currentMode == MODE_ARTICLE) {
                    getArticleList(false);
                } else {
                    getTagDetail(false, currentTagId);
                }
            }
        }
    }

    public ShareInfoParam getShareInfoParam() {
        return shareInfoParam;
    }
}
