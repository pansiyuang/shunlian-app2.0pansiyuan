package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
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

    public PayListPresenter(Context context, IPayListView iView, boolean isPLUS) {
        super(context, iView);
        mIsPLUS = isPLUS;
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

    /**
     * 提交订单
     *
     * @param shop_goods
     * @param address_id
     * @param paytype
     */
    public void orderCheckout(String shop_goods, String address_id, String paytype) {
        Map<String, String> map = new HashMap<>();
        map.put("shop_goods", shop_goods);
        map.put("address_id", address_id);
        map.put("paytype", paytype);
        sortAndMD5(map);
        Call<BaseEntity<PayOrderEntity>> baseEntityCall = getAddCookieApiService().orderCheckout(getRequestBody(map));

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
}
