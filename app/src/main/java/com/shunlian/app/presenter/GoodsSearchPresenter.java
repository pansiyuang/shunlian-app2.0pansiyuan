package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IGoodsSearchView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/1.
 */

public class GoodsSearchPresenter extends BasePresenter<IGoodsSearchView> {

    public static final int PAGE_SIZE = 20;
    private GoodsSearchParam mParam;

    public GoodsSearchPresenter(Context context, IGoodsSearchView iView) {
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

    public void getSearchGoods(GoodsSearchParam goodsSearchParam, boolean isShowLoading) {
        this.mParam = goodsSearchParam;
        currentPage = 1;
        requestData(goodsSearchParam, isShowLoading);
    }

    private void requestData(GoodsSearchParam goodsSearchParam, boolean isShowLoading) {
        Map<String, String> map = new HashMap<>();
        LogUtil.augusLogW("yxf--"+goodsSearchParam);
        if (!TextUtils.isEmpty(goodsSearchParam.keyword)) {
            map.put("keyword", goodsSearchParam.keyword);
        }

        if (!TextUtils.isEmpty(goodsSearchParam.min_price)) {
            map.put("min_price", goodsSearchParam.min_price);
        }

        if (!TextUtils.isEmpty(goodsSearchParam.max_price)) {
            map.put("max_price", goodsSearchParam.max_price);
        }

        if (!TextUtils.isEmpty(goodsSearchParam.is_free_ship)) {
            map.put("is_free_ship", goodsSearchParam.is_free_ship);
        }

        if (!TextUtils.isEmpty(goodsSearchParam.brand_ids)) {
            map.put("brand_ids", goodsSearchParam.brand_ids);
        }
        if (!TextUtils.isEmpty(goodsSearchParam.cid)) {
            map.put("cid", goodsSearchParam.cid);
        }

        if (!TextUtils.isEmpty(goodsSearchParam.send_area)) {
            map.put("send_area", goodsSearchParam.send_area);
        }

        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        map.put("small_shop", "1");

        if (goodsSearchParam.attr_data != null && goodsSearchParam.attr_data.size() != 0) {
            try {
                String attr_data = new ObjectMapper().writeValueAsString(goodsSearchParam.attr_data);
                map.put("attr_data", attr_data);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(goodsSearchParam.sort_type)) {
            map.put("sort_type", goodsSearchParam.sort_type);
        }

        sortAndMD5(map);

        Call<BaseEntity<SearchGoodsEntity>> searchGoodsCallback = getAddCookieApiService().getSearchGoods(getRequestBody(map));
        getNetData(isShowLoading, searchGoodsCallback, new SimpleNetDataCallback<BaseEntity<SearchGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SearchGoodsEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                SearchGoodsEntity searchGoodsEntity = entity.data;
                LogUtil.augusLogW("85yxf--"+searchGoodsEntity);
                currentPage = Integer.parseInt(entity.data.page);
                allPage = Integer.parseInt(entity.data.total_page);
                iView.getSearchGoods(searchGoodsEntity, currentPage, allPage);
                currentPage++;
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
            }
        });
    }

    public void initPage() {
        currentPage = 1;
    }

    public void resetCurrentPage() {
        currentPage = allPage;
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                requestData(mParam, false);
            }
        }
    }

    public void getFairishNums() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().fairishNums(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity= entity.data;
                iView.getFairishNums(commonEntity.num);
            }
        });
    }

    public void addStoreGoods(String goodsIds) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_ids",goodsIds);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().addGoods(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity = entity.data;
                iView.addStoreFinish(commonEntity.num);
            }
        });
    }
}
