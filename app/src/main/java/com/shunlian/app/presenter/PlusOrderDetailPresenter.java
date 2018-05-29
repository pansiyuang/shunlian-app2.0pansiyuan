package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.confirm_order.SearchOrderResultActivity;
import com.shunlian.app.ui.order.AllFrag;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IPlusOrderdetailView;
import com.shunlian.app.view.OrderdetailView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PlusOrderDetailPresenter extends BasePresenter<IPlusOrderdetailView> {
    private String plus_order_id;

    public PlusOrderDetailPresenter(Context context, IPlusOrderdetailView iView, String plus_order_id) {
        super(context, iView);
        this.plus_order_id = plus_order_id;
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
        map.put("plus_order_id", plus_order_id);
        sortAndMD5(map);
        Call<BaseEntity<OrderdetailEntity>> baseEntityCall = getApiService().plusorderdetail(map);
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
