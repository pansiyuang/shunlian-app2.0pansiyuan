package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.shunlian.app.adapter.NearAddrAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.NearAddrEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.INearAddressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/10/17.
 */

public class NearAddressPresenter extends BasePresenter<INearAddressView> {

    private List<NearAddrEntity.NearAddr> nearAddrList = new ArrayList<>();
    private NearAddrAdapter adapter;

    public NearAddressPresenter(Context context, INearAddressView iView) {
        super(context, iView);
        initApi();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        getNearAddr(null);
    }

    public void getNearAddr(String key_word){
        Map<String,String> map = new HashMap<>();
        if (isEmpty(key_word)){
            Location location = Common.getGPS((Activity) context);
            if (location != null) {
                map.put("lng",String.valueOf(location.getLongitude()));
                map.put("lat",String.valueOf(location.getLatitude()));
            }
        }else {
            map.put("key_word",key_word);
        }
        Call<BaseEntity<NearAddrEntity>>
                relatedPlaces = getAddCookieApiService().getRelatedPlaces(getRequestBody(map));
        getNetData(true,relatedPlaces,new SimpleNetDataCallback<BaseEntity<NearAddrEntity>>(){
            @Override
            public void onSuccess(BaseEntity<NearAddrEntity> entity) {
                super.onSuccess(entity);

                setAdapterData(entity.data);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });
    }

    private void setAdapterData(NearAddrEntity data) {
        nearAddrList.clear();
        nearAddrList.addAll(data.list);
        if (adapter == null) {
            adapter = new NearAddrAdapter(context, nearAddrList);
            if (iView != null)
                iView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
}
