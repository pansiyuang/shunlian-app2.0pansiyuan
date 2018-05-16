package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IMegerView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MegerPresenter extends BasePresenter<IMegerView> {
    public MegerPresenter(Context context, IMegerView iView) {
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

    public void getMegercCates(String meger) {
        Map<String, String> map = new HashMap<>();
        map.put("join_sign", meger);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CateEntity>> baseEntityCall = getAddCookieApiService().megerGoodsCates(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CateEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CateEntity> entity) {
                CateEntity cateEntity = entity.data;
                if (cateEntity != null) {
                    iView.getCateEntity(cateEntity);
                }
                super.onSuccess(entity);
            }
        });
    }

    public void getGoodsSku(String goodsId) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goodsId);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<GoodsDeatilEntity.GoodsInfo>> baseEntityCall = getAddCookieApiService().getGoodsSku(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity.GoodsInfo>>() {
            @Override
            public void onSuccess(BaseEntity<GoodsDeatilEntity.GoodsInfo> entity) {
                GoodsDeatilEntity.GoodsInfo goodsInfo = entity.data;
                if (goodsInfo != null) {
                    iView.getGoodsInfo(goodsInfo);
                }
                super.onSuccess(entity);
            }
        });
    }

    public void addCart(String goods_id, String sku_id, String qty,String promId) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        map.put("sku_id", sku_id);
        map.put("qty", qty);
        map.put("prom_id", promId);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CateEntity>> baseEntityCall = getAddCookieApiService().addCart(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CateEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CateEntity> entity) {
                super.onSuccess(entity);
                CateEntity cateEntity = entity.data;
                if (cateEntity != null) {
                    iView.addCart(entity.data);
                }
            }
        });
    }
}
