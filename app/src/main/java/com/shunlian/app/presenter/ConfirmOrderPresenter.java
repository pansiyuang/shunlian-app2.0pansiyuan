package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IConfirmOrderView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/29.
 */

public class ConfirmOrderPresenter extends BasePresenter<IConfirmOrderView> {
    /******是否选择平台优惠券********/
    public static boolean isSelectStageVoucher = false;
    /******是否选择店铺优惠券********/
    public static boolean isSelectStoreVoucher = true;

    public ConfirmOrderPresenter(Context context, IConfirmOrderView iView) {
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

    /**
     * 立即购买接口
     * @param goods_id
     * @param qty
     * @param sku_id
     */
    public void orderBuy(String goods_id,String qty,String sku_id,String address_id){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("qty",qty);
        map.put("sku_id",sku_id);
        if (!TextUtils.isEmpty(address_id)) {
            map.put("address_id", address_id);
        }
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<ConfirmOrderEntity>> baseEntityCall = getAddCookieApiService().orderBuy(requestBody);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<ConfirmOrderEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ConfirmOrderEntity> entity) {
                super.onSuccess(entity);
                /*ConfirmOrderEntity data = entity.data;
                iView.confirmOrderAllGoods(data.enabled,data.disabled,data.address);
                float total_amount = 0;
                if (data.enabled != null && data.enabled.size() > 0){
                    List<ConfirmOrderEntity.Enabled> enabled = data.enabled;
                    for (int i = 0; i < enabled.size(); i++) {
                        String sub_total = enabled.get(i).sub_total;
                        total_amount += Float.parseFloat(sub_total);
                    }
                }
                iView.goodsTotalPrice(data.total_count, Common.formatFloat(total_amount));*/
                setDate(entity);
            }
        });
    }

    /**
     * 购物车进入购买
     * @param cart_ids
     * @param address_id
     */
    public void orderConfirm(String cart_ids,String address_id){
        Map<String,String> map = new HashMap<>();
        map.put("cart_ids",cart_ids);
        if (!TextUtils.isEmpty(address_id)) {
            map.put("address_id", address_id);
        }
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<ConfirmOrderEntity>> baseEntityCall = getAddCookieApiService().orderConfirm(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<ConfirmOrderEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ConfirmOrderEntity> entity) {
                super.onSuccess(entity);
                setDate(entity);
            }
        });
    }

    private void setDate(BaseEntity<ConfirmOrderEntity> entity) {
        ConfirmOrderEntity data = entity.data;

        float total_amount = 0;
        if (data.enabled != null && data.enabled.size() > 0){
            List<ConfirmOrderEntity.Enabled> enabled = data.enabled;
            for (int i = 0; i < enabled.size(); i++) {
                ConfirmOrderEntity.Enabled enabled1 = enabled.get(i);
                String sub_total = enabled1.sub_total;
                total_amount += Float.parseFloat(sub_total);
                if ("1".equals(data.user_stage_voucher)){
                    enabled1.selectVoucherId = enabled1.voucher.size();
                }
                if (!isEmpty(enabled1.voucher)){
                    ConfirmOrderEntity.Voucher voucher = new ConfirmOrderEntity.Voucher();
                    voucher.title = getStringResouce(R.string.not_use_voucher);
                    voucher.voucher_hint = getStringResouce(R.string.not_use_voucher);
                    voucher.voucher_id = "";
                    voucher.denomination = "0";
                    enabled1.voucher.add(voucher);
                }
            }
        }

        iView.goodsTotalPrice(data.total_count, Common.formatFloat(total_amount));
        iView.confirmOrderAllGoods(data.enabled,data.disabled,data.address);

        if (!isEmpty(data.stage_voucher)){
            ConfirmOrderEntity.Voucher voucher = new ConfirmOrderEntity.Voucher();
            voucher.title = getStringResouce(R.string.not_use_voucher);
            voucher.voucher_hint = getStringResouce(R.string.not_use_voucher);
            voucher.voucher_id = "";
            voucher.denomination = "0";
            data.stage_voucher.add(voucher);
        }
        if ("1".equals(data.user_stage_voucher)){
            isSelectStageVoucher = true;
            isSelectStoreVoucher = false;
        }else {
            isSelectStageVoucher = false;
            isSelectStoreVoucher = true;
        }
        iView.stageVoucher(data.user_stage_voucher,data.stage_voucher);
    }

    /**
     * 购买套餐
     * @param combo
     */
    public void buyCombo(String combo,String address_id){
        Map<String,String> map = new HashMap<>();
        map.put("combo",combo);
        map.put("address_id",address_id);
        sortAndMD5(map);

        Call<BaseEntity<ConfirmOrderEntity>> buycombo = getAddCookieApiService().buycombo(getRequestBody(map));
        getNetData(true,buycombo,new SimpleNetDataCallback<BaseEntity<ConfirmOrderEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ConfirmOrderEntity> entity) {
                super.onSuccess(entity);
                /*ConfirmOrderEntity data = entity.data;
                iView.confirmOrderAllGoods(data.enabled,data.disabled,data.address);
                float total_amount = 0;
                if (data.enabled != null && data.enabled.size() > 0){
                    List<ConfirmOrderEntity.Enabled> enabled = data.enabled;
                    for (int i = 0; i < enabled.size(); i++) {
                        String sub_total = enabled.get(i).sub_total;
                        total_amount += Float.parseFloat(sub_total);
                    }
                }
                iView.goodsTotalPrice(data.total_count, Common.formatFloat(total_amount));*/
                setDate(entity);
            }
        });
    }
}
