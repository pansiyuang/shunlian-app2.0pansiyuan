package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.ArticleDetailEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.VoucherEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IAssignVoucherView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/7/20.
 */

public class AssignVoucherPresenter extends BasePresenter<IAssignVoucherView> {

    public AssignVoucherPresenter(Context context, IAssignVoucherView iView) {
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

    public void getVoucherDetai(String voucherId) {
        Map<String, String> map = new HashMap<>();
        map.put("voucher_id", voucherId);
        sortAndMD5(map);
        Call<BaseEntity<VoucherEntity>> baseEntityCall = getApiService().getAssignVoucher(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<VoucherEntity>>() {
            @Override
            public void onSuccess(BaseEntity<VoucherEntity> entity) {
                super.onSuccess(entity);
                VoucherEntity voucherEntity = entity.data;
                iView.getVoucherDetail(voucherEntity);
            }
        });
    }
}
