package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PLUSConfirmEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPLUSConfirmView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/29.
 */

public class PLUSConfirmOrderPresenter extends BasePresenter<IPLUSConfirmView> {


    public PLUSConfirmOrderPresenter(Context context, IPLUSConfirmView iView) {
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
     * @param product_id
     * @param sku_id
     */
    public void orderBuy(String product_id,String sku_id,String address_id){
        Map<String,String> map = new HashMap<>();
        map.put("product_id",product_id);
        map.put("sku_id",sku_id);
        if (!TextUtils.isEmpty(address_id)) {
            map.put("address_id", address_id);
        }
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<PLUSConfirmEntity>> baseEntityCall = getAddCookieApiService().plusConfirm(requestBody);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<PLUSConfirmEntity>>(){
            @Override
            public void onSuccess(BaseEntity<PLUSConfirmEntity> entity) {
                super.onSuccess(entity);
            }
        });
    }
}
