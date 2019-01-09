package com.shunlian.app.ui.new3_login;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.BasePresenter;
import com.shunlian.app.utils.Common;

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
     * 图形验证码验证
     * @param picCode
     */
    public void checkPictureCode(String mobile,String picCode){
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("vcode",picCode);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().checkCode(getRequestBody(map));
        getNetData(baseEntityCall,new
                SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
                    @Override
                    public void onSuccess(BaseEntity<EmptyEntity> entity) {
                        super.onSuccess(entity);
                        iView.checkPictureCode(null);
                    }

                    @Override
                    public void onErrorCode(int code, String message) {
                        super.onErrorCode(code, message);
                        iView.checkPictureCode(message);
                    }
                });
    }

    /**
     * 登录界面提示信息
     */
    public void loginInfoTip(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<New3LoginInfoTipEntity>> baseEntityCall = getApiService().loginInfoTip(map);
        getNetData(false,baseEntityCall,new
                SimpleNetDataCallback<BaseEntity<New3LoginInfoTipEntity>>(){
                    @Override
                    public void onSuccess(BaseEntity<New3LoginInfoTipEntity> entity) {
                        super.onSuccess(entity);
                        iView.setLoginInfoTip(entity.data);
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
     *
     * @param mobile 手机号
     * @param mobile_code 短信验证码
     * @param code 推荐人id
     * @param unique_sign 微信登录code
     */
    public void register(String mobile,String mobile_code,String code,String unique_sign){
        Map<String,String> map = new HashMap<>();
        if (!TextUtils.isEmpty(unique_sign)){
            map.put("unique_sign",unique_sign);
        }
        map.put("mobile",mobile);
        map.put("mobile_code",mobile_code);
        map.put("code",code);
        sortAndMD5(map);

        Call<BaseEntity<LoginFinishEntity>>
                register = getSaveCookieApiService().new_register(getRequestBody(map));
        getNetData(true,register, new SimpleNetDataCallback<BaseEntity<LoginFinishEntity>>() {
            @Override
            public void onSuccess(BaseEntity<LoginFinishEntity> entity) {
                super.onSuccess(entity);
                iView.loginMobileSuccess(entity.data);
            }
        });
    }

    /**
     * 邀请码详情
     * @param id
     */
    public void codeDetail(String id){
        Map<String,String> map = new HashMap<>();
        map.put("code",id);
        sortAndMD5(map);

        Call<BaseEntity<MemberCodeListEntity>>
                baseEntityCall = getApiService().codeInfo(map);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback
                <BaseEntity<MemberCodeListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<MemberCodeListEntity> entity) {
                super.onSuccess(entity);
                iView.codeInfo(entity.data,null);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.codeInfo(null,message);
            }
        });
    }

    /**
     * 检验短信验证码
     * @param mobile
     * @param smsCode
     */
    public void checkSmsCode(String mobile,String smsCode){
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("mobile_code", smsCode);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>>
                requestBodyCall = getSaveCookieApiService().checkNew3LoginSmsCode(getRequestBody(map));

        getNetData(true,requestBodyCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.checkSmsCode(0,null);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                int state = 2;
                if (code == 1003){//1003代表要弹出图形验证码
                    state = 1;
                }
                iView.checkSmsCode(state,message);
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
                iView.accountPwdSuccess(entity.data);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                if (code == 1003){
                    iView.accountPwdSuccess(null);
                }
            }
        });
    }


    /**
     * 登录时检验手机号是否注册，仅用手机验证码登录
     */
    public void checkMobile(String mobile){
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        sortAndMD5(map);
        Call<BaseEntity<New3LoginEntity>> baseEntityCall = getApiService().checkMobileNews(map);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<New3LoginEntity>>(){
            @Override
            public void onSuccess(BaseEntity<New3LoginEntity> entity) {
                super.onSuccess(entity);
                iView.iSMobileRight(true,entity.data.status,entity.data.shareid);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.iSMobileRight(false,message,null);
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
                iView.smsCode(0,null);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                int isShow = 2;
                if (code == 1003)isShow = 1;
                iView.smsCode(isShow,message);
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


    public void newRegister(String mobile,String mobile_code,String code,String unique_sign){
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("mobile_code", mobile_code);
        if (!isEmpty(unique_sign))
        map.put("unique_sign", unique_sign);
        map.put("code", code);
        sortAndMD5(map);

        Call<BaseEntity<LoginFinishEntity>> baseEntityCall
                = getSaveCookieApiService().newRegister(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<LoginFinishEntity>>(){
            @Override
            public void onSuccess(BaseEntity<LoginFinishEntity> entity) {
                super.onSuccess(entity);
                iView.loginMobileSuccess(entity.data);
            }
        });
    }



    public void checkFromWXMobile(String status,String mobile){
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        if (!isEmpty(status))
        map.put("status", status);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>>
                baseEntityCall = getAddCookieApiService().checkFromWXMobile(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.checkFromWXMobile(entity.data.status,
                        entity.data.share_show_status,entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.checkFromWXMobile("","",message);
            }
        });
    }


    public void bindShareid(String code){
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>>
                baseEntityCall = getAddCookieApiService().bindShareidV2(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.bindShareID("");
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.bindShareID(message);
            }
        });
    }
}
