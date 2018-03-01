package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.PersonShopEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPersonStoreView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/2/28.
 */

public class PersonStorePresent extends BasePresenter<IPersonStoreView> {

    public PersonStorePresent(Context context, IPersonStoreView iView) {
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

    public void getPersonDetail() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<PersonShopEntity>> baseEntityCall = getApiService().getPersonShop(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<PersonShopEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PersonShopEntity> entity) {
                super.onSuccess(entity);
                PersonShopEntity personShopEntity = entity.data;
                iView.getShopDetail(personShopEntity);
            }
        });
    }
    public void getFairishNums() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().fairishNums(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity= entity.data;
                iView.getFairishNums(commonEntity.num);
            }
        });
    }
}
