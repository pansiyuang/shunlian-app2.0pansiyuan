package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.LuckDrawEntity;
import com.shunlian.app.bean.TurnTableEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ITurnTableView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/8/31.
 */

public class TurnTablePresenter extends BasePresenter<ITurnTableView> {

    public TurnTablePresenter(Context context, ITurnTableView iView) {
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

    public void getTurnTableData() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<TurnTableEntity>> baseEntityCall = getApiService().getTurnTable(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<TurnTableEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TurnTableEntity> entity) {
                TurnTableEntity turnTableEntity = entity.data;
                iView.getTurnData(turnTableEntity);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void luckDrawData() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<LuckDrawEntity>> baseEntityCall = getApiService().luckDraw(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<LuckDrawEntity>>() {
            @Override
            public void onSuccess(BaseEntity<LuckDrawEntity> entity) {
                LuckDrawEntity luckDrawEntity = entity.data;
                iView.getLuckDraw(luckDrawEntity);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }
}
