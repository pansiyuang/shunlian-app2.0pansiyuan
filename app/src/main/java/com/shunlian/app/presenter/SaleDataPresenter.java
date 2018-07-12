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
    private Call<BaseEntity<SaleDataEntity>> salesdata;
    private Call<BaseEntity<SalesChartEntity>> salesChartCall;

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
        if (salesdata != null)salesdata.cancel();
        if (salesChartCall != null)salesChartCall.cancel();
    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);

        salesdata = getApiService().salesdata(map);
        getNetData(true, salesdata,new SimpleNetDataCallback<BaseEntity<SaleDataEntity>>(){
            @Override
            public void onSuccess(BaseEntity<SaleDataEntity> entity) {
                super.onSuccess(entity);

                SaleDataEntity data = entity.data;
                iView.setGrowthValue_RequestCode(data.user_info.grow_num,data.user_info.invite_code);
                iView.setHeadNickname(data.user_info.avatar,data.user_info.nickname);
                iView.setMonthVip_Order(data.sales_info.month_sales,
                        data.sales_info.today_members,data.sales_info.today_orders);
                iView.setEliteTutorData(data.master_info_new,data.user_info.member_role_code);
                iView.setplusrole(data.user_info.plus_role_code);
                iView.setSalePS(data.tip);
                iView.setSaleTip(data.user_info.grow_infos);
                /*List<String> list = new ArrayList<>();
                list.add("恭喜您已连续9个月成为精英本月如果再完成精英目标成长值将整体上浮5");
                list.add("恭喜您已获得本月成长值1.5倍的增长机会，若本月小店销售大于3000即可转获本月成长值2倍的增长机会");
                iView.setSaleTip(list);*/
            }
        });
    }


    public void salesChart(String path_name,String block){
        Map<String,String> map = new HashMap<>();
        map.put("block",block);
        sortAndMD5(map);

        salesChartCall = getAddCookieApiService()
                .salesChart(path_name,getRequestBody(map));

        getNetData(salesChartCall,new SimpleNetDataCallback<BaseEntity<SalesChartEntity>>(){
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
