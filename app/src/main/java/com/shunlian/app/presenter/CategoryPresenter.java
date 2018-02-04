package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ICategoryView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/5.
 */

public class CategoryPresenter extends BasePresenter<ICategoryView> {
    public static final int PAGE_SIZE = 20;
    private GoodsSearchParam mParam;

    public CategoryPresenter(Context context, ICategoryView iView) {
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
            isLoading = true;
            if (currentPage <= allPage) {
                currentPage++;
                getSearchGoods(mParam, false);
            }
        }
    }
}
