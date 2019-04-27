package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GetQrCardEntity;
import com.shunlian.app.bean.PosterEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IHbCode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * Created by Administrator on 2019/3/21.
 */

public class PHbCode extends BasePresenter <IHbCode>{
    private String storeId;
    public PHbCode(Context context, IHbCode iView, String storeId) {
        super(context,  iView);
        this.storeId=storeId;
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
       map.put("broadcast_id", storeId);
        sortAndMD5(map);

        Call<BaseEntity<PosterEntity>> baseEntityCall = getApiService().shareQrcard(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<PosterEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PosterEntity> entity) {
                super.onSuccess(entity);
                PosterEntity data = entity.data;
                if (data != null) {
                    LogUtil.httpLogW("baseEntityCall:" + data);
                    iView.setApiData(data);
                }
            }
        });
    }
}
