package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.LuckDrawEntity;
import com.shunlian.app.bean.TurnTableEntity;
import com.shunlian.app.bean.TurnTablePopEntity;
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

//            @Override
//            public void onErrorCode(int code, String message) {
//                Common.staticToast(context, message);
//                super.onErrorCode(code, message);
//            }
        });
    }

    public void luckDrawPopup() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<TurnTablePopEntity>> baseEntityCall = getApiService().turntablePopup(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<TurnTablePopEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TurnTablePopEntity> entity) {
                TurnTablePopEntity turnTablePopEntity = entity.data;
                iView.getTurnPop(turnTablePopEntity);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void turntableAddAddress(String id, String tmtId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("tmt_id", tmtId);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().turntableAddAddress(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                Common.staticToast(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void turntableShareImg() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().turntableShareImg(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                CommonEntity commonEntity = entity.data;
                iView.getShareImg(commonEntity.img);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }
}
