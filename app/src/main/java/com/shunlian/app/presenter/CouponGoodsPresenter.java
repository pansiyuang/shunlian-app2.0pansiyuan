package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.StageVoucherGoodsListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ICouponGoodsView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/7/26.
 */

public class CouponGoodsPresenter extends BasePresenter<ICouponGoodsView> {

    private String mStore_id;
    private String mVoucher_id;

    public CouponGoodsPresenter(Context context, ICouponGoodsView iView,
                                String store_id, String voucher_id) {
        super(context, iView);
        mStore_id = store_id;
        mVoucher_id = voucher_id;
        initApi();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {

    }

    public void pageing(){
        Map<String,String> map = new HashMap<>();
        //map.put("voucher_id",mVoucher_id);
        map.put("store_id",mStore_id);
        map.put("page",currentPage+"");
        map.put("page_size",page_size);
        sortAndMD5(map);

        Call<BaseEntity<String>> baseEntityCall = getApiService().voucherRelatedStore(map);
        getNetData(true,baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<String>>(){
                    @Override
                    public void onSuccess(BaseEntity<String> entity) {
                        super.onSuccess(entity);

                    }
                });
    }
}
