package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
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

}
