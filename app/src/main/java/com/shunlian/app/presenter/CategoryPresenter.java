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
    private GoodsSearchParam goodsSearchParam;

    public CategoryPresenter(Context context, ICategoryView iView, GoodsSearchParam goodsSearchParam) {
        super(context, iView);
        this.goodsSearchParam = goodsSearchParam;
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

    public void getSearchGoods(GoodsSearchParam goodsSearchParam) {
        Map<String, String> map = new HashMap<>();
        map.put("keyword", goodsSearchParam.keyword);
        map.put("min_price", goodsSearchParam.min_price);
        map.put("max_price",goodsSearchParam. max_price);
        map.put("is_free_ship", goodsSearchParam.is_free_ship);
        map.put("brand_ids", goodsSearchParam.brand_ids);
        map.put("cid", goodsSearchParam.cid);
        map.put("send_area", goodsSearchParam.send_area);
        try {
            String attr_data = new ObjectMapper().writeValueAsString(goodsSearchParam.attr_data);
            map.put("attr_data", attr_data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        map.put("sort_type",goodsSearchParam. sort_type);

        sortAndMD5(map);

        Call<BaseEntity<SearchGoodsEntity>> searchGoodsCallback = getAddCookieApiService().getSearchGoods(getRequestBody(map));
        getNetData(true, searchGoodsCallback, new SimpleNetDataCallback<BaseEntity<SearchGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SearchGoodsEntity> entity) {
                super.onSuccess(entity);
                if (entity.data != null) {
                    SearchGoodsEntity searchGoodsEntity = entity.data;
//                    iView.getSearchGoods(entity.data);
                    LogUtil.httpLogW("searchGoodsEntity:" + searchGoodsEntity);

                }

            }
        });
    }

//    public void getSearchGoods(String keyword, String min_price, String max_price, String is_free_ship, String brand_ids, String cid, String send_area, String attr_data, String sort_type) {

}
