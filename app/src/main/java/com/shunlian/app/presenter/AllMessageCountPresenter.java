package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMessageCountView;
import com.shunlian.app.view.IView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/8.
 */

public class AllMessageCountPresenter extends BasePresenter<IMessageCountView> {
    private Call<BaseEntity<AllMessageCountEntity>> baseEntityCall;

    public AllMessageCountPresenter(Context context, IMessageCountView iView) {
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

    public void messageAllCount() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<AllMessageCountEntity>> baseEntityCall = getAddCookieApiService().messageAllCount(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AllMessageCountEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AllMessageCountEntity> entity) {
                super.onSuccess(entity);
                AllMessageCountEntity messageCountEntity = entity.data;
                iView.getMessageCount(messageCountEntity);
            }

            @Override
            public void onErrorCode(int code, String message) {
                iView.getMessageCountFail();
                super.onErrorCode(code, message);
            }
        });
    }

    public void cancelRequest() {
        if (baseEntityCall != null && !baseEntityCall.isCanceled()) {
            baseEntityCall.cancel();
        }
    }
}
