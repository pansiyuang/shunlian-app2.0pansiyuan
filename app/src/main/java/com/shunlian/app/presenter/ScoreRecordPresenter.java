package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CreditLogEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IScoreRecordView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class ScoreRecordPresenter extends BasePresenter<IScoreRecordView> {
    public static final int PAGE_SIZE = 20;

    public ScoreRecordPresenter(Context context, IScoreRecordView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }

    public void initPage() {
        currentPage = 1;
    }

    public void getScoreRecord(boolean isFirst) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<CreditLogEntity>> noAddressOrder = getApiService().getScoreRecord(map);
        getNetData(isFirst, noAddressOrder, new SimpleNetDataCallback<BaseEntity<CreditLogEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CreditLogEntity> entity) {
                isLoading = false;
                iView.getScoreRecord(entity.data.list, entity.data.page, entity.data.total_page);
                currentPage = entity.data.page;
                allPage = entity.data.total_page;
                if (currentPage == 1) {
                    iView.refreshFinish();
                }
                currentPage++;
                super.onSuccess(entity);
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
                getScoreRecord(false);
            }
        }
    }
}
