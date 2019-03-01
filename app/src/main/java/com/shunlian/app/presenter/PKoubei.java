package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.CommondEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;
import com.shunlian.app.bean.HotsaleEntity;
import com.shunlian.app.bean.HotsaleHomeEntity;
import com.shunlian.app.bean.KoubeiSecondEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IH5View;
import com.shunlian.app.view.IKoubei;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class PKoubei extends BasePresenter<IKoubei> {

    public PKoubei(Context context, IKoubei iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }

    public void getHomedata(){
        Map<String, String> map = new HashMap<>();
//        map.put("id", id);
        sortAndMD5(map);
        Call<BaseEntity<HotsaleHomeEntity>> baseEntityCall = getApiService().hotsaleHome(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<HotsaleHomeEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HotsaleHomeEntity> entity) {
                super.onSuccess(entity);
                HotsaleHomeEntity data = entity.data;
                if (data != null) {
                    iView.setHomeData(data);
                }
            }
        });
    }

    public void getHotsaleCate(String cate2){
        Map<String, String> map = new HashMap<>();
        map.put("cate2", cate2);
        sortAndMD5(map);
        Call<BaseEntity<HotsaleEntity>> baseEntityCall = getApiService().hotsaleCate(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<HotsaleEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HotsaleEntity> entity) {
                super.onSuccess(entity);
                HotsaleEntity data = entity.data;
                if (data != null) {
                    iView.setHotsaleCate(data);
                }
            }
        });
    }

    public void getSetZan(String goods_id){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().hotsalePraise(requestBody);

        getNetData(false,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    iView.getZan(data);
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.showFailureView(-1);
            }

            @Override
            public void onErrorData(BaseEntity<CommonEntity> commonEntityBaseEntity) {
                super.onErrorData(commonEntityBaseEntity);
                iView.showFailureView(-1);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.showFailureView(-1);
            }

        });
    }

    public void getHotsaleCates(String cate3){
        Map<String, String> map = new HashMap<>();
        map.put("cate3", cate3);
//        map.put("cate3", "60");
        sortAndMD5(map);
        Call<BaseEntity<KoubeiSecondEntity>> baseEntityCall = getApiService().hotsaleCates(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<KoubeiSecondEntity>>() {
            @Override
            public void onSuccess(BaseEntity<KoubeiSecondEntity> entity) {
                super.onSuccess(entity);
                KoubeiSecondEntity data = entity.data;
                if (data != null) {
                    iView.setHotsaleCates(data);
                }
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
