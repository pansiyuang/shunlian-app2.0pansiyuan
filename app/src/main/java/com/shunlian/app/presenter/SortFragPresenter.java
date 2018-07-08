package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.SortFragEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISortFragView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/6.
 */

public class SortFragPresenter extends BasePresenter<ISortFragView> {

    private Call<BaseEntity<SortFragEntity>> baseEntityCall;

    public SortFragPresenter(Context context, ISortFragView iView) {
        super(context, iView);
        initApi();
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
        if (baseEntityCall != null)baseEntityCall.cancel();
    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        baseEntityCall = getApiService().categoryAll(map);
        getNetData(true, baseEntityCall,new SimpleNetDataCallback<BaseEntity<SortFragEntity>>(){
            @Override
            public void onSuccess(BaseEntity<SortFragEntity> entity) {
                super.onSuccess(entity);
                iView.categoryAll(entity.data.categoryList);
                iView.setKeyworld(entity.data.search);
            }
        });
    }
}
