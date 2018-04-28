package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ISelectStoreGoodsView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/20.
 */

public class SelectStoreGoodsPresenter extends BasePresenter<ISelectStoreGoodsView> {
    public static final int PAGE_SIZE = 20;

    private String currentStoreId;
    private String currentKeyword;
    private String currentSrc1;
    private String currentSrc2;

    public SelectStoreGoodsPresenter(Context context, ISelectStoreGoodsView iView) {
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

    public void getBabyList(boolean isLoad, String storeId, String keyword, String src1, String src2) {
        currentStoreId = storeId;
        currentKeyword = keyword;
        currentSrc1 = src1;
        currentSrc2 = src2;

        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        if (!isEmpty(keyword)) {
            map.put("keyword", currentKeyword);
        }

        if (!isEmpty(currentSrc1)) {
            map.put("sc1", currentSrc1);
        }

        if (!isEmpty(currentSrc2)) {
            map.put("sc2", currentSrc2);
        }
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<StoreGoodsListEntity>> baseEntityCall = getApiService().storeGoodsList(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreGoodsListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreGoodsListEntity> entity) {
                super.onSuccess(entity);
                StoreGoodsListEntity data = entity.data;

                currentPage = Integer.parseInt(data.page);
                allPage = Integer.parseInt(data.allPage);
                iView.getBabyList(data.datas, currentPage, allPage);
                if (currentPage == 1) {
                    iView.refreshFinish();
                }
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

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getBabyList(false, currentStoreId, currentKeyword, currentSrc1, currentSrc2);
            }
        }
    }
}
