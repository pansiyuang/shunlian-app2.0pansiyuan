package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
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

    public void orderBuy(String goods_id,String qty,String sku_id){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("qty",qty);
        map.put("sku_id",sku_id);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().orderBuy(requestBody);

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
            }
        });
    }
}
