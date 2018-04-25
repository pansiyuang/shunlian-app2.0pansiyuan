package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IChangeUserView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/4/24.
 */

public class ChangeUserPresenter extends BasePresenter<IChangeUserView> {
    /*检验图形验证码*/
    public final String checkCode ="checkImageCode";
    /*修改密码*/
    public final String modifyPwd ="changePassword";
    /*对原手机号发送短信验证当前身份*/
    public final String sendSmsCode ="sendSmsCode";
    /*修改手机号*/
    public final String changeMobile ="changeMobile";

    public ChangeUserPresenter(Context context, IChangeUserView iView) {
        super(context, iView);
        initApi();
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
     * 获取图形验证码
     */
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

    /**
     * 获取手机号
     */
    public void getMobile(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> mobile = getApiService().getMobile(map);
        getNetData(true,mobile,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.setMobile(entity.data.mobile);
            }
        });
    }

    /**
     * 校验图形验证码
     * @param vcode
     */
    public void checkImageCode(String vcode){
        Map<String, String> map = new HashMap<>();
        map.put("vcode", vcode);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> mobile = getAddCookieApiService()
                .userAndPwd(checkCode,getRequestBody(map));
        getNetData(true,mobile,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.setCheckSuccess();
            }
        });
    }

    /**
     * 校验短信验证码
     * @param smsCode
     */
    public void checkSmsCode(String smsCode){
        Map<String,String> map = new HashMap<>();
        map.put("smscode",smsCode);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService()
                .checkSmsCode(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.key(entity.data.key);
            }
        });
    }

    /**
     * 验证新手机号，并发送短信验证码
     * @param mobile
     * @param vcode
     * @param key
     */
    public void checkNewMobile(String mobile,String vcode,String key){
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("vcode",vcode);
        map.put("key",key);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService()
                .checkNewMobile(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.goBindNewMobile(entity.data.mobile,entity.data.key);
            }
        });
    }

    /**
     * 修改手机号
     * @param key
     * @param smsCode
     */
    public void changeMobile(String key,String smsCode){
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("smscode", smsCode);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> mobile = getAddCookieApiService()
                .userAndPwd(changeMobile,getRequestBody(map));
        getNetData(true,mobile,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.bindMobileSuccess();
            }
        });
    }

    /**
     * 修改密码
     * @param password
     * @param smsCode
     */
    public void changePassword(String password,String smsCode){
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        map.put("smscode", smsCode);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> mobile = getAddCookieApiService()
                .userAndPwd(modifyPwd,getRequestBody(map));
        getNetData(true,mobile,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.modifyPwdSuccess();
            }
        });
    }

    /**
     * 发送短信验证码
     * @param key
     */
    public void sendSmsCode(String key){
        Map<String,String> map = new HashMap<>();
        if (isEmpty(key)){
            sortAndMD5(map);
            Call<BaseEntity<EmptyEntity>> mobile = getAddCookieApiService()
                    .userAndPwd(sendSmsCode,getRequestBody(map));
            getNetData(true,mobile,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
                @Override
                public void onSuccess(BaseEntity<EmptyEntity> entity) {
                    super.onSuccess(entity);

                }
            });
        }else {
            map.put("key", key);
            sortAndMD5(map);

            Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService()
                    .sendSmsCodeToMobile(getRequestBody(map));
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
                @Override
                public void onSuccess(BaseEntity<CommonEntity> entity) {
                    super.onSuccess(entity);
                    iView.goBindNewMobile(entity.data.mobile, entity.data.key);
                }
            });
        }
    }

}
