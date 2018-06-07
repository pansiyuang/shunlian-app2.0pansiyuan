package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AmountDetailEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IAmountDetail;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PAmountDetail extends BasePresenter<IAmountDetail> {
    private String id;

    public PAmountDetail(Context context, IAmountDetail iView, String id) {
        super(context, iView);
        this.id = id;
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        sortAndMD5(map);

        Call<BaseEntity<AmountDetailEntity>> baseEntityCall = getApiService().amountDetails(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AmountDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AmountDetailEntity> entity) {
                super.onSuccess(entity);
                AmountDetailEntity amountDetailEntity = entity.data;
                if (amountDetailEntity.list != null && amountDetailEntity.list.size() > 0)
                    iView.setApiData(amountDetailEntity.list);
            }
        });
    }

    public void getDatas() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        sortAndMD5(map);

        Call<BaseEntity<AmountDetailEntity>> baseEntityCall = getApiService().withdrawLogDetail(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AmountDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AmountDetailEntity> entity) {
                super.onSuccess(entity);
                AmountDetailEntity amountDetailEntity = entity.data;
                if (!isEmpty(amountDetailEntity.amount_label)&&!isEmpty(amountDetailEntity.amount)){
                    AmountDetailEntity.Content content=new AmountDetailEntity.Content();
                    content.name=amountDetailEntity.amount_label;
                    content.value=amountDetailEntity.amount;
                    amountDetailEntity.list.add(0,content);
                    iView.setApiData(amountDetailEntity.list);
                }
            }
        });
    }

    @Override
    protected void initApi() {

    }

}
