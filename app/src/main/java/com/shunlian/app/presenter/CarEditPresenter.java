package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IEditCarView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/27.
 */

public class CarEditPresenter extends BasePresenter<IEditCarView> {
    public CarEditPresenter(Context context, IEditCarView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }

    public void editCar(String carId, String qty, String skuId, String promId, String isCheck) {
        Map<String, String> map = new HashMap<>();
        map.put("cart_id", carId);
        if (!TextUtils.isEmpty(qty)) {
            map.put("qty", qty);
        }
        if (!TextUtils.isEmpty(skuId)) {
            map.put("sku_id", skuId);
        }
        if (!TextUtils.isEmpty(promId)) {
            map.put("prom_id", promId);
        }
        if (!TextUtils.isEmpty(isCheck)) {
            map.put("is_check", isCheck);
        }
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<ShoppingCarEntity>> baseEntityCall = getApiService().carEdit(requestBody);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShoppingCarEntity>>() {
                @Override
                public void onSuccess(BaseEntity<ShoppingCarEntity> entity) {
                    ShoppingCarEntity shoppingCarEntity = entity.data;
                    if (shoppingCarEntity != null) {
                        iView.OnEditEntity(shoppingCarEntity);
                    }
                    super.onSuccess(entity);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
