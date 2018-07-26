package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.UserCouponAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.StageVoucherGoodsListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IUserCouponListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/7/26.
 */

public class UserCouponListPresenter extends BasePresenter<IUserCouponListView> {


    private String mVoucherId;

    public UserCouponListPresenter(Context context, IUserCouponListView iView, String voucherId) {
        super(context, iView);
        mVoucherId = voucherId;
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
        Map<String,String> map = new HashMap<>();
        map.put("voucher_id",mVoucherId);
        sortAndMD5(map);

        Call<BaseEntity<StageVoucherGoodsListEntity>>
                baseEntityCall = getApiService().stageVoucherGoodsList(map);
        getNetData(true,baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<StageVoucherGoodsListEntity>>(){
                    @Override
                    public void onSuccess(BaseEntity<StageVoucherGoodsListEntity> entity) {
                        super.onSuccess(entity);
                        setData(entity.data);
                    }
                });
    }

    private void setData(StageVoucherGoodsListEntity data) {

        UserCouponAdapter adapter = new UserCouponAdapter(context,data.goods_list,data.voucher_info);
        iView.setAdapter(adapter);
    }
}
