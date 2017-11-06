package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IRegisterOneView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/20.
 */

public class RegisterOnePresenter extends BasePresenter<IRegisterOneView> {


    public RegisterOnePresenter(Context context, IRegisterOneView iView) {
        super(context, iView);
        initApi();
    }

    @Override
    protected void initApi() {
        getCode();
    }

    public void getCode() {
        Call<ResponseBody> responseBodyCall = getSaveCookieApiService().graphicalCode();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    byte[] bytes = response.body().bytes();
                    iView.setCode(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void checkCode(String code){
        Map<String,String> map = new HashMap<>();
        map.put("code",code);
        sortAndMD5(map);
        Call<BaseEntity<String>> baseEntityCall = getApiService().checkCode(map);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<String>>(){
            @Override
            public void onSuccess(BaseEntity<String> entity) {
                super.onSuccess(entity);
                iView.checkCode(true);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.checkCode(false);
            }
        });
    }

    public void checkPhone(String mobile){
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        sortAndMD5(map);
        Call<BaseEntity<String>> baseEntityCall = getApiService().checkMobile(map);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<String>>(){
            @Override
            public void onSuccess(BaseEntity<String> entity) {
                super.onSuccess(entity);
                iView.checkMobile(true);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.checkMobile(false);
            }
        });
    }

    public void sendSmsCode(String phone, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("vcode", code);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<String>> baseEntityCall = getAddCookieApiService().sendSmsCode(requestBody);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<String>>() {
                @Override
                public void onSuccess(BaseEntity<String> entity) {
                    super.onSuccess(entity);
                    iView.smsCode(entity.data);
                }

                @Override
                public void onErrorCode(int code, String message) {
                    super.onErrorCode(code, message);
                    Common.staticToast(message);
                    iView.smsCode(null);
                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
