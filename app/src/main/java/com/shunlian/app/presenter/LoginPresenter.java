package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ILoginView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class LoginPresenter extends BasePresenter<ILoginView> {
    public static final String TYPE_USER = "username";
    public static final String TYPE_MOBILE = "mobile";
    private String currentType;

    public LoginPresenter(Context context, ILoginView iView, String type) {
        super(context, iView);
        this.currentType = type;
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {
    }

    public void LoginUserName(String userName, String psw) {
        Map<String, String> map = new HashMap<>();
        map.put("type", currentType);
        map.put("username", userName);
        map.put("password", psw);
        sortAndMD5(map);
        LoginAction(map);
    }

    public void LoginMobile(String mobile, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("type", currentType);
        map.put("mobile", mobile);
        map.put("code", code);
        sortAndMD5(map);
        LoginAction(map);
    }

    private void LoginAction(Map map) {
        try {
            String stringEntry = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
            Call<BaseEntity<String>> baseEntityCall = getSaveCookieApiService().login(requestBody);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<String>>() {
                @Override
                public void onSuccess(BaseEntity<String> entity) {
                    super.onSuccess(entity);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
