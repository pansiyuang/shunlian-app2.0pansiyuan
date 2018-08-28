package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.CommondEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.PunishEntity;
import com.shunlian.app.bean.UpdateEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IMain;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PMain extends BasePresenter<IMain> {
    public PMain(Context context, IMain iView) {
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

    public void entryInfo(){
        Map<String,String> map = new HashMap<>();
//        map.put("word",word);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().entryInfo(requestBody);

        getNetData(false,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    iView.entryInfo(data);
                }
            }
        });
    }

    public void getCommond(String word){
        Map<String,String> map = new HashMap<>();
        map.put("word",word);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommondEntity>> baseEntityCall = getAddCookieApiService().parseSecretWord(requestBody);

        getNetData(false,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommondEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommondEntity> entity) {
                super.onSuccess(entity);
                CommondEntity data = entity.data;
                if (data != null) {
                    iView.setCommond(data);
                }
            }
        });
    }


    public void getSplashAD() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<AdEntity>> baseEntityCall = getApiService().splashScreen(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AdEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AdEntity> entity) {
                super.onSuccess(entity);
                AdEntity data = entity.data;
                if (data != null) {
                    iView.setAD(data);
                }
            }
        });
    }

    public void getDiscoveryUnreadCount() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().getDiscoveryUnreadCount(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    iView.setDiscoveryUnreadCount(data);
                }
            }
        });
    }

    public void getUpdateInfo(String type, String version) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("version", version);
        sortAndMD5(map);

        Call<BaseEntity<UpdateEntity>> baseEntityCall = getApiService().updateappcheck(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<UpdateEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UpdateEntity> entity) {
                super.onSuccess(entity);
                UpdateEntity data = entity.data;
                if (data != null) {
                    iView.setUpdateInfo(data);
                }
            }
        });
    }

    public void getPopAD() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<AdEntity>> baseEntityCall = getApiService().popup(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AdEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AdEntity> entity) {
                super.onSuccess(entity);
                AdEntity data = entity.data;
                if (data != null) {
                    iView.setAD(data);
                }
            }
        });
    }

    public void getMenuData(){
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<GetMenuEntity>> baseEntityCall = getApiService().channelGetMenu(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetMenuEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetMenuEntity> entity) {
                super.onSuccess(entity);
                iView.setTab(entity.data);
            }
        });

    }

    public void getContentData(String id){
        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        sortAndMD5(map);
        Call<BaseEntity<GetDataEntity>> baseEntityCall = getApiService().channelGetData(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetDataEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetDataEntity> entity) {
                super.onSuccess(entity);
                iView.setContent(entity.data);
            }
        });
    }

}
