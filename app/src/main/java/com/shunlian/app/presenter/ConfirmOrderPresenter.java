package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.new3_login.EditInviteCodeDialog;
import com.shunlian.app.ui.new3_login.New3LoginInfoTipEntity;
import com.shunlian.app.ui.new3_login.VerifyPicDialog;
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

    private EditInviteCodeDialog mInviteCodeDialog;
    private VerifyPicDialog mVerifyPicDialog;
    /********是否有上级***********/
    public static boolean isHasSuperior;

    public ConfirmOrderPresenter(Context context, IConfirmOrderView iView) {
        super(context, iView);
        checkBindShareidV2();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {
        if (mInviteCodeDialog != null)
            mInviteCodeDialog.release();
        if (mVerifyPicDialog != null)
            mVerifyPicDialog.release();

        isHasSuperior = false;
    }

    @Override
    protected void initApi() {

    }

    /**
     * 是否需要绑定上级
     */
    private void isCanBindShareID() {
        //share_status == 2 表示该用户没有上级，需要绑定上级才能购买
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
                codeDetail(inviteCode);
            }
        });
        mInviteCodeDialog.show();

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
        ((ConfirmOrderAct) context).device_order = data.device_order;
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
                Common.staticToasts(context,"已确认",R.mipmap.icon_common_duihao);
                if (mVerifyPicDialog != null){
                    mVerifyPicDialog.release();
                }
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
     * 邀请码详情
     * @param id
     */
    public void codeDetail(String id){
        Map<String,String> map = new HashMap<>();
        map.put("code",id);
        sortAndMD5(map);

        Call<BaseEntity<MemberCodeListEntity>>
                baseEntityCall = getApiService().codeInfo(map);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback
                <BaseEntity<MemberCodeListEntity>>(){

            @Override
            public void onSuccess(BaseEntity<MemberCodeListEntity> entity) {
                super.onSuccess(entity);

                if (mInviteCodeDialog != null)mInviteCodeDialog.dismiss();


                MemberCodeListEntity bean = entity.data;
                if (bean != null) {
                    mVerifyPicDialog = new VerifyPicDialog((Activity) context);
                    mVerifyPicDialog.setTvSureColor(R.color.pink_color);
                    mVerifyPicDialog.setTvSureBgColor(Color.WHITE);
                    mVerifyPicDialog.setMessage("请确认您的导购专员");
                    mVerifyPicDialog.showState(2);
                    mVerifyPicDialog.setMemberDetail(bean.info);
                    mVerifyPicDialog.setSureAndCancleListener("确认", v -> {
                        bindShareid(bean.info.code);
                        mVerifyPicDialog.dismiss();
                    }, "返回", v -> {
                        mVerifyPicDialog.dismiss();
                        if (mInviteCodeDialog != null)mInviteCodeDialog.show();
                    }).show();
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
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
                baseEntityCall = getAddCookieApiService().plusfree(map);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<ConfirmOrderEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ConfirmOrderEntity> entity) {
                super.onSuccess(entity);
                setDate(entity);
            }
        });
    }


    public void checkBindShareidV2(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>>
                baseEntityCall = getApiService().checkBindShareidV2(map);

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){

            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                isHasSuperior = true;
                CommonEntity data = entity.data;
                if (data != null && "2".equals(data.share_status)){//需要绑定上级
                    isCanBindShareID();
                }
            }
        });
    }
}
