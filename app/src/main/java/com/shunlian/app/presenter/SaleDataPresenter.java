package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.SaleDataEntity;
import com.shunlian.app.bean.SalesChartEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISaleDataView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/12.
 */

public class SaleDataPresenter extends BasePresenter<ISaleDataView> {
    public final String salesChart = "salesChart";//销售数据
    public final String ordersChart = "ordersChart";//订单数据
    public final String membersChart = "membersChart";//会员数据
    public final String _7day = "7";
    public final String _30day = "30";
    public final String _60day = "60";

    public SaleDataPresenter(Context context, ISaleDataView iView) {
        super(context, iView);
        initApi();
        salesChart(salesChart,_7day);
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

        Call<BaseEntity<SaleDataEntity>> salesdata = getApiService().salesdata(map);
        getNetData(true,salesdata,new SimpleNetDataCallback<BaseEntity<SaleDataEntity>>(){
            @Override
            public void onSuccess(BaseEntity<SaleDataEntity> entity) {
                super.onSuccess(entity);

                SaleDataEntity data = entity.data;
                iView.setGrowthValue_RequestCode(data.user_info.grow_num,data.user_info.invite_code);
                iView.setHeadNickname(data.user_info.avatar,data.user_info.nickname);
                iView.setMonthVip_Order(data.sales_info.month_sales,
                        data.sales_info.today_members,data.sales_info.today_orders);
                iView.setEliteTutorData(data.master_info);
                iView.setplusrole(data.user_info.plus_role_code);
            }
        });
    }


    public void salesChart(String path_name,String block){
        Map<String,String> map = new HashMap<>();
        map.put("block",block);
        sortAndMD5(map);

        Call<BaseEntity<SalesChartEntity>> baseEntityCall = getAddCookieApiService()
                .salesChart(path_name,getRequestBody(map));

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<SalesChartEntity>>(){
            @Override
            public void onSuccess(BaseEntity<SalesChartEntity> entity) {
                super.onSuccess(entity);
                SalesChartEntity data = entity.data;
                iView.setSaleData(data.total_child_store,
                        data.total_grand_child_store,data.total_consume);

                iView.setSaleChart(data);
            }
        });
    }

    /**
     * 分发网络请求接口
     * @param pos
     * @param day
     */
    public void handleRequest(int pos,String day){
        switch (pos){
            case 0:
                salesChart(salesChart,day);
                break;
            case 1:
                salesChart(ordersChart,day);
                break;
            case 2:
                salesChart(membersChart,day);
                break;
        }
    }
}