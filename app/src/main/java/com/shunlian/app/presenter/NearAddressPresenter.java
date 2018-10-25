package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

import com.shunlian.app.adapter.NearAddrAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.NearAddrEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/10/17.
 */

public class NearAddressPresenter extends BasePresenter {

    private List<NearAddrEntity.NearAddr> nearAddrList = new ArrayList<>();
    private NearAddrAdapter adapter;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NearAddrEntity.NearAddr na = nearAddrList.get(msg.what);
            Intent intent = new Intent();
            if (msg.what == 0){
                intent.putExtra("name","");
            }else {
                intent.putExtra("name",na.name);
                intent.putExtra("addr",na.addr);
            }
            ((Activity)context).setResult(Activity.RESULT_OK,intent);
            ((Activity)context).finish();
        }
    };
    private Call<BaseEntity<NearAddrEntity>> relatedPlaces;

    public NearAddressPresenter(Context context, IView iView) {
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
        if (nearAddrList != null) {
            nearAddrList.clear();
            nearAddrList = null;
        }
        if (adapter != null){
            adapter.unbind();
            adapter = null;
        }
        if (relatedPlaces != null && relatedPlaces.isExecuted()){
            relatedPlaces.cancel();
        }
        relatedPlaces=null;
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
        if (!isEmpty(key_word)){
            map.put("key_word",key_word);
        }
        Location location = Common.getGPS((Activity) context);
        if (location != null) {
            map.put("lng",String.valueOf(location.getLongitude()));
            map.put("lat",String.valueOf(location.getLatitude()));
        }
        sortAndMD5(map);
        relatedPlaces = getAddCookieApiService().getRelatedPlaces(getRequestBody(map));
        getNetData(true, relatedPlaces,new SimpleNetDataCallback<BaseEntity<NearAddrEntity>>(){
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
        NearAddrEntity.NearAddr nearAddr = new NearAddrEntity.NearAddr();
        nearAddr.name = "不显示位置";
        nearAddrList.add(nearAddr);
        nearAddrList.addAll(data.list);
        if (adapter == null) {
            adapter = new NearAddrAdapter(context, nearAddrList);
            adapter.setOnItemClickListener((view, position) -> {
                adapter.item_id = position;
                adapter.notifyDataSetChanged();
                mHandler.sendEmptyMessageDelayed(position,400);
            });
            if (iView != null)
                iView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
}
