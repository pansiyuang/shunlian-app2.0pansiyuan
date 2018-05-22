package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.MyCommentListEntity;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ITraceView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/14.
 */

public class OrderLogisticsPresenter extends BasePresenter<ITraceView> {

    public OrderLogisticsPresenter(Context context, ITraceView iView) {
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

    public void orderLogistics(String orderStr) {
        Map<String, String> map = new HashMap<>();
        map.put("order_id", String.valueOf(orderStr));
        map.put("page", String.valueOf(orderStr));
        map.put("page_size", String.valueOf(orderStr));
        sortAndMD5(map);
        Call<BaseEntity<OrderLogisticsEntity>> baseEntityCall = getApiService().orderLogistics(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<OrderLogisticsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<OrderLogisticsEntity> entity) {
                super.onSuccess(entity);
                if (entity.data != null) {
                    iView.getLogistics(entity.data);
                }
            }
        });
    }
}
