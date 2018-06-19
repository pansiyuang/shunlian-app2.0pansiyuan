package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.DiscoveryNavEntity;
import com.shunlian.app.bean.HelpcenterIndexEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IDiscover;
import com.shunlian.app.view.IHelpOneView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PHelpOne extends BasePresenter<IHelpOneView> {
    public PHelpOne(Context context, IHelpOneView iView) {
        super(context, iView);
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<HelpcenterIndexEntity>> baseEntityCall = getApiService().helpcenterIndex(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<HelpcenterIndexEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HelpcenterIndexEntity> entity) {
                super.onSuccess(entity);
                HelpcenterIndexEntity data = entity.data;
                if (data != null) {
                    iView.setApiData(data);
                }
            }
        });
    }

    public void getHelpPhone(){
        Map<String, String> map = new HashMap<>();
        map.put("field", "telephone");
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().getAdminInfo(map);
        getNetData(false,baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    iView.setPhoneNum(data.telephone);
                }
            }
        });
    }

    public void getUserId() {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", "-1");
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().getUserId(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity = entity.data;
                iView.getUserId(commonEntity.user_id);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }
}
