package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.AddressDataEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IOrderAddressView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/5.
 */

public class OrderAddressPresenter extends BasePresenter<IOrderAddressView> {
    public OrderAddressPresenter(Context context, IOrderAddressView iView) {
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

    public void getAddressList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<AddressDataEntity>> baseEntityCall = getAddCookieApiService().allAddress(requestBody);
            getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<AddressDataEntity>>() {
                @Override
                public void onSuccess(BaseEntity<AddressDataEntity> entity) {
                    super.onSuccess(entity);
                    AddressDataEntity addressDataEntity = entity.data;
                    if (addressDataEntity != null) {
                        iView.orderList(addressDataEntity.address_list);
                    }
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void delAddress(final String addressId) {
        Map<String, String> map = new HashMap<>();
        map.put("address_id", addressId);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delAddress(requestBody);
            getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
                @Override
                public void onSuccess(BaseEntity<EmptyEntity> entity) {
                    super.onSuccess(entity);
                    iView.delAddressSuccess(addressId);
                }

                @Override
                public void onErrorCode(int code, String message) {
                    iView.delAddressFail();
                    super.onErrorCode(code, message);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void addressDefault(String address_id) {
        Map<String, String> map = new HashMap<>();
        map.put("isdefault", "1");
        map.put("address_id", address_id);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addressEdit(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                if (entity.code == 1000) {
                    iView.editAddressSuccess();
                } else {
                    iView.delAddressFail();
                }
            }
        });
    }
}
