package com.shunlian.app.wxapi;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.WXLoginEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.BasePresenter;
import com.shunlian.app.utils.Constant;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/24.
 */

public class WXEntryPresenter extends BasePresenter<WXEntryView>{

    public WXEntryPresenter(Context context, WXEntryView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }

    public void notifyShare(String type,String id){
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("id",id);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().shareAdd(requestBody);

        getNetData(false,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    Constant.SHARE_TYPE="";
                    if (iView!=null)
                    iView.notifyCallback(data);
                }
            }

            @Override
            public void onErrorData(BaseEntity<CommonEntity> commonEntityBaseEntity) {
                Constant.SHARE_TYPE="";
                super.onErrorData(commonEntityBaseEntity);
                iView.cloasePage();
            }

            @Override
            public void onErrorCode(int code, String message) {
                Constant.SHARE_TYPE="";
                super.onErrorCode(code, message);
                iView.cloasePage();
            }

            @Override
            public void onFailure() {
                Constant.SHARE_TYPE="";
                super.onFailure();
                iView.cloasePage();
            }
        });
    }

    public void wxLogin(String code){
        Map<String,String> map = new HashMap<>();
        map.put("code",code);
        sortAndMD5(map);
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),json);
        final Call<BaseEntity<WXLoginEntity>> baseEntityCall = getSaveCookieApiService().wxLogin(requestBody);

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<WXLoginEntity>>(){
            @Override
            public void onSuccess(BaseEntity<WXLoginEntity> entity) {
                super.onSuccess(entity);
                iView.onWXCallback(entity);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.onWXCallback(null);
                iView.cloasePage();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.cloasePage();
            }
        });
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }


    public void goodsShare(String type,String id){
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("id",id);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>>
                baseEntityCall = getSaveCookieApiService().shareSuccessCall(getRequestBody(map));

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                String tip = "恭喜获得%s金蛋";
                iView.golde_eggs(String.format(tip,entity.data.gold_num));
                Constant.SHARE_TYPE="";
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Constant.SHARE_TYPE="";
                iView.cloasePage();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                Constant.SHARE_TYPE="";
                iView.cloasePage();
            }
        });
    }
}
