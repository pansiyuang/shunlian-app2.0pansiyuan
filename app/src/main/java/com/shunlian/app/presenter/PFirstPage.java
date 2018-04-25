package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.GetRealInfoEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IAlipayDetail;
import com.shunlian.app.view.IFirstPage;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PFirstPage extends BasePresenter<IFirstPage> {

    public PFirstPage(Context context, IFirstPage iView) {
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
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetDataEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetDataEntity> entity) {
                super.onSuccess(entity);
                LogUtil.augusLogW("yf--"+id);
                iView.setContent(entity.data);
            }
        });
    }

}
