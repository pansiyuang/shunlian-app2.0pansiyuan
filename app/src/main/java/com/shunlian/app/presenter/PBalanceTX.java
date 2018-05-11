package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.balance.BalanceTXAct;
import com.shunlian.app.view.IBalanceMain;
import com.shunlian.app.view.IBalanceTX;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PBalanceTX extends BasePresenter<IBalanceTX> {
    public PBalanceTX(Context context, IBalanceTX iView) {
        super(context, iView);
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().getWithdrawAccount(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    iView.setApiData(data);
                }
            }
        });
    }

    public void tiXian(String password,String amount,String account_number){
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        map.put("amount", amount);
        map.put("account_number",account_number);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().balanceWithdraw(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data=entity.data;
                iView.tiXianCallback(data,-1,"");
            }

            @Override
            public void onErrorCode(int code, String message) {
                if (code== BalanceTXAct.PASSWORDERROR||code==BalanceTXAct.PASSWORDLOCK){
                    iView.tiXianCallback(null,code,message);
                }else {
                    super.onErrorCode(code, message);
                }
            }
        });

    }
}