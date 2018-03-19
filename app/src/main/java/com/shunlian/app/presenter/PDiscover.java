package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DiscoveryNavEntity;
import com.shunlian.app.bean.GetQrCardEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IDiscover;
import com.shunlian.app.view.IQrCode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PDiscover extends BasePresenter<IDiscover> {
    public PDiscover(Context context, IDiscover iView) {
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

        Call<BaseEntity<DiscoveryNavEntity>> baseEntityCall = getApiService().discoveryNav(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<DiscoveryNavEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DiscoveryNavEntity> entity) {
                super.onSuccess(entity);
                DiscoveryNavEntity data = entity.data;
                if (data != null) {
                    iView.setNavData(data);
                }
            }
        });
    }

    public void initData(){
        initApi();
    }
}
