package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.PayListEntity;
import com.shunlian.app.bean.PayOrderEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPayListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/21.
 */

public class PayListPresenter extends BasePresenter<IPayListView> {

    private boolean mIsPLUS;
    private String type = "";

    public PayListPresenter(Context context, IPayListView iView, boolean isPLUS, String seType) {
        super(context, iView);
        mIsPLUS = isPLUS;
        type = seType;
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
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        if (mIsPLUS) {//获取plus支付列表
            map.put("order_type", "product");
        }
        map.put("union_mobile_pay", type);
        sortAndMD5(map);
        Call<BaseEntity<PayListEntity>> methodlist = getApiService().methodlist(map);
        getNetData(methodlist, new SimpleNetDataCallback<BaseEntity<PayListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PayListEntity> entity) {
                super.onSuccess(entity);
                iView.payList(entity.data.pay_method);
            }
        });
    }

    public void payVerify(String url) {
        Call<BaseEntity<EmptyEntity>> methodlist = getApiService().payVerify(url);
        getNetData(methodlist, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.paySuccessCall();
            }


            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.payFailCall();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.payFailCall();
            }
        });
    }

    /***
     * 提交订单
     * @param shop_goods
     * @param address_id
     * @param stage_voucher_id 平台优惠券
     * @param use_egg 金蛋
     * @param paytype
     */
    public void orderCheckout(String shop_goods, String address_id, String stage_voucher_id,
                              String anonymous, String use_egg, String paytype) {
        Map<String, String> map = new HashMap<>();
        map.put("shop_goods", shop_goods);
        map.put("address_id", address_id);
        map.put("stage_voucher_id", stage_voucher_id);
        map.put("anonymous", anonymous);
        map.put("use_egg", use_egg);
        map.put("paytype", paytype);
        sortAndMD5(map);
        Call<BaseEntity<PayOrderEntity>>
                baseEntityCall = getAddCookieApiService().orderCheckout(getRequestBody(map));

        getNetData(true, baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<PayOrderEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<PayOrderEntity> entity) {
                        super.onSuccess(entity);
                        iView.payOrder(entity.data);
                    }

                    @Override
                    public void onErrorData(BaseEntity<PayOrderEntity> payOrderEntityBaseEntity) {
                        super.onErrorData(payOrderEntityBaseEntity);
                        iView.payOrderFail(payOrderEntityBaseEntity.data);
                    }
                });
    }

    /**
     * 从订单列表去支付
     *
     * @param order_id
     * @param paytype
     */
    public void fromOrderListGoPay(String order_id, String paytype) {
        Map<String, String> map = new HashMap<>();
        map.put("order_id", order_id);
        map.put("paytype", paytype);
        sortAndMD5(map);

        Call<BaseEntity<PayOrderEntity>> baseEntityCall = getAddCookieApiService()
                .fromOrderListGoPay(getRequestBody(map));

        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<PayOrderEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PayOrderEntity> entity) {
                super.onSuccess(entity);
                iView.payOrder(entity.data);
            }

            @Override
            public void onErrorData(BaseEntity<PayOrderEntity> payOrderEntityBaseEntity) {
                super.onErrorData(payOrderEntityBaseEntity);
                iView.payOrderFail(payOrderEntityBaseEntity.data);
            }
        });

    }

    /**
     * 提交plus订单
     *
     * @param mProductId
     * @param mSkuId
     * @param addressId
     * @param code
     */
    public void submitPLUSOrder(String mProductId, String mSkuId, String addressId, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("product_id", mProductId);
        map.put("sku_id", mSkuId);
        map.put("address_id", addressId);
        map.put("paytype", code);
        sortAndMD5(map);

        Call<BaseEntity<PayOrderEntity>> baseEntityCall = getAddCookieApiService()
                .submitPLUSOrder(getRequestBody(map));
        getNetData(true, baseEntityCall, new
                SimpleNetDataCallback<BaseEntity<PayOrderEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<PayOrderEntity> entity) {
                        super.onSuccess(entity);
                        iView.payOrder(entity.data);
                    }

                    @Override
                    public void onErrorData(BaseEntity<PayOrderEntity> payOrderEntityBaseEntity) {
                        super.onErrorData(payOrderEntityBaseEntity);
                        iView.payOrderFail(payOrderEntityBaseEntity.data);
                    }
                });
    }

    /**
     * 手机充值
     *
     * @param phoneNumber
     * @param topUpPrice
     * @param paytype
     */
    public void phoneTopUp(String phoneNumber, String topUpPrice, String paytype) {
        Map<String, String> map = new HashMap<>();
        map.put("number", phoneNumber);
        map.put("face_price", topUpPrice);
        map.put("paytype", paytype);
        sortAndMD5(map);

        Call<BaseEntity<PayOrderEntity>> phoneTopUpCall = getAddCookieApiService()
                .phoneTopUp(getRequestBody(map));
        getNetData(true, phoneTopUpCall, new
                SimpleNetDataCallback<BaseEntity<PayOrderEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<PayOrderEntity> entity) {
                        super.onSuccess(entity);
                        iView.payOrder(entity.data);
                    }

                    @Override
                    public void onErrorData(BaseEntity<PayOrderEntity> payOrderEntityBaseEntity) {
                        super.onErrorData(payOrderEntityBaseEntity);
                        iView.payOrderFail(payOrderEntityBaseEntity.data);
                    }
                });
    }
}
