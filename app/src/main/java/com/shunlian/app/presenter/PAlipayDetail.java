package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GetRealInfoEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IAlipayDetail;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PAlipayDetail extends BasePresenter<IAlipayDetail> {

    public PAlipayDetail(Context context, IAlipayDetail iView) {
        super(context, iView);
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }


    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<GetRealInfoEntity>> baseEntityCall = getApiService().balanceGetRealInfo(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetRealInfoEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetRealInfoEntity> entity) {
                super.onSuccess(entity);
                iView.setApiData(entity.data);
            }
        });

    }

}
