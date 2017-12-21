package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PayListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPayListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/21.
 */

public class PayListPresenter extends BasePresenter<IPayListView> {

    public PayListPresenter(Context context, IPayListView iView) {
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
        Call<BaseEntity<PayListEntity>> methodlist = getApiService().methodlist(map);
        getNetData(methodlist,new SimpleNetDataCallback<BaseEntity<PayListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<PayListEntity> entity) {
                super.onSuccess(entity);
                iView.payList(entity.data.pay_method);
            }
        });
    }
}
