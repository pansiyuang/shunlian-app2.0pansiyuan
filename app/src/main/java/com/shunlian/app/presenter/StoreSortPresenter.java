package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.StoreIntroduceView;
import com.shunlian.app.view.StoreSortView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class StoreSortPresenter extends BasePresenter<StoreSortView> {
    private String storeId;

    public StoreSortPresenter(Context context, StoreSortView iView, String storeId) {
        super(context, iView);
        this.storeId=storeId;
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
        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<StoreCategoriesEntity>> baseEntityCall = getApiService().storeCategories(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreCategoriesEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreCategoriesEntity> entity) {
                super.onSuccess(entity);
                StoreCategoriesEntity data = entity.data;
                if (data != null) {
                    LogUtil.httpLogW("StoreCategoriesEntity:" + data);
                    iView.introduceInfo(data);
                }
            }
        });
    }

}
