package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IChatOrderView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/14.
 */

public class ChatOrderPresenter extends BasePresenter<IChatOrderView> {
    public static final int PAGE_SIZE = 20;

    public ChatOrderPresenter(Context context, IChatOrderView iView) {
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


    public void initPage() {
        currentPage = 1;
    }

    public void getOrderList(boolean isLoad) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        map.put("status", "1-2-3");
        sortAndMD5(map);
        Call<BaseEntity<MyOrderEntity>> baseEntityCall = getApiService().orderList(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<MyOrderEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MyOrderEntity> entity) {
                super.onSuccess(entity);
                MyOrderEntity orderEntity = entity.data;

                currentPage = Integer.parseInt(orderEntity.page);
                allPage = Integer.parseInt(orderEntity.total_page);

                if (!isEmpty(orderEntity.orders)) {
                    iView.getOrderList(orderEntity.orders,currentPage,allPage);
                }

                if (currentPage == 1) {
                    iView.refreshFinish();
                }
                currentPage++;
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
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getOrderList(false);
            }
        }
    }
}
