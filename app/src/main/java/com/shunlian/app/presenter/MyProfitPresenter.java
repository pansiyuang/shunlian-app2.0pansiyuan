package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.MyProfitEntity;
import com.shunlian.app.bean.SalesChartEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IMyProfitView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MyProfitPresenter extends BasePresenter<IMyProfitView> {

    public final String _7day = "7";
    public final String _30day = "30";
    public final String _60day = "60";

    public MyProfitPresenter(Context context, IMyProfitView iView) {
        super(context, iView);
        initApi();
        profitCharts(_7day);
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<MyProfitEntity>> generalprofit = getAddCookieApiService()
                .generalprofit(getRequestBody(map));

        getNetData(true,generalprofit,new SimpleNetDataCallback<BaseEntity<MyProfitEntity>>(){
            @Override
            public void onSuccess(BaseEntity<MyProfitEntity> entity) {
                super.onSuccess(entity);
                MyProfitEntity data = entity.data;
                iView.setUserInfo(data.user_info);
                iView.setProfitInfo(data.profit_info);
                iView.setProfitTip(data.tip);
            }
        });
    }

    public void profitCharts(String block){
        Map<String,String> map = new HashMap<>();
        map.put("block",block);
        sortAndMD5(map);

        Call<BaseEntity<SalesChartEntity>> baseEntityCall = getAddCookieApiService()
                .profitChart(getRequestBody(map));

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<SalesChartEntity>>(){
            @Override
            public void onSuccess(BaseEntity<SalesChartEntity> entity) {
                super.onSuccess(entity);
                iView.setProfitCharts(entity.data);
            }
        });
    }

    public void receiveReward(String type){
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().receiveReward(map);
        getNetData(true,baseEntityCall,new
                SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.receiveReward(type);
            }
        });
    }
}
