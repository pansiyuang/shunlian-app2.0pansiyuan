package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GetQrCardEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IBalanceMain;
import com.shunlian.app.view.IQrCode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PBalanceMain extends BasePresenter<IBalanceMain> {
    public PBalanceMain(Context context, IBalanceMain iView) {
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
        Call<BaseEntity<BalanceInfoEntity>> baseEntityCall;
        if (Constant.ISBALANCE){
            baseEntityCall = getApiService().balanceInfo(map);
        }else {
            baseEntityCall = getApiService().balanceDetail(map);
        }
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<BalanceInfoEntity>>() {
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

}
