package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.ChatGoodsEntity;
import com.shunlian.app.view.IChatGoodsView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ChatGoodsPresenter extends BasePresenter<IChatGoodsView> {
    public static final int PAGE_SIZE = 20;
    private String mType, mStoreId;

    public ChatGoodsPresenter(Context context, IChatGoodsView iView) {
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

    public void getChatGoodsList(String type, String storeId, boolean isLoad) {
        this.mType = type;
        this.mStoreId = storeId;
        Map<String, String> map = new HashMap<>();
        map.put("type", mType);
        map.put("store_id", mStoreId);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<ChatGoodsEntity>> baseEntityCall = getAddCookieApiService().chatGoodsList(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ChatGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ChatGoodsEntity> entity) {
                super.onSuccess(entity);
                ChatGoodsEntity chatGoodsEntity = entity.data;
                isLoading = false;
                currentPage = Integer.parseInt(entity.data.page);
                allPage = Integer.parseInt(entity.data.total_page);
                iView.getChatGoodsList(chatGoodsEntity.list, currentPage, allPage);
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
                getChatGoodsList(mType, mStoreId, false);
            }
        }
    }
}
