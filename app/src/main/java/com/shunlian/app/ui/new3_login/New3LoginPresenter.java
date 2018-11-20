package com.shunlian.app.ui.new3_login;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.BasePresenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhanghe on 2018/11/20.
 * 第三版登录接口
 */

public class New3LoginPresenter extends BasePresenter<INew3LoginView> {

    public New3LoginPresenter(Context context, INew3LoginView iView) {
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
    protected void initApi() {

    }

    /**
     * 密码登录
     * @param username
     * @param password
     */
    public void loginPwd(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "username");
        map.put("username", username);
        map.put("password", password);
        sortAndMD5(map);

        Call<BaseEntity<LoginFinishEntity>>
                requestBodyCall = getSaveCookieApiService().login(getRequestBody(map));

        getNetData(true,requestBodyCall,new SimpleNetDataCallback<BaseEntity<LoginFinishEntity>>(){
            @Override
            public void onSuccess(BaseEntity<LoginFinishEntity> entity) {
                super.onSuccess(entity);
                iView.accountPwdSuccess(entity.data);
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
                iView.iSMobileRight(true,entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.iSMobileRight(false,message);
            }
        });
    }

    /**
     * 发送短信验证码
     * @param phone
     * @param code
     */
    public void sendSmsCode(String phone, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("vcode", code);
        sortAndMD5(map);

        Call<BaseEntity<String>>
                baseEntityCall = getAddCookieApiService().sendSmsCode(getRequestBody(map));
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<String>>() {
            @Override
            public void onSuccess(BaseEntity<String> entity) {
                super.onSuccess(entity);
                iView.smsCode(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.smsCode(null);
            }
        });
    }

    /**
     * 获取图形验证码
     */
    public void getPictureCode() {
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
}
