package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GifProductEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IGifBagView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/25.
 */

public class GifBagPresenter extends BasePresenter<IGifBagView> {

    public GifBagPresenter(Context context, IGifBagView iView) {
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

    public void getGifList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<GifProductEntity>> baseEntityCall = getApiService().getProductList(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GifProductEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GifProductEntity> entity) {
                super.onSuccess(entity);
                GifProductEntity gifProductEntity = entity.data;
                iView.getGifList(gifProductEntity.list);
            }
        });
    }
}
