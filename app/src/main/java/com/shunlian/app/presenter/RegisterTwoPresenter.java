package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RegisterFinishEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.SharedPrefUtil;
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

    public void register(String mobile,String mobile_code,String code,String password,String nickname,String unique_sign){
        Map<String,String> map = new HashMap<>();
        if (!TextUtils.isEmpty(unique_sign)){
            map.put("unique_sign",unique_sign);
        }
        map.put("mobile",mobile);
        map.put("mobile_code",mobile_code);
        map.put("code",code);
        map.put("password",password);
        map.put("nickname",nickname);
        sortAndMD5(map);
        String s = null;
        try {
            s = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),s);
        Call<BaseEntity<RegisterFinishEntity>> register = getApiService().register(requestBody);
        getNetData(register,new SimpleNetDataCallback<BaseEntity<RegisterFinishEntity>>(){
            @Override
            public void onSuccess(BaseEntity<RegisterFinishEntity> entity) {
                super.onSuccess(entity);
                SharedPrefUtil.saveSharedPrfString("token",entity.data.token);
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
