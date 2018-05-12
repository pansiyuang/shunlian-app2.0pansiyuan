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
    /**
     * 获取商家客服Id
     * @param storeId
     */

    public void getUserId(String storeId) {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", storeId);
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
     * 换货确认收货
     * @param refund_id
     */
    public void confirmReceive(String refund_id){
        Map<String,String> map = new HashMap<>();
        map.put("refund_id",refund_id);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().confirmReceive(getRequestBody(map));
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
