package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ExchangeDetailView;
import com.shunlian.app.view.OrderdetailView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class ExchangeDetailPresenter extends BasePresenter<ExchangeDetailView> {
    private String refund_id;

    public ExchangeDetailPresenter(Context context, ExchangeDetailView iView, String refund_id) {
        super(context, iView);
        this.refund_id = refund_id;
        initApiData();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void initApiData(){
        Map<String, String> map = new HashMap<>();
        map.put("refund_id", refund_id);
        sortAndMD5(map);
        Call<BaseEntity<RefundDetailEntity>> baseEntityCall = getApiService().refundDetail(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<RefundDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<RefundDetailEntity> entity) {
                super.onSuccess(entity);
                RefundDetailEntity data = entity.data;
                iView.setData(data);
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
                iView.confirmReceive();
            }
        });
    }

    @Override
    protected void initApi() {

    }
}
