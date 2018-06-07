package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ExperienceEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IExperienceView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ExperiencePresenter extends BasePresenter<IExperienceView> {
    public static final int PAGE_SIZE = 20;

    public ExperiencePresenter(Context context, IExperienceView iView) {
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

    public void getExperienceList(boolean isShowLoading) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<ExperienceEntity>> baseEntityCall = getApiService().experienceList(map);
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ExperienceEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ExperienceEntity> entity) {
                super.onSuccess(entity);
                ExperienceEntity experienceEntity = entity.data;
                isLoading = false;
                iView.getExperienceList(experienceEntity.list, Integer.valueOf(experienceEntity.page), Integer.valueOf(experienceEntity.total_page));
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

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getExperienceList(false);
            }
        }
    }
}
