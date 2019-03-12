package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.MergeOrderEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IMegerView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MegerPresenter extends BasePresenter<IMegerView> {
    public static final int PageSize = 20;
    public String currentPromId;
    public String currentOrderBy;

    public MegerPresenter(Context context, IMegerView iView) {
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

    public void initPage() {
        currentPage = 1;
    }

    public void getMegerGoods(boolean isFirst, String promId, String orderBy) {
        currentPromId = promId;
        currentOrderBy = orderBy;
        Map<String, String> map = new HashMap<>();
        map.put("prom_id", currentPromId);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PageSize));
        map.put("order_by", currentOrderBy);
        sortAndMD5(map);

        Call<BaseEntity<MergeOrderEntity>> baseEntityCall = getAddCookieApiService().megerGoodsList(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<MergeOrderEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MergeOrderEntity> entity) {
                MergeOrderEntity mergeOrderEntity = entity.data;
                isLoading = false;
                currentPage = mergeOrderEntity.page;
                allPage = mergeOrderEntity.total_page;
                iView.getMegerOrder(mergeOrderEntity);
                if (currentPage == 1) {
                    iView.refreshFinish();
                }
                currentPage++;
                super.onSuccess(entity);
            }
        });
    }

    public void addCart(String goods_id, String sku_id, String qty, String promId) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        if (!isEmpty(sku_id)) {
            map.put("sku_id", sku_id);
        }
        map.put("qty", qty);
        map.put("prom_id", promId);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CateEntity>> baseEntityCall = getAddCookieApiService().addCart(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CateEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CateEntity> entity) {
                super.onSuccess(entity);
                CateEntity cateEntity = entity.data;
                iView.addCart(cateEntity);
            }
        });
    }

    /**
     * 获取商品属性
     *
     * @param goods_id
     */
    public void getGoodsSku(final String goods_id, String promId) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        map.put("prom_id", promId);
        sortAndMD5(map);

        Call<BaseEntity<GoodsDeatilEntity.GoodsInfo>> goodsSkuCall = getAddCookieApiService().getGoodsSku(getRequestBody(map));
        getNetData(true, goodsSkuCall, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity.GoodsInfo>>() {
            @Override
            public void onSuccess(BaseEntity<GoodsDeatilEntity.GoodsInfo> entity) {
                super.onSuccess(entity);
                GoodsDeatilEntity.Goods goods = new GoodsDeatilEntity.Goods();
                goods.goods_info = entity.data;
                goods.thumb = entity.data.thumb;
                goods.stock = entity.data.stock;
                goods.price = entity.data.price;
                goods.type = entity.data.type;
                goods.limit_min_buy = entity.data.limit_min_buy;
                goods.goods_id = goods_id;
                iView.showGoodsSku(goods);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getMegerGoods(false, currentPromId, currentOrderBy);
            }
        }
    }
}
