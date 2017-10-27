package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RegisterFinishEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IRegisterTwoView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class RegisterTwoPresenter extends BasePresenter<IRegisterTwoView> {

    public RegisterTwoPresenter(Context context, IRegisterTwoView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

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
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void register(String mobile,String mobile_code,String code,String password,String nickname,String unique_sign){
        Map<String,String> map = new HashMap<>();
        if (!TextUtils.isEmpty(unique_sign)){
            map.put("unique_sign",unique_sign);
        }
        map.put("mobile",mobile);
        map.put("mobile_code",mobile_code);
        map.put("code",code);
        map.put("password",password);
        map.put("nickname",nickname);
        sortAndMD5(map);
        String s = null;
        try {
            s = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), s);
        Call<BaseEntity<RegisterFinishEntity>> register = getApiService().register(requestBody);
        getNetData(register, new SimpleNetDataCallback<BaseEntity<RegisterFinishEntity>>() {
            @Override
            public void onSuccess(BaseEntity<RegisterFinishEntity> entity) {
                super.onSuccess(entity);
                iView.registerFinish(entity.data);
            }
        });
    }

    public void findPsw(String mobile, String password, String pwd, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("password", password);
        map.put("pwd", pwd);
        map.put("code", code);
        sortAndMD5(map);
        String s = null;
        try {
            s = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), s);
        Call<BaseEntity<RegisterFinishEntity>> register = getApiService().findPsw(requestBody);
        getNetData(register, new SimpleNetDataCallback<BaseEntity<RegisterFinishEntity>>() {
            @Override
            public void onSuccess(BaseEntity<RegisterFinishEntity> entity) {
                iView.resetPsw(entity.message);
                super.onSuccess(entity);
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
