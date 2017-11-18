package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IShoppingCarView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/18.
 */

public class ShopCarPresenter extends BasePresenter<IShoppingCarView> {
    public ShopCarPresenter(Context context, IShoppingCarView iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void initShopData(){
        initApi();
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<ShoppingCarEntity>> baseEntityCall = getApiService().storeList(requestBody);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShoppingCarEntity>>() {
                @Override
                public void onSuccess(BaseEntity<ShoppingCarEntity> entity) {
                    super.onSuccess(entity);
                    ShoppingCarEntity shoppingCarEntity = entity.data;
                    if (shoppingCarEntity != null) {
                       iView.OnShoppingCarEntity(shoppingCarEntity);
                    }
                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
