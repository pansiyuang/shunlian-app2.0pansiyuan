package com.shunlian.app.adapter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.GoldEggPrizeEntity;
import com.shunlian.app.bean.SaturdayDrawRecordEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.BasePresenter;
import com.shunlian.app.view.IDrawRecordView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class DrawRecordPresenter extends BasePresenter<IDrawRecordView> {
    public static final int PAGE_SIZE = 20;

    public DrawRecordPresenter(Context context, IDrawRecordView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }

    public void initPage() {
        currentPage = 1;
    }

    public void getDrawRecord(boolean isFirst) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<SaturdayDrawRecordEntity>> brandlist = getApiService().getDrawRecord(map);
        getNetData(isFirst, brandlist, new SimpleNetDataCallback<BaseEntity<SaturdayDrawRecordEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SaturdayDrawRecordEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                iView.getDrawRecord(entity.data.list, entity.data.page, entity.data.total_page);
                currentPage = entity.data.page;
                allPage = entity.data.total_page;
                currentPage++;
            }
        });
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getDrawRecord(false);
            }
        }
    }
}
