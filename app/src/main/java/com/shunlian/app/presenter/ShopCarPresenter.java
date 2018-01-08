package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IShoppingCarView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/18.
 */

public class ShopCarPresenter extends BasePresenter<IShoppingCarView> {

    public ShopCarPresenter(Context context, IShoppingCarView iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void initShopData() {
        initApi();
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<ShoppingCarEntity>> baseEntityCall = getApiService().storeList(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShoppingCarEntity>>() {
                @Override
                public void onSuccess(BaseEntity<ShoppingCarEntity> entity) {
                    super.onSuccess(entity);
                    ShoppingCarEntity shoppingCarEntity = entity.data;
                    if (shoppingCarEntity != null) {
                        iView.OnShoppingCarEntity(shoppingCarEntity);
                    }
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editCar(String carId, String qty, String skuId, String promId, String isCheck) {
        Map<String, String> map = new HashMap<>();
        map.put("cart_id", carId);
        if (!TextUtils.isEmpty(qty)) {
            map.put("qty", qty);
        }
        if (!TextUtils.isEmpty(skuId)) {
            map.put("sku_id", skuId);
        }
        if (!TextUtils.isEmpty(promId)) {
            map.put("prom_id", promId);
        }
        if (!TextUtils.isEmpty(isCheck)) {
            map.put("is_check", isCheck);
        }
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<ShoppingCarEntity>> baseEntityCall = getApiService().carEdit(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShoppingCarEntity>>() {
                @Override
                public void onSuccess(BaseEntity<ShoppingCarEntity> entity) {
                    ShoppingCarEntity shoppingCarEntity = entity.data;
                    if (shoppingCarEntity != null) {
                        iView.OnEditEntity(shoppingCarEntity);
                    }
                    super.onSuccess(entity);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkCartGoods(String storeId, String isCheck) {
        Map<String, String> map = new HashMap<>();
        map.put("store_id", storeId);
        map.put("is_check", isCheck);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<ShoppingCarEntity>> baseEntityCall = getApiService().checkCartGoods(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShoppingCarEntity>>() {
                @Override
                public void onSuccess(BaseEntity<ShoppingCarEntity> entity) {
                    ShoppingCarEntity shoppingCarEntity = entity.data;
                    if (shoppingCarEntity != null) {
                        iView.OnEditEntity(shoppingCarEntity);
                    }
                    super.onSuccess(entity);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVoucher(String voucherId) {
        Map<String, String> map = new HashMap<>();
        map.put("voucher_id", voucherId);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<GoodsDeatilEntity.Voucher>> baseEntityCall = getApiService().getVoucher(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity.Voucher>>() {
                @Override
                public void onSuccess(BaseEntity<GoodsDeatilEntity.Voucher> entity) {
                    if (entity.code == 1000 && entity.data != null) {
                        iView.OnGetVoucher(entity.data);
                    } else {
                        Common.staticToast(entity.message);
                    }
                    super.onSuccess(entity);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removetofav(String goodsId) {
        Map<String, String> map = new HashMap<>();
        map.put("cart_ids", goodsId);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<ShoppingCarEntity>> baseEntityCall = getApiService().removetofav(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShoppingCarEntity>>() {
                @Override
                public void onSuccess(BaseEntity<ShoppingCarEntity> entity) {
                    ShoppingCarEntity shoppingCarEntity = entity.data;
                    if (shoppingCarEntity != null) {
                        iView.OnEditEntity(shoppingCarEntity);
                    }
                    super.onSuccess(entity);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cartRemove(String cartId) {
        Map<String, String> map = new HashMap<>();
        map.put("cart_ids", cartId);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<ShoppingCarEntity>> baseEntityCall = getApiService().cartRemove(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShoppingCarEntity>>() {
                @Override
                public void onSuccess(BaseEntity<ShoppingCarEntity> entity) {
                    ShoppingCarEntity shoppingCarEntity = entity.data;
                    if (shoppingCarEntity != null) {
                        iView.OnEditEntity(shoppingCarEntity);
                    }
                    super.onSuccess(entity);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void multyRemove(String storeId) {
        Map<String, String> map = new HashMap<>();
        map.put("store_id", storeId);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<ShoppingCarEntity>> baseEntityCall = getApiService().multyremove(requestBody);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShoppingCarEntity>>() {
                @Override
                public void onSuccess(BaseEntity<ShoppingCarEntity> entity) {
                    ShoppingCarEntity shoppingCarEntity = entity.data;
                    if (shoppingCarEntity != null) {
                        iView.OnEditEntity(shoppingCarEntity);
                    }
                    super.onSuccess(entity);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}