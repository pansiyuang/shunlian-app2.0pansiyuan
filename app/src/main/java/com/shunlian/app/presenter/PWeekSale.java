package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.WeekSaleTopEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IChosenView;
import com.shunlian.app.view.IWeekSale;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/14.
 */

public class PWeekSale extends BasePresenter<IWeekSale> {

    public PWeekSale(Context context, IWeekSale iView) {
        super(context, iView);
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
        sortAndMD5(map);
        Call<BaseEntity<WeekSaleTopEntity>> baseEntityCall = getApiService().weekSaleTop(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<WeekSaleTopEntity>>() {
            @Override
            public void onSuccess(BaseEntity<WeekSaleTopEntity> entity) {
                super.onSuccess(entity);
                iView.setApiData(entity.data);
            }
            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.showDataEmptyView(0);
            }
        });
    }

}
