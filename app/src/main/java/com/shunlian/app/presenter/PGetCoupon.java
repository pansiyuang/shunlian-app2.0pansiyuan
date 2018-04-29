package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotRdEntity;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IAishang;
import com.shunlian.app.view.IGetCoupon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PGetCoupon extends BasePresenter<IGetCoupon> {
    private int pageSize=20;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading;

    public List<VouchercenterplEntity.MData> mDatas = new ArrayList<>();

    public PGetCoupon(Context context, IGetCoupon iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void resetBaby(String type,String keyword) {
        babyPage = 1;
        babyIsLoading = true;
        mDatas.clear();
        getDian(type,keyword);
    }

    public void refreshBaby(String type,String keyword) {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getDian(type,keyword);
        }
    }

    @Override
    protected void initApi() {

    }

    /**
     * 领取优惠券
     *
     * @param voucherId
     */
    public void getVoucher(String voucherId,boolean isCommon,int position) {

        if (Common.loginPrompt()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("voucher_id", voucherId);
        sortAndMD5(map);
        Call<BaseEntity<GoodsDeatilEntity.Voucher>> baseEntityCall = getApiService().getVoucher(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity.Voucher>>() {
            @Override
            public void onSuccess(BaseEntity<GoodsDeatilEntity.Voucher> entity) {
                super.onSuccess(entity);
                Common.staticToast(context.getResources().getString(R.string.get_success));
                iView.getCouponCallBack(isCommon,position);
            }
        });

    }

    public void getPing() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<VouchercenterplEntity>> baseEntityCall = getApiService().vouchercenterpl(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<VouchercenterplEntity>>() {
            @Override
            public void onSuccess(BaseEntity<VouchercenterplEntity> entity) {
                super.onSuccess(entity);
                iView.setpingData(entity.data);
            }
        });
    }

    public void getDian(String type,String keyword) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(babyPage));
        map.put("page_size", String.valueOf(pageSize));
        map.put("type", type);
        map.put("keyword", keyword);
        sortAndMD5(map);

        Call<BaseEntity<VouchercenterplEntity>> baseEntityCall = getApiService().vouchercenter(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<VouchercenterplEntity>>() {
            @Override
            public void onSuccess(BaseEntity<VouchercenterplEntity> entity) {
                super.onSuccess(entity);
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(entity.data.total_page);
                mDatas.addAll(entity.data.seller_voucher);
                iView.setdianData(mDatas,entity.data.page,entity.data.total_page);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.showDataEmptyView(0);
            }
        });
    }

}
