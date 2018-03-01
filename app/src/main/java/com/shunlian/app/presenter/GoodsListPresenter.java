package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AddGoodsEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IGoodsListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/2/27.
 */

public class GoodsListPresenter extends BasePresenter<IGoodsListView> {
    public static final int PAGE_SIZE = 20;
    private String fromStr;

    public GoodsListPresenter(Context context, IGoodsListView iView) {
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

    public void getValidGoods(String from, boolean isShowLoading) {
        fromStr = from;
        Map<String, String> map = new HashMap<>();
        map.put("from", from);
        if (isShowLoading) {
            currentPage = 1;
        }
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<AddGoodsEntity>> baseEntityCall = getApiService().validGoods(map);
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AddGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AddGoodsEntity> entity) {
                super.onSuccess(entity);
                AddGoodsEntity addGoodsEntity = entity.data;

                isLoading = false;
                iView.getValidGoods(addGoodsEntity.list, Integer.valueOf(addGoodsEntity.page), Integer.valueOf(addGoodsEntity.total_page));
                currentPage = Integer.parseInt(entity.data.page);
                allPage = Integer.parseInt(entity.data.total_page);
                currentPage++;
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getValidGoods(fromStr, false);
            }
        }
    }
}
