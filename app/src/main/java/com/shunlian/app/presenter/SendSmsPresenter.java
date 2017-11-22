package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ISendSmsView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/20.
 */

public class SendSmsPresenter extends BasePresenter<ISendSmsView> {

    public SendSmsPresenter(Context context, ISendSmsView iView) {
        super(context, iView);
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

    public void checkCode(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        sortAndMD5(map);
        try {
            String stringEntry = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
            Call<ResponseBody> baseEntityCall = getApiService().checkCode(requestBody);
            baseEntityCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        LogUtil.httpLogW("response:" + response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public void sendSms(String mobile, String vcode) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("vcode", vcode);
        sortAndMD5(map);
        try {
            String stringEntry = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
            Call<ResponseBody> baseEntityCall = getApiService().sendSms(requestBody);
            baseEntityCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        LogUtil.httpLogW("response:" + response.body().string());
                        Common.staticToast(response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
