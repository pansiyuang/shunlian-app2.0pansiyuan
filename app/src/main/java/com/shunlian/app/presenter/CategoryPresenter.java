package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ICategoryView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/5.
 */

public class CategoryPresenter extends BasePresenter<ICategoryView> {

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

    public void getSearchGoods() {
        getSearchGoods("", "", "", "", "", "", "", "", "");
    }

    public void getSearchGoods(String keyword, String min_price, String max_price, String is_free_ship, String brand_ids, String cid, String send_area, String attr_data, String sort_type) {
        Map<String, String> map = new HashMap<>();

        if (!TextUtils.isEmpty(keyword)) {
            map.put("keyword", keyword);
        }
        if (!TextUtils.isEmpty(min_price)) {
            map.put("min_price", min_price);
        }
        if (!TextUtils.isEmpty(max_price)) {
            map.put("max_price", max_price);
        }
        if (!TextUtils.isEmpty(is_free_ship)) {
            map.put("is_free_ship", is_free_ship);
        }
        if (!TextUtils.isEmpty(brand_ids)) {
            map.put("brand_ids", brand_ids);
        }
        if (!TextUtils.isEmpty(cid)) {
            map.put("cid", cid);
        }
        if (!TextUtils.isEmpty(send_area)) {
            map.put("send_area", send_area);
        }
        if (!TextUtils.isEmpty(attr_data)) {
            map.put("attr_data", attr_data);
        }
        if (!TextUtils.isEmpty(sort_type)) {
            map.put("sort_type", sort_type);
        }
        sortAndMD5(map);

        Call<BaseEntity<SearchGoodsEntity>> searchGoodsCallback = getAddCookieApiService().getSearchGoods(getRequestBody(map));
        getNetData(true, searchGoodsCallback, new SimpleNetDataCallback<BaseEntity<SearchGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SearchGoodsEntity> entity) {
                if (entity.data != null)
                    iView.getSearchGoods(entity.data);
                super.onSuccess(entity);
            }
        });
    }
}
