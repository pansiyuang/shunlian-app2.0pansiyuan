package com.shunlian.app.wxapi;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.WXLoginEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/24.
 */

public class WXEntryPresenter extends BasePresenter<WXEntryView>{

    public WXEntryPresenter(Context context, WXEntryView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }

    public void wxLogin(String code){
        Map<String,String> map = new HashMap<>();
        map.put("code",code);
        sortAndMD5(map);
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),json);
        final Call<BaseEntity<WXLoginEntity>> baseEntityCall = getSaveCookieApiService().wxLogin(requestBody);

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<WXLoginEntity>>(){
            @Override
            public void onSuccess(BaseEntity<WXLoginEntity> entity) {
                super.onSuccess(entity);
                iView.onWXCallback(entity.data);
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
