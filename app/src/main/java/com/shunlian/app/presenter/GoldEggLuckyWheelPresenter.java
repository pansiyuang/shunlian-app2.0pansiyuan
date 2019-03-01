package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.CreditLogEntity;
import com.shunlian.app.bean.DrawRecordEntity;
import com.shunlian.app.bean.GoldEggPrizeEntity;
import com.shunlian.app.bean.MyDrawRecordEntity;
import com.shunlian.app.bean.NoAddressOrderEntity;
import com.shunlian.app.bean.TaskDrawEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
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
    }

    public void getPrizeList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<GoldEggPrizeEntity>> brandlist = getApiService().getPrizeList(map);
        getNetData(false, brandlist, new SimpleNetDataCallback<BaseEntity<GoldEggPrizeEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GoldEggPrizeEntity> entity) {
                super.onSuccess(entity);
                GoldEggPrizeEntity goldEggPrizeEntity = entity.data;
                iView.getPrizeData(goldEggPrizeEntity);
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
                if (code == 4040) {
                    iView.taskDrawFail();
                }
            }
        });
    }

    public void getDrawRecordList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<DrawRecordEntity>> taskDraw = getApiService().getDrawRecordList(map);
        getNetData(false, taskDraw, new SimpleNetDataCallback<BaseEntity<DrawRecordEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DrawRecordEntity> entity) {
                super.onSuccess(entity);
                iView.getDrawRecordList(entity.data.list);
            }
        });
    }

    public void getNoAddressOrder() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<NoAddressOrderEntity>> noAddressOrder = getApiService().getNoAddressOrder(map);
        getNetData(false, noAddressOrder, new SimpleNetDataCallback<BaseEntity<NoAddressOrderEntity>>() {
            @Override
            public void onSuccess(BaseEntity<NoAddressOrderEntity> entity) {
                super.onSuccess(entity);
                LogUtil.httpLogW("getNoAddressOrder1321");
                iView.getNoAddressData(entity.data);
            }
        });
    }

    public void getMyDrawRecordList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<MyDrawRecordEntity>> noAddressOrder = getApiService().getMyDrawRecordList(map);
        getNetData(false, noAddressOrder, new SimpleNetDataCallback<BaseEntity<MyDrawRecordEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MyDrawRecordEntity> entity) {
                super.onSuccess(entity);
                iView.getMyRecordList(entity.data.list);
            }
        });
    }

    public void updateOrderAddress(String id, String address_id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("address_id", address_id);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> noAddressOrder = getApiService().updateOrderAddress(getRequestBody(map));
        getNetData(true, noAddressOrder, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
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
