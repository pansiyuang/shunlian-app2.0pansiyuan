package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Constant;
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
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void initApiData(){
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().getWithdrawAccount(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
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

    @Override
    protected void initApi() {

    }

    public void tiXian(String password, String amount, String account_number) {
        Map<String, String> map = new HashMap<>();
        Call<BaseEntity<CommonEntity>> baseEntityCall;
        if (Constant.ISBALANCE){
            map.put("password", password);
            map.put("amount", amount);
            map.put("account_number", account_number);
            sortAndMD5(map);
            baseEntityCall= getAddCookieApiService().balanceWithdraw(getRequestBody(map));
        }else {
            map.put("pwd", password);
            map.put("money", amount);
            sortAndMD5(map);
            baseEntityCall= getAddCookieApiService().withdrawProfit(map);
        }
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                iView.tiXianCallback(data, -1, "");
            }

            @Override
            public void onErrorCode(int code, String message) {
                iView.tiXianCallback(null, code, message);
                super.onErrorCode(code, message);
            }
        });

    }
}
