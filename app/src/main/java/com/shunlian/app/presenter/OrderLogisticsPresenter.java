package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.MyCommentListEntity;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ITraceView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/14.
 */

public class OrderLogisticsPresenter extends BasePresenter<ITraceView> {

    public static final int PAGE_SIZE = 20;
    private String currentOrderStr, currentOrderId, currentOrderType;

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

    public void resetCurrentPage() {
        currentPage = 1;
    }

    public void orderLogistics(boolean isFirst, String orderStr) {
        this.currentOrderStr = orderStr;
        Map<String, String> map = new HashMap<>();
        map.put("order_id", String.valueOf(currentOrderStr));
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<OrderLogisticsEntity>> baseEntityCall = getApiService().orderLogistics(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<OrderLogisticsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<OrderLogisticsEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                OrderLogisticsEntity.History history = entity.data.history;
                iView.getLogistics(entity.data, history.page, history.total_page);
                currentPage = history.page;
                allPage = history.total_page;
                currentPage++;
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void getPlusLogistics(boolean isFirst, String queryId, String type) {
        //type 类型 1是普通订单物流 2退换货物流用户 3退换货商家 4礼包
        this.currentOrderId = queryId;
        this.currentOrderType = type;
        Map<String, String> map = new HashMap<>();
        map.put("type", currentOrderType);
        map.put("query_id", currentOrderId);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<OrderLogisticsEntity>> baseEntityCall = getAddCookieApiService().getOppositeTraces(getRequestBody(map));
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<OrderLogisticsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<OrderLogisticsEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                OrderLogisticsEntity.History history = entity.data.history;
                iView.getLogistics(entity.data, history.page, history.total_page);
                currentPage = history.page;
                allPage = history.total_page;
                currentPage++;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                if (!isEmpty(currentOrderStr)) {
                    orderLogistics(false, currentOrderStr);
                } else {
                    getPlusLogistics(false, currentOrderId, currentOrderType);
                }
            }
        }
    }
}
