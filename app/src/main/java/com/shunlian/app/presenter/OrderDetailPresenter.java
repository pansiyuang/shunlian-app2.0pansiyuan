package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.OrderdetailView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class OrderDetailPresenter extends BasePresenter<OrderdetailView> {
    private String order_id;

    public OrderDetailPresenter(Context context, OrderdetailView iView, String order_id) {
        super(context, iView);
        this.order_id = order_id;
        initApiData();
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

    public void initApiData(){
        Map<String, String> map = new HashMap<>();
        map.put("order_id", order_id);
        sortAndMD5(map);
        Call<BaseEntity<OrderdetailEntity>> baseEntityCall = getApiService().orderdetail(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<OrderdetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<OrderdetailEntity> entity) {
                super.onSuccess(entity);
                OrderdetailEntity data = entity.data;
                iView.setOrder(data);
            }
        });
    }
    /**
     * 取消订单
     */
    public void cancleOrder(String order_id,int reason){
        Map<String,String> map = new HashMap<>();
        map.put("order_id",order_id);
        map.put("reason",String.valueOf(reason));
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().cancleOrder(getRequestBody(map));
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null){
                    Common.staticToast(data.message);
                }
                initApi();
            }
        });
    }

    /**
     * 提醒发货
     * @param order_id
     */
    public void remindseller(String order_id){
        Map<String,String> map = new HashMap<>();
        map.put("order_id",order_id);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().remindseller(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null){
                    Common.staticToast(data.message);
                }
            }
        });
    }

    /**
     * 延长收货
     * @param order_id
     */
    public void postpone(String order_id){
        Map<String,String> map = new HashMap<>();
        map.put("order_id",order_id);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().postpone(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null){
                    Common.staticToast(data.message);
                }
                initApi();
            }
        });
    }

    /**
     * 确认收货
     * @param order_id
     */
    public void confirmreceipt(String order_id){
        Map<String,String> map = new HashMap<>();
        map.put("order_id",order_id);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().confirmreceipt(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null){
                    Common.staticToast(data.message);
                }
                initApi();
            }
        });
    }
}
