package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.new3_login.EditInviteCodeDialog;
import com.shunlian.app.ui.new3_login.New3LoginInfoTipEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SharedPrefUtil;
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

    private EditInviteCodeDialog mInviteCodeDialog;

    public ConfirmOrderPresenter(Context context, IConfirmOrderView iView) {
        super(context, iView);
        isCanBindShareID();
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
     * 是否需要绑定上级
     */
    private void isCanBindShareID() {
        //share_status == 2 表示该用户没有上级，需要绑定上级才能购买
        String share_status = SharedPrefUtil.getSharedUserString("share_status", "");
        if ("2".equals(share_status)){
            loginInfoTip();
            mInviteCodeDialog = new EditInviteCodeDialog((Activity) context);
            mInviteCodeDialog.setOnClickListener(v -> {
                mInviteCodeDialog.release();
                ((Activity) context).finish();
            }, v -> {
                String inviteCode = mInviteCodeDialog.getInviteCode();
                if (isEmpty(inviteCode)) {
                    Common.staticToast("请填写邀请码");
                } else {
                    bindShareid(inviteCode);
                }
            });
            mInviteCodeDialog.show();
        }
    }


    /**
     * 新人专享
     */
    public void newexclusive(String address_id){
        Map<String,String> map = new HashMap<>();
        if (!TextUtils.isEmpty(address_id)) {
            map.put("address_id", address_id);
        }
        sortAndMD5(map);

        Call<BaseEntity<ConfirmOrderEntity>>
                newexclusive = getAddCookieApiService().newexclusive(getRequestBody(map));

        getNetData(true,newexclusive,new SimpleNetDataCallback<BaseEntity<ConfirmOrderEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ConfirmOrderEntity> entity) {
                super.onSuccess(entity);
                setDate(entity);
            }
        });
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

    /**
     * 购物车进入购买
     * @param address_id
     */
    public void orderNewUserConfirm(String address_id){
        Map<String,String> map = new HashMap<>();
//        map.put("cart_ids",cart_ids);
        if (!TextUtils.isEmpty(address_id)) {
            map.put("address_id", address_id);
        }
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<ConfirmOrderEntity>> baseEntityCall = getAddCookieApiService().orderNewUserConfirm(requestBody);
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
        iView.confirmOrderAllGoods(data.enabled,
                data.disabled,data.address,data.no_delivery);

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
        iView.goldenEggs(data.egg_tip,data.gold_egg,data.egg_reduce);
        iView.receivingPrompt(data.receiving_prompt);
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
                setDate(entity);
            }
        });
    }

    /**
     * 绑定上级
     * @param code
     */
    public void bindShareid(String code){
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>>
                baseEntityCall = getAddCookieApiService().bindShareidV2(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.bindShareID("");
                SharedPrefUtil.saveSharedUserString("share_status", "1");
                if (mInviteCodeDialog != null){
                    mInviteCodeDialog.release();
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.bindShareID(message);
            }
        });
    }

    /**
     * 登录界面提示信息
     */
    public void loginInfoTip(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<New3LoginInfoTipEntity>> baseEntityCall = getApiService().loginInfoTip(map);
        getNetData(false,baseEntityCall,new
                SimpleNetDataCallback<BaseEntity<New3LoginInfoTipEntity>>(){
                    @Override
                    public void onSuccess(BaseEntity<New3LoginInfoTipEntity> entity) {
                        super.onSuccess(entity);
                        if (entity.data != null && mInviteCodeDialog != null){
                            mInviteCodeDialog.setStrategyUrl(entity.data.incite_code_url);
                        }
                    }
                });
    }

    /**
     * plus免费专区
     * @param goods_id
     * @param qty
     * @param sku_id
     * @param address_id
     */
    public void plusfree(String goods_id, String qty, String sku_id, String address_id) {
        Map<String,String> map = new HashMap<>();
        map.put("gid",goods_id);
        map.put("qty",qty);
        map.put("sku_id",sku_id);
        if (!TextUtils.isEmpty(address_id)) {
            map.put("address_id", address_id);
        }
        sortAndMD5(map);

        Call<BaseEntity<ConfirmOrderEntity>>
                baseEntityCall = getAddCookieApiService().plusfree(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<ConfirmOrderEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ConfirmOrderEntity> entity) {
                super.onSuccess(entity);
                setDate(entity);
            }
        });
    }
}
