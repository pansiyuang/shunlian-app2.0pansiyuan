package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IPlusLogisticsView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/28.
 */

public class PlusLogisticsPresenter extends BasePresenter<IPlusLogisticsView> {

    public PlusLogisticsPresenter(Context context, IPlusLogisticsView iView) {
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

    public void getPlusLogistics(String queryId) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "4");
        map.put("query_id", queryId);
        sortAndMD5(map);
        Call<BaseEntity<OrderLogisticsEntity>> baseEntityCall = getAddCookieApiService().getOppositeTraces(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<OrderLogisticsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<OrderLogisticsEntity> entity) {
                super.onSuccess(entity);
                iView.getLogistics(entity.data);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }
}
