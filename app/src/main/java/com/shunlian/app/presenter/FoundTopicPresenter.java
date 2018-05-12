package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IFoundTopicView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/11.
 */

public class FoundTopicPresenter extends BasePresenter<IFoundTopicView> {

    public static final int PAGE_SIZE = 20;

    public FoundTopicPresenter(Context context, IFoundTopicView iView) {
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

    public void getFoundTopicList(boolean isLoad) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<StoreMsgEntity>> baseEntityCall = getAddCookieApiService().foundTopicList(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreMsgEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreMsgEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                StoreMsgEntity storeMsgEntity = entity.data;
                iView.getFoundTopicList(storeMsgEntity.list, storeMsgEntity.page, storeMsgEntity.total);
                currentPage = entity.data.page;
                allPage = entity.data.total;
                currentPage++;
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getFoundTopicList(false);
            }
        }
    }
}
