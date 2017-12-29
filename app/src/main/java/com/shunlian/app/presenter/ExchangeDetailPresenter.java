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
}
