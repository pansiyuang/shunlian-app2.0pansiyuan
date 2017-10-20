package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class RegisterOnePresenter extends BasePresenter {

    public RegisterOnePresenter(Context context, IView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }

    public void sendSmsCode(String phone,String code){
        long time = System.currentTimeMillis() / 1000;
        Map<String,String> map = new HashMap<>();
        map.put("mobile",phone);
        map.put("vcode",code);
        map.put("timestamp",String.valueOf(time));
        map.put("sign",sortAndMD5(map));
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),s);
            Call<BaseEntity> baseEntityCall = getApiService().sendSmsCode(requestBody);
            getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity>(){
                @Override
                public void onSuccess(BaseEntity entity) {
                    super.onSuccess(entity);
                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
