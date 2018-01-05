package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.LogisticsNameEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISelectLogisticsView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/4.
 */

public class SelectLogisticsPresenter extends BasePresenter<ISelectLogisticsView> {

    public SelectLogisticsPresenter(Context context, ISelectLogisticsView iView) {
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

    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<LogisticsNameEntity>> baseEntityCall = getApiService().refundExpressList(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<LogisticsNameEntity>>(){
            @Override
            public void onSuccess(BaseEntity<LogisticsNameEntity> entity) {
                super.onSuccess(entity);
                iView.selectLogistics(entity.data.express_list);
            }
        });
    }
}
