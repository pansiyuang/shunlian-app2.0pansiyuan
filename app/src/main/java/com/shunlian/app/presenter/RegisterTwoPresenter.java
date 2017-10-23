package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class RegisterTwoPresenter extends BasePresenter {

    public RegisterTwoPresenter(Context context, IView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }

    public void register(String mobile,String mobile_code,String code,String password,String nickname){
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("mobile_code",mobile_code);
        map.put("code",code);
        map.put("password",password);
        map.put("nickname",nickname);
        map.put("sign",sortAndMD5(map));
        String s = null;
        try {
            s = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),s);
        Call<BaseEntity<String>> register = getApiService().register(requestBody);
        getNetData(register,new SimpleNetDataCallback<BaseEntity<String>>(){
            @Override
            public void onSuccess(BaseEntity<String> entity) {
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
