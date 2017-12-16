package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IOrderListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/14.
 */

public class OrderListPresenter extends BasePresenter<IOrderListView> {

    public static final int PAGE_SIZE = 10;
    public static final String all = "all";//全部
    public static final String pay = "0";//待支付
    public static final String send = "1";//待发货
    public static final String receive = "2";//待收货
    public static final String comment = "3";//待评价
    private String status = all;
    public static final int LOAD_CODE = 100;//加载更多的code
    public static final int OTHER_CODE = 200;//其他code


    public OrderListPresenter(Context context, IOrderListView iView) {
        super(context, iView);
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
        isLoading = false;
        currentPage = 1;
        allPage = 1;
    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        orderListAll();
    }

    public void orderListAll(){
        status = all;
        orderList(OTHER_CODE,OTHER_CODE,true,status,1);
    }

    public void orderListPay(){
        status = pay;
        orderList(OTHER_CODE,OTHER_CODE,true,status,1);
    }

    public void orderListSend(){
        status = send;
        orderList(OTHER_CODE,OTHER_CODE,true,status,1);
    }

    public void orderListReceive(){
        status = receive;
        orderList(OTHER_CODE,OTHER_CODE,true,status,1);
    }

    public void orderListComment(){
        status = comment;
        orderList(OTHER_CODE,OTHER_CODE,true,status,1);
    }

    public void orderList(int empty, int failure, boolean isShow, String status, final int page){
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("page_size",String.valueOf(PAGE_SIZE));
        map.put("status",String.valueOf(status));
        sortAndMD5(map);
        Call<BaseEntity<MyOrderEntity>> baseEntityCall = getApiService().orderList(map);
        getNetData(empty,failure,isShow,baseEntityCall,new SimpleNetDataCallback<BaseEntity<MyOrderEntity>>(){
            @Override
            public void onSuccess(BaseEntity<MyOrderEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                MyOrderEntity data = entity.data;

                allPage = Integer.parseInt(data.total_page);
                currentPage = Integer.parseInt(data.page);

                iView.orderList(data.orders,currentPage,allPage);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading){
            isLoading = true;
            if (currentPage <= allPage){
                currentPage ++;
                orderList(LOAD_CODE,LOAD_CODE,false,status,currentPage);
            }
        }
    }
}
