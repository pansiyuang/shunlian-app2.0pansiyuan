package com.shunlian.app.demo.demo1;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.BasePresenter;
import com.shunlian.app.view.IAishang;
import com.shunlian.app.view.IView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2019/4/1.
 */

public class DemoPAishang extends BasePresenter<IAishang> {
    public DemoPAishang(Context context, IAishang iView) {
        super(context, iView);
    }
    public void getCorePing() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "dp");
        sortAndMD5(map);

        Call<BaseEntity<CorePingEntity>> baseEntityCall = getApiService().corePing(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CorePingEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CorePingEntity> entity) {
                super.onSuccess(entity);
                iView.setPingData(entity.data);
            }
        });
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
}
