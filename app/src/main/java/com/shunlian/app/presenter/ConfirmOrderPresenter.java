package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IConfirmOrderView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/29.
 */

public class ConfirmOrderPresenter extends BasePresenter<IConfirmOrderView> {


    public ConfirmOrderPresenter(Context context, IConfirmOrderView iView) {
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

    /**
     * 立即购买接口
     * @param goods_id
     * @param qty
     * @param sku_id
     */
    public void orderBuy(String goods_id,String qty,String sku_id){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("qty",qty);
        map.put("sku_id",sku_id);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<ConfirmOrderEntity>> baseEntityCall = getAddCookieApiService().orderBuy(requestBody);

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<ConfirmOrderEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ConfirmOrderEntity> entity) {
                super.onSuccess(entity);
                System.out.println(entity.data.toString());
            }
        });
    }

    /**
     * 购物车进入购买
     * @param cart_ids
     * @param address_id
     */
    public void orderConfirm(String cart_ids,String address_id){
        Map<String,String> map = new HashMap<>();
        map.put("cart_ids",cart_ids);
        map.put("address_id",address_id);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<ConfirmOrderEntity>> baseEntityCall = getAddCookieApiService().orderConfirm(requestBody);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<ConfirmOrderEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ConfirmOrderEntity> entity) {
                super.onSuccess(entity);
                iView.confirmOrderAllGoods(entity.data.enabled,entity.data.disabled);
            }
        });
    }
}