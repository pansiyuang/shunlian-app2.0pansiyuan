package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IGoodsDetailView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDetailPresenter extends BasePresenter<IGoodsDetailView> {


    private String goods_id;

    public GoodsDetailPresenter(Context context, IGoodsDetailView iView, String goods_id) {
        super(context, iView);
        this.goods_id = goods_id;
        initApi();
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<GoodsDeatilEntity>> baseEntityCall = getApiService().goodsDetail(requestBody);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity>>() {
                @Override
                public void onSuccess(BaseEntity<GoodsDeatilEntity> entity) {
                    super.onSuccess(entity);
                    GoodsDeatilEntity data = entity.data;
                    if (data != null) {
                        iView.goodsDetailData(data);
                        iView.isFavorite(data.is_fav);
                    }


                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关注店铺
     * @param storeId
     */
    public void followStore(String storeId){
        Map<String,String> map = new HashMap<>();
        map.put("storeId",storeId);
        sortAndMD5(map);
        String stringEntry = null;
        try {
            stringEntry = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addMark(requestBody);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    /**
     * 取消关注店铺
     * @param storeId
     */
    public void delFollowStore(String storeId){
        Map<String,String> map = new HashMap<>();
        map.put("storeId",storeId);
        sortAndMD5(map);
        String stringEntry = null;
        try {
            stringEntry = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delMark(requestBody);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void addCart(String goods_id,String sku_id,String qty){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("sku_id",sku_id);
        map.put("qty",qty);
        sortAndMD5(map);
        String stringEntry = null;
        try {
            stringEntry = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addCart(requestBody);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.addCart(entity.message);
            }
        });

    }
    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
