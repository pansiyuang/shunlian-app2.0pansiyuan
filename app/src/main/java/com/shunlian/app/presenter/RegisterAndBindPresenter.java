package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.bean.RegisterFinishEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IRegisterAndBindView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
                Common.staticToast(message);
                iView.smsCode(null);
            }
        });
    }

    /**
     * 手机号登录
     * @param mobile
     * @param code
     */
    public void loginMobile(String mobile, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "mobile");
        map.put("mobile", mobile);
        map.put("code", code);
        sortAndMD5(map);

        Call<BaseEntity<LoginFinishEntity>>
                requestBodyCall = getSaveCookieApiService().login(getRequestBody(map));

        getNetData(true,requestBodyCall,new SimpleNetDataCallback<BaseEntity<LoginFinishEntity>>(){
            @Override
            public void onSuccess(BaseEntity<LoginFinishEntity> entity) {
                super.onSuccess(entity);
                iView.loginMobileSuccess(entity.data);
            }
        });
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

        Call<BaseEntity<LoginFinishEntity>>
                register = getSaveCookieApiService().new_register(getRequestBody(map));
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

    /**
     * 找回密码
     * @param mobile
     * @param password
     * @param pwd
     * @param code
     */
    public void findPsw(String mobile, String password, String pwd, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("password", password);
        map.put("pwd", pwd);
        map.put("code", code);
        sortAndMD5(map);

        Call<BaseEntity<RegisterFinishEntity>>
                register = getApiService().findPsw(getRequestBody(map));
        getNetData(register, new SimpleNetDataCallback<BaseEntity<RegisterFinishEntity>>() {
            @Override
            public void onSuccess(BaseEntity<RegisterFinishEntity> entity) {
                super.onSuccess(entity);
                iView.findPwdSuccess(entity.message);
            }
        });
    }

    /**
     * 检验短信验证码
     * @param mobile
     * @param smscode
     */
    public void checkMobileCode(String mobile,String smscode){
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("mobile_code", smscode);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>>
                baseEntityCall = getApiService().checkMobileCode(getRequestBody(map));
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.checkMobileSmsCode(entity.message);
            }
        });
    }
}
