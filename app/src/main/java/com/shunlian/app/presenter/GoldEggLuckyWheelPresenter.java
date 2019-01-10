package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DrawRecordEntity;
import com.shunlian.app.bean.GoldEggPrizeEntity;
import com.shunlian.app.bean.TaskDrawEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IGoldEggLuckyWheelView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class GoldEggLuckyWheelPresenter extends BasePresenter<IGoldEggLuckyWheelView> {

    public GoldEggLuckyWheelPresenter(Context context, IGoldEggLuckyWheelView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<GoldEggPrizeEntity>> brandlist = getApiService().getPrizeList(map);

        getNetData(true, brandlist, new SimpleNetDataCallback<BaseEntity<GoldEggPrizeEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GoldEggPrizeEntity> entity) {
                super.onSuccess(entity);
                GoldEggPrizeEntity goldEggPrizeEntity = entity.data;
                iView.getPrizeData(goldEggPrizeEntity);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void getTaskDraw() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<TaskDrawEntity>> taskDraw = getApiService().getTaskDraw(map);

        getNetData(true, taskDraw, new SimpleNetDataCallback<BaseEntity<TaskDrawEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TaskDrawEntity> entity) {
                super.onSuccess(entity);
                iView.getTaskDraw(entity.data);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void getDrawRecordList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<DrawRecordEntity>> taskDraw = getApiService().getDrawRecordList(map);

        getNetData(true, taskDraw, new SimpleNetDataCallback<BaseEntity<DrawRecordEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DrawRecordEntity> entity) {
                super.onSuccess(entity);
                iView.getDrawRecordList(entity.data.list);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
