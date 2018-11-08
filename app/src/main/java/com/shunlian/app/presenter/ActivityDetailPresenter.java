package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IActivityDetailView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/10/23.
 */

public class ActivityDetailPresenter extends BasePresenter<IActivityDetailView> {

    public static int PAGE_SIZE = 10;
    private String currentActId;

    public ActivityDetailPresenter(Context context, IActivityDetailView iView) {
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

    public void getActivityDetail(boolean isFirst, String activityId) {
        currentActId = activityId;
        Map<String, String> map = new HashMap<>();
        map.put("activity_id", activityId);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<HotBlogsEntity>> baseEntityCall = getAddCookieApiService().getActivityDetail(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HotBlogsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HotBlogsEntity> entity) {
                super.onSuccess(entity);
                HotBlogsEntity hotBlogsEntity = entity.data;
                isLoading = false;
                iView.getActivityDetail(hotBlogsEntity.list, hotBlogsEntity.detail, hotBlogsEntity.pager.page, hotBlogsEntity.pager.total_page);
                currentPage = hotBlogsEntity.pager.page;
                allPage = hotBlogsEntity.pager.total_page;
                currentPage++;
            }

            @Override
            public void onFailure() {
                isLoading = false;
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                isLoading = false;
                super.onErrorCode(code, message);
            }
        });
    }


    //1关注，2取消关注
    public void focusUser(int type, String memberId) {
        Map<String, String> map = new HashMap<>();
        if (type == 0) {
            map.put("type", "1");
        } else {
            map.put("type", "2");
        }
        map.put("member_id", memberId);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().focusUser(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.focusUser(type, memberId);
                Common.staticToast(entity.message);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void praiseBlos(String blogId) {
        Map<String, String> map = new HashMap<>();
        map.put("blog_id", blogId);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().praiseBlog(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.praiseBlog(blogId);
                Common.staticToast(entity.message);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void downCount(String blogId) {
        Map<String, String> map = new HashMap<>();
        map.put("blog_id", blogId);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().downCount(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.downCountSuccess(blogId);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void goodsShare(String type, String blogId, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("id", id);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getSaveCookieApiService().shareSuccessCall(getRequestBody(map));
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast("感谢您的分享");
                iView.shareGoodsSuccess(blogId, id);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getActivityDetail(false, currentActId);
            }
        }
    }
}
