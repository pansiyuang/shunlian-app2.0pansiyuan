package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISearchResultView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/16.
 */

public class SearchOrderResultPresent extends BasePresenter<ISearchResultView> {

    public SearchOrderResultPresent(Context context, ISearchResultView iView) {
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

    public void searchOrder(String page, String pageSize, String status, String keyword) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page);
        map.put("page_size", pageSize);
        map.put("status", status);
        map.put("key_word", keyword);
        sortAndMD5(map);
        Call<BaseEntity<MyOrderEntity>> baseEntityCall = getApiService().orderList(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<MyOrderEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MyOrderEntity> entity) {
                super.onSuccess(entity);
                iView.getSearchResult(entity.data);
            }
        });
    }
}
