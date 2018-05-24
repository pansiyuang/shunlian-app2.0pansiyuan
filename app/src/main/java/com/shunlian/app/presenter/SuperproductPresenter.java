package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.SuperProductEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISuperProductView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/24.
 */

public class SuperproductPresenter extends BasePresenter<ISuperProductView> {

    public SuperproductPresenter(Context context, ISuperProductView iView) {
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

    public void getProductList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<SuperProductEntity>> classesshare = getApiService().superProductsList(map);
        getNetData(false, classesshare, new SimpleNetDataCallback<BaseEntity<SuperProductEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SuperProductEntity> entity) {
                super.onSuccess(entity);
                SuperProductEntity superProductEntity = entity.data;
                iView.getProductList(superProductEntity.data);
            }
        });
    }
}
