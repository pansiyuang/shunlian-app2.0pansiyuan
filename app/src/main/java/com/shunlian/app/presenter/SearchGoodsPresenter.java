package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.HotSearchEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ISearchGoodsView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/19.
 */

public class SearchGoodsPresenter extends BasePresenter<ISearchGoodsView> {

    public SearchGoodsPresenter(Context context, ISearchGoodsView iView) {
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

    public void getSearchTag() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<HotSearchEntity>> baseEntityCall = getApiService().hotSearch(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HotSearchEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HotSearchEntity> entity) {
                super.onSuccess(entity);
                if (1000 == entity.code) {
                    iView.getSearchGoods(entity.data);
                } else {
                    Common.staticToast(entity.message);
                }
            }
        });
    }

    public void getSearchTips(String keyWord) {
        Map<String, String> map = new HashMap<>();
        map.put("keyword", keyWord);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().keywordSuggest(requestBody);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
                @Override
                public void onSuccess(BaseEntity<CommonEntity> entity) {
                    if (1000 == entity.code) {
                        CommonEntity commonEntity = entity.data;
                        iView.getSearchTips(commonEntity.suggest_list);
                    } else {

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

    public void clearSearchHistory() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().clearSearchHistory(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                if (1000 == entity.code) {
                    iView.clearSuccess(entity.message);
                } else {
                    Common.staticToast(entity.message);
                }
            }
        });
    }
}
