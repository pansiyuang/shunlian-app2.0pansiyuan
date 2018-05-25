package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ProductDetailEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IGifDetailView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/25.
 */

public class GifDetailPresenter extends BasePresenter<IGifDetailView> {

    public GifDetailPresenter(Context context, IGifDetailView iView) {
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

    public void getProductDetail(String productId) {
        Map<String, String> map = new HashMap<>();
        map.put("product_id", productId);
        sortAndMD5(map);
        Call<BaseEntity<ProductDetailEntity>> baseEntityCall = getApiService().getProductDetail(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ProductDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ProductDetailEntity> entity) {
                super.onSuccess(entity);
                ProductDetailEntity productDetailEntity = entity.data;
                iView.getGifDetail(productDetailEntity);
            }
        });
    }
}
