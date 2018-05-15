package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListOneEntity;
import com.shunlian.app.bean.StorePromotionGoodsListTwoEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.StoreSearchView;
import com.shunlian.app.view.StoreSortView;
import com.shunlian.app.view.StoreView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class PStoreSearch extends BasePresenter<StoreSearchView> {
    private final int count = 20;//获取数量
    public String storeId,sc1,sc2,keyword;
    private int babyPage = 1;//当前页数
    private String babySort = "default";//评价类型
    private int babyAllPage = 0;
    private boolean babyIsLoading;
    private List<StoreGoodsListEntity.MData> babyDatas = new ArrayList<>();

    public PStoreSearch(Context context, StoreSearchView iView, String storeId, String sc1, String sc2) {
        super(context, iView);
        this.storeId = storeId;
        this.sc1 = sc1;
        this.sc2 = sc2;
    }


    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            initBaby(storeId, babyPage, babySort, count,keyword);
        }
    }


    public void resetBaby(String sort,String key) {
        babyPage = 1;
        babySort = sort;
        keyword=key;
        babyIsLoading = true;
        babyDatas.clear();
        initBaby(storeId, babyPage, babySort, count,keyword);
    }


    @Override
    protected void initApi() {

    }

    public void initBaby(String storeId, final int page, String sort, int pageSize,String keyword) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        map.put("page", String.valueOf(page));
        map.put("sort", sort);
        map.put("keyword", keyword);
        map.put("sc1", sc1);
        map.put("sc2", sc2);
        map.put("pageSize", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<StoreGoodsListEntity>> baseEntityCall = getApiService().storeGoodsList(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreGoodsListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreGoodsListEntity> entity) {
                super.onSuccess(entity);
                StoreGoodsListEntity data = entity.data;
                if (data != null && data.datas != null) {
                    babyIsLoading = false;
                    babyPage++;
                    babyAllPage = Integer.parseInt(data.allPage);
                    babyDatas.addAll(data.datas);
                    iView.storeBaby(babyDatas, babyAllPage, babyPage);
                }
            }
        });
    }


    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
