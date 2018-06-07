package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BalanceDetailEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.WithdrawListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IBalanceDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PBalanceDetail extends BasePresenter<IBalanceDetail> {
    private int pageSize = 20;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading = false;
    private List<BalanceDetailEntity.Balance> mDatas = new ArrayList<>();
    private List<WithdrawListEntity.Record> mDatass = new ArrayList<>();

    public PBalanceDetail(Context context, IBalanceDetail iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getApiData();
        }
    }
    public void refreshBabys() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getApiDatas();
        }
    }

    public void getApiDatas() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(babyPage));
        map.put("page_size", String.valueOf(pageSize));
        sortAndMD5(map);
        Call<BaseEntity<WithdrawListEntity>> baseEntityCall = getAddCookieApiService().withdrawList(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<WithdrawListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<WithdrawListEntity> entity) {
                super.onSuccess(entity);
                WithdrawListEntity data = entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(data.pager.total_page);
                mDatass.addAll(data.list);
                iView.setApiDatas(data.pager, mDatass);
            }

            @Override
            public void onFailure() {
               iView.showDataEmptyView(1);
                super.onFailure();
            }
        });
    }

    public void getApiData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(babyPage));
        map.put("pageSize", String.valueOf(pageSize));
        sortAndMD5(map);
        Call<BaseEntity<BalanceDetailEntity>> baseEntityCall = getAddCookieApiService().balanceTransactionList(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<BalanceDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<BalanceDetailEntity> entity) {
                super.onSuccess(entity);
                BalanceDetailEntity data = entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(data.total_page);
                mDatas.addAll(data.list);
                iView.setApiData(data, mDatas);
            }
            @Override
            public void onFailure() {
                iView.showDataEmptyView(1);
                super.onFailure();
            }
        });
    }

    @Override
    protected void initApi() {

    }

}
