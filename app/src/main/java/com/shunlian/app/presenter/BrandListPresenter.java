package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IBrandListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/2/28.
 */

public class BrandListPresenter extends BasePresenter<IBrandListView> {

    public BrandListPresenter(Context context, IBrandListView iView) {
        super(context, iView);
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
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<GetListFilterEntity>> brandlist = getApiService().brandlist(map);

        getNetData(true,brandlist,new SimpleNetDataCallback<BaseEntity<GetListFilterEntity>>(){
            @Override
            public void onSuccess(BaseEntity<GetListFilterEntity> entity) {
                super.onSuccess(entity);
                GetListFilterEntity data = entity.data;
                iView.setBrandList(data.first_letter_list,data.brand_list);
            }
        });
    }
}
