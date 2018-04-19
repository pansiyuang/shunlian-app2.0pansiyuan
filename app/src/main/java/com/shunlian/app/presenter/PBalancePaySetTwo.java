package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IBalancePaySetOne;
import com.shunlian.app.view.IBalancePaySetTwo;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PBalancePaySetTwo extends BasePresenter<IBalancePaySetTwo> {
    public PBalancePaySetTwo(Context context, IBalancePaySetTwo iView) {
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

    public void checkRule(String password){
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().checkPayPasswordRuleValid(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.checkRuleValidCall(true);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.checkRuleValidCall(false);
            }

        });
    }
    public void setPayPassword(String password,String key){
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        map.put("key", key);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().balanceSetPayPassword(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.setPasswordCall();
            }
        });
    }

    public void changePayPassword(String password,String key){
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        map.put("key", key);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().balanceChangePayPassword(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.changePasswordCall();
            }
        });
    }
    public void checkPayPassword(String password){
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().balanceCheckPayPassword(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.checkPasswordCall(true,entity.data.key);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.checkPasswordCall(false,"");
            }
        });
    }
    public void unbindAliPay(String password){
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().balanceUnbindAliPay(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToasts(context,getStringResouce(R.string.balance_jiebangcheng),R.mipmap.icon_common_duihao);
                iView.unbindAliPayCall(true);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.unbindAliPayCall(false);
            }
        });
    }
}
