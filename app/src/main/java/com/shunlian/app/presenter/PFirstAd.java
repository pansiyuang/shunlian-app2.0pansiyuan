package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.FirstPageAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IFirstAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/5.
 */

public class PFirstAd extends BasePresenter<IFirstAd> {
    private int pageSize = 20;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading;

    private List<GoodsDeatilEntity.Goods> mDatas = new ArrayList<>();

    public PFirstAd(Context context, IFirstAd iView) {
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

    public void resetBaby(String cate_id) {
        babyPage = 1;
        babyIsLoading = true;
        mDatas.clear();
        getSearchGoods(cate_id);
    }

    public void refreshBaby( String cate_id) {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getSearchGoods(cate_id);
        }
    }

    public void getSearchGoods(String cate_id) {
        Map<String, String> map = new HashMap<>();
        map.put("cid", cate_id);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<SearchGoodsEntity>> searchGoodsCallback = getAddCookieApiService().getSearchGoods(getRequestBody(map));
        getNetData(true, searchGoodsCallback, new SimpleNetDataCallback<BaseEntity<SearchGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SearchGoodsEntity> entity) {
                super.onSuccess(entity);
                SearchGoodsEntity searchGoodsEntity = entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(searchGoodsEntity.total_page);
                mDatas.addAll(searchGoodsEntity.goods_list);
                iView.setGoods(mDatas, babyPage, babyAllPage);
            }
        });
    }

}
