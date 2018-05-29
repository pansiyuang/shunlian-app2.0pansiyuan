package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PlusOrderEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPlusOrderView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/28.
 */

public class PlusOrderPresenter extends BasePresenter<IPlusOrderView> {

    public static final int PAGE_SIZE = 20;
    private String currentType;

    public PlusOrderPresenter(Context context, IPlusOrderView iView) {
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

    public void getOrderList(String type, boolean isFirst) {
        this.currentType = type;
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<PlusOrderEntity>> orderList = getApiService().getPlusOrderList(getRequestBody(map));

        getNetData(isFirst, orderList, new SimpleNetDataCallback<BaseEntity<PlusOrderEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PlusOrderEntity> entity) {
                super.onSuccess(entity);
                PlusOrderEntity data = entity.data;
                PlusOrderEntity.Pager pager = data.pager;
                iView.getPlusOrderList(pager.page, pager.total_page, data.list);
            }
        });
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getOrderList(currentType, false);
            }
        }
    }
}
