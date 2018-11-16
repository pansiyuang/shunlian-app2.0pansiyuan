package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IActivityView;
import com.shunlian.app.view.IView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/10/18.
 */

public class ActivityPresenter extends BasePresenter<IActivityView> {
    public static final int PAGE_SIZE = 10;
    public String currentKeyword;
    public String more;

    public ActivityPresenter(Context context, IActivityView iView) {
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

    public void getActivities(boolean isFirst) {
        getActivities(isFirst, "", "");
    }

    public void getActivities(boolean isFirst, String keyword, String isMore) {
        currentKeyword = keyword;
        more = isMore;
        Map<String, String> map = new HashMap<>();
        if (!isEmpty(keyword)) {
            map.put("key_word", keyword);
        }
        if (!isEmpty(isMore)) {
            map.put("more", isMore);
        }
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<DiscoverActivityEntity>> baseEntityCall = getApiService().getActivitys(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<DiscoverActivityEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DiscoverActivityEntity> entity) {
                super.onSuccess(entity);
                DiscoverActivityEntity activityEntity = entity.data;
                isLoading = false;
                iView.getActivities(activityEntity, activityEntity.pager.page, activityEntity.pager.total_page);
                currentPage = activityEntity.pager.page;
                allPage = activityEntity.pager.total_page;
                if (currentPage == 1) {
                    iView.refreshFinish();
                }
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

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getActivities(false, currentKeyword, more);
            }
        }
    }
}
