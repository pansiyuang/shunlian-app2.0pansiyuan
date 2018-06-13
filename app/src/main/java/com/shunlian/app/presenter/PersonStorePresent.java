package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.PersonShopEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IPersonStoreView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/2/28.
 */

public class PersonStorePresent extends BasePresenter<IPersonStoreView> {

    private Call<BaseEntity<CommonEntity>> fairishNumsCall;

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
        Call<BaseEntity<PersonShopEntity>> baseEntityCall = getAddCookieApiService().getPersonShop(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<PersonShopEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PersonShopEntity> entity) {
                super.onSuccess(entity);
                PersonShopEntity personShopEntity = entity.data;
                iView.getShopDetail(personShopEntity);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void getFairishNums() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        fairishNumsCall = getAddCookieApiService().fairishNums(map);
        getNetData(false, fairishNumsCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity = entity.data;
                iView.getFairishNums(commonEntity.num, false);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void delStorGoods(String goodsIds) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_ids", goodsIds);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().delGoods(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity = entity.data;
                iView.getFairishNums(commonEntity.num, true);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void cancelRequest() {
        if (fairishNumsCall != null && !fairishNumsCall.isCanceled()) {
            fairishNumsCall.cancel();
        }
    }
}
