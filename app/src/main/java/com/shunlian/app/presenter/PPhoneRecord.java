package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.DetailOrderRecordAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DetailOrderRecordEntity;
import com.shunlian.app.bean.DiscoveryTieziEntity;
import com.shunlian.app.bean.PhoneRecordEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IDetailOrderRecordView;
import com.shunlian.app.view.IPhoneRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/17.
 */

public class PPhoneRecord extends BasePresenter<IPhoneRecord> {

    private List<PhoneRecordEntity.MData> mDatas = new ArrayList<>();


    public PPhoneRecord(Context context, IPhoneRecord iView) {
        super(context, iView);
        getApiData();
    }

    @Override
    protected void initApi() {

    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    public void refreshBaby() {
        if (!pageIsLoading && currentPage <= allPage) {
            pageIsLoading = true;
            getApiData();
        }
    }


    public void getApiData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", page_size);
        sortAndMD5(map);

        Call<BaseEntity<PhoneRecordEntity>> estimateDetail = getAddCookieApiService()
                .rechargelist(getRequestBody(map));

        getNetData(true, estimateDetail, new SimpleNetDataCallback<BaseEntity<PhoneRecordEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PhoneRecordEntity> entity) {
                super.onSuccess(entity);
                PhoneRecordEntity data = entity.data;
                pageIsLoading = false;
                currentPage++;
                allPage = Integer.parseInt(data.pager.total_page);
                mDatas.addAll(data.list);
                iView.setApiData(data.pager,mDatas);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
                pageIsLoading=false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
                pageIsLoading=false;
            }
        });

    }
}
