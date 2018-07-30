package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IRegisterAndBindView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhanghe on 2018/7/23.
 */

public class RegisterAndBindPresenter extends BasePresenter<IRegisterAndBindView> {

    public RegisterAndBindPresenter(Context context, IRegisterAndBindView iView) {
        super(context, iView);
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        getCode();
    }

    public void getCode() {
        Call<ResponseBody> responseBodyCall = getSaveCookieApiService().graphicalCode();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() == null)return;
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


    /**
     * 登录时检验手机号是否注册，仅用手机验证码登录
     */
    public void checkMobile(String mobile,String type){
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("type",type);
        sortAndMD5(map);
        Call<BaseEntity<String>> baseEntityCall = getApiService().checkMobile(map);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<String>>(){
            @Override
            public void onSuccess(BaseEntity<String> entity) {
                super.onSuccess(entity);
                iView.iSMobileRight(true);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
                iView.iSMobileRight(false);
            }
        });
    }

    /**
     * 检验推荐人id
     * @param code
     */
    public void checkRefereesId(String code){
        Map<String,String> map = new HashMap<>();
        map.put("code",code);
        sortAndMD5(map);
        Call<BaseEntity<String>> baseEntityCall = getApiService().checkCode(map);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<String>>(){
            @Override
            public void onSuccess(BaseEntity<String> entity) {
                super.onSuccess(entity);
                iView.isRefereesId(true);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.isRefereesId(false);
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
                    iView.smsCode(entity.message);
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


    public void loginMobile(String mobile, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "mobile");
        map.put("mobile", mobile);
        map.put("code", code);
        sortAndMD5(map);
        String stringEntry = null;
        try {
            stringEntry = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        Call<BaseEntity<LoginFinishEntity>> requestBodyCall = getSaveCookieApiService().login(requestBody);

        getNetData(true,requestBodyCall,new SimpleNetDataCallback<BaseEntity<LoginFinishEntity>>(){
            @Override
            public void onSuccess(BaseEntity<LoginFinishEntity> entity) {
                super.onSuccess(entity);
                iView.loginMobileSuccess(entity.data);
            }
        });
    }

    /**
     *
     * @param mobile 手机号
     * @param mobile_code 短信验证码
     * @param code 推荐人id
     * @param nickname 昵称
     * @param unique_sign 微信登录code
     */
    public void register(String mobile,String mobile_code,String code,String nickname,String unique_sign){
        Map<String,String> map = new HashMap<>();
        if (!TextUtils.isEmpty(unique_sign)){
            map.put("unique_sign",unique_sign);
        }
        map.put("mobile",mobile);
        map.put("mobile_code",mobile_code);
        map.put("code",code);
        map.put("nickname",nickname);
        sortAndMD5(map);
        String s = null;
        try {
            s = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), s);
        Call<BaseEntity<LoginFinishEntity>> register = getSaveCookieApiService().new_register(requestBody);
        getNetData(register, new SimpleNetDataCallback<BaseEntity<LoginFinishEntity>>() {
            @Override
            public void onSuccess(BaseEntity<LoginFinishEntity> entity) {
                super.onSuccess(entity);
                iView.loginMobileSuccess(entity.data);
            }
        });
    }

    /**
     * 绑定导购员
     * @param member_id
     * @param code
     * @param mobile
     * @param mobile_code
     */
    public void bindShareid(String member_id,String code,String mobile,String mobile_code){
        Map<String,String> map = new HashMap<>();
        map.put("member_id",member_id);
        map.put("code",code);
        map.put("mobile",mobile);
        map.put("mobile_code",mobile_code);
        sortAndMD5(map);

        Call<BaseEntity<LoginFinishEntity>>
                baseEntityCall = getAddCookieApiService().bindShareid(getRequestBody(map));

        getNetData(true,baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<LoginFinishEntity>>(){
            @Override
            public void onSuccess(BaseEntity<LoginFinishEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                iView.loginMobileSuccess(entity.data);
            }
        });
    }
}
