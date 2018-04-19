package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AmountDetailEntity;
import com.shunlian.app.bean.BalanceDetailEntity;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IAmountDetail;
import com.shunlian.app.view.IBalanceDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PAmountDetail extends BasePresenter<IAmountDetail> {
    private String id;

    public PAmountDetail(Context context, IAmountDetail iView,String id) {
        super(context, iView);
        this.id=id;
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
        map.put("id", id);
        sortAndMD5(map);

        Call<BaseEntity<AmountDetailEntity>> baseEntityCall = getApiService().amountDetails(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<AmountDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AmountDetailEntity> entity) {
                super.onSuccess(entity);
                AmountDetailEntity amountDetailEntity=entity.data;
                if (amountDetailEntity.list!=null&&amountDetailEntity.list.size()>0)
                    iView.setApiData(amountDetailEntity.list);
            }
        });

    }

}
