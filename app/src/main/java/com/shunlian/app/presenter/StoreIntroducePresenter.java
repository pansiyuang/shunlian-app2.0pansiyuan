package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ISendSmsView;
import com.shunlian.app.view.StoreIntroduceView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/20.
 */

public class StoreIntroducePresenter extends BasePresenter<StoreIntroduceView> {
    private String storeId;
    public StoreIntroducePresenter(Context context, StoreIntroduceView iView, String storeId) {
        super(context, iView);
        this.storeId=storeId;
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<StoreIntroduceEntity>> baseEntityCall = getApiService().storeIntroduce(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreIntroduceEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreIntroduceEntity> entity) {
                super.onSuccess(entity);
                StoreIntroduceEntity data = entity.data;
                if (data != null) {
                    LogUtil.httpLogW("StoreIntroduceEntity:" + data);
                    iView.introduceInfo(data);
                }
            }
        });
    }
    /**
     * 关注店铺
     *
     * @param storeId
     */
    public void followStore(String storeId) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        sortAndMD5(map);
        String stringEntry = null;
        try {
            stringEntry = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addMark(requestBody);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.storeFocus();
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
     *
     * @param storeId
     */
    public void delFollowStore(String storeId) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        sortAndMD5(map);
        String stringEntry = null;
        try {
            stringEntry = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delMark(requestBody);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                iView.storeFocus();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }


}
