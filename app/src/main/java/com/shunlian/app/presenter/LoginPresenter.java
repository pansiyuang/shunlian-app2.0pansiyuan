package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.view.ILoginView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        long time = System.currentTimeMillis() / 1000;
        Map<String, String> map = new HashMap<>();
        map.put("type", currentType);
        map.put("username", userName);
        map.put("password", psw);
        map.put("timestamp", String.valueOf(time));
        map.put("sign", sortAndMD5(map));
        LoginAction(map);
    }

    public void LoginMobile(String mobile, String code) {
        long time = System.currentTimeMillis() / 1000;
        Map<String, String> map = new HashMap<>();
        map.put("type", currentType);
        map.put("mobile", mobile);
        map.put("code", code);
        map.put("timestamp", String.valueOf(time));
        map.put("sign", sortAndMD5(map));
        LoginAction(map);
    }

    private void LoginAction(Map map) {
        Call<ResponseBody> baseEntityCall = getApiService().memberCodeList(map);
        baseEntityCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
