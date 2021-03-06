package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ProbabyLikeGoodsEntity;
import com.shunlian.app.bean.RecommendEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IShoppingCarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        getApiData();
        getProbablyLikeList();
        getWantHotGoodsList();
    }

    public void getApiData() {
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

    @Override
    protected void initApi() {

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
                    iView.OnGetVoucher(entity.data);
                    super.onSuccess(entity);
                }

                @Override
                public void onErrorCode(int code, String message) {
                    Common.staticToast(message);
                    super.onErrorCode(code, message);
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
                    iView.OnEditEntity(shoppingCarEntity);
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

    public void getProbablyLikeList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        try {
            Call<BaseEntity<ProbabyLikeGoodsEntity>> baseEntityCall = getApiService().getProbablyLikeList(map);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<ProbabyLikeGoodsEntity>>() {
                @Override
                public void onSuccess(BaseEntity<ProbabyLikeGoodsEntity> entity) {
                    ProbabyLikeGoodsEntity probabyLikeGoodsEntity = entity.data;
                    List<ProbabyLikeGoodsEntity.Match> matchList = probabyLikeGoodsEntity.matched;
                    List<ProbabyLikeGoodsEntity.Goods> goodsList = new ArrayList<>();

                    for (int i = 0; i < matchList.size(); i++) {
                        List<ProbabyLikeGoodsEntity.Goods> mGoods = matchList.get(i).goods;
                        ProbabyLikeGoodsEntity.Goods goods = new ProbabyLikeGoodsEntity.Goods();
                        goods.goods_id = matchList.get(i).goods_id;
                        goods.thumb = matchList.get(i).thumb;
                        goods.title = matchList.get(i).hint;
                        goods.isParent = true;
                        goodsList.add(goods);
                        for (int j = 0; j < mGoods.size(); j++) {
                            ProbabyLikeGoodsEntity.Goods good = mGoods.get(j);
                            good.index = j;
                            goodsList.add(good);
                        }
                    }
                    iView.OnGetProbabyGoods(goodsList);
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

    public void getWantHotGoodsList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        try {
            Call<BaseEntity<RecommendEntity>> baseEntityCall = getApiService().getWantHotGoodsList(map);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<RecommendEntity>>() {
                @Override
                public void onSuccess(BaseEntity<RecommendEntity> entity) {
                    RecommendEntity recommendEntity = entity.data;
                    iView.OnGetWantGoodsList(recommendEntity.hot_goods);
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