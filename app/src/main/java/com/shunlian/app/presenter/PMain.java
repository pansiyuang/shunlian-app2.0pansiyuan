package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.UpdateEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IMain;

import java.util.HashMap;
import java.util.Map;

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
                iView.setUpdateInfo(null);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.setUpdateInfo(null);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.setUpdateInfo(null);
            }
        });
    }

}
