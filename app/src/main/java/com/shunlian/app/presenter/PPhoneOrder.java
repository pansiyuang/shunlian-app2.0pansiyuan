package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PhoneOrderDetailEntity;
import com.shunlian.app.bean.PhoneRecordEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPhoneOrder;
import com.shunlian.app.view.IPhoneRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/17.
 */

public class PPhoneOrder extends BasePresenter<IPhoneOrder> {
    private String id;

    public PPhoneOrder(Context context, IPhoneOrder iView,String id) {
        super(context, iView);
        this.id=id;
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

public void detail(){
    Map<String, String> map = new HashMap<>();
    map.put("id",id);
    sortAndMD5(map);

    Call<BaseEntity<PhoneOrderDetailEntity>> estimateDetail = getAddCookieApiService()
            .virtualOrderDetail(getRequestBody(map));

    getNetData(true, estimateDetail, new SimpleNetDataCallback<BaseEntity<PhoneOrderDetailEntity>>() {
        @Override
        public void onSuccess(BaseEntity<PhoneOrderDetailEntity> entity) {
            super.onSuccess(entity);
            PhoneOrderDetailEntity data = entity.data;
            if (data!=null)
                iView.setApiData(data);
        }
    });
}

    public void result() {
        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        sortAndMD5(map);

        Call<BaseEntity<PhoneOrderDetailEntity>> estimateDetail = getAddCookieApiService()
                .orderPayResult(getRequestBody(map));

        getNetData(true, estimateDetail, new SimpleNetDataCallback<BaseEntity<PhoneOrderDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PhoneOrderDetailEntity> entity) {
                super.onSuccess(entity);
                PhoneOrderDetailEntity data = entity.data;
                if (data!=null)
                iView.setApiData(data);
            }
        });

    }
}
