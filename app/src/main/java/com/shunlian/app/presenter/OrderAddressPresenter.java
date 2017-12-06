package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.AddressDataEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
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
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<AddressDataEntity>>() {
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
}
