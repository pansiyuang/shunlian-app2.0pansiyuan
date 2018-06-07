package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.R;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.balance.BalanceTXAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IBalanceMain;
import com.shunlian.app.view.IBalancePaySetOne;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PBalancePaySetOne extends BasePresenter<IBalancePaySetOne> {
    public PBalancePaySetOne(Context context, IBalancePaySetOne iView) {
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

    public void getData(){
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);
        Call<BaseEntity<BalanceInfoEntity>> baseEntityCall;
        if (Constant.ISBALANCE){
            baseEntityCall = getApiService().balanceInfo(map);
        }else {
            baseEntityCall = getApiService().balanceDetail(map);
        }
        getNetData(false,baseEntityCall, new SimpleNetDataCallback<BaseEntity<BalanceInfoEntity>>() {
            @Override
            public void onSuccess(BaseEntity<BalanceInfoEntity> entity) {
                super.onSuccess(entity);
                BalanceInfoEntity data = entity.data;
                if (data != null) {
                    iView.setApiData(data);
                }
            }
        });
    }
    public void getCode(){
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().balanceSendSmsCode(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(getStringResouce(R.string.balance_yanzhengmayifasong));
                iView.getCodeCall();
            }
        });
    }

    public void checkCode(String vcode){
        Map<String, String> map = new HashMap<>();
        map.put("vcode", vcode);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().balanceCheckCode(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data=entity.data;
                iView.nextCall(data.key);
            }
        });
    }

    public void bindAliPay(String vcode,String account_name,String account_number){
        Map<String, String> map = new HashMap<>();
        map.put("vcode", vcode);
        map.put("account_name", account_name);
        map.put("account_number", account_number);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().balanceBindAliPay(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data=entity.data;
                iView.bindAlipayCall(data.account_number);
            }
        });
    }
}
