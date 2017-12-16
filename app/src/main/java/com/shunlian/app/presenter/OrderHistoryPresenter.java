package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.bean.TagEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ITagView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/16.
 */

public class OrderHistoryPresenter extends BasePresenter<ITagView> {

    public OrderHistoryPresenter(Context context, ITagView iView) {
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

    public void getOrderHistory() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<TagEntity>> baseEntityCall = getApiService().searchHistory(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<TagEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TagEntity> entity) {
                super.onSuccess(entity);
                if (entity.data != null) {
                    iView.getOrderHistory(entity.data);
                }
            }
        });
    }

    public void delHistory() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().delHistory(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                if (entity.code == 1000) {
                    iView.delSuccess();
                } else {
                    iView.delFail(entity.message);
                }
            }
        });
    }
}
