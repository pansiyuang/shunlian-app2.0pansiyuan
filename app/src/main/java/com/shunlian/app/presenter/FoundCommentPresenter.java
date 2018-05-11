package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IFoundCommentView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/11.
 */

public class FoundCommentPresenter extends BasePresenter<IFoundCommentView> {

    public static final int PAGE_SIZE = 20;

    public FoundCommentPresenter(Context context, IFoundCommentView iView) {
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

    public void getFoundCommentList(boolean isLoad) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<StoreMsgEntity>> baseEntityCall = getAddCookieApiService().foundCommentList(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreMsgEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreMsgEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                StoreMsgEntity storeMsgEntity = entity.data;
                iView.getFoundCommentList(storeMsgEntity.list, storeMsgEntity.page, storeMsgEntity.total);
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
                getFoundCommentList(false);
            }
        }
    }
}
