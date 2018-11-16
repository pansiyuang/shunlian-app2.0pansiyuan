package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.NoticeMsgEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.INoticeMsgView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/10/25.
 */

public class NoticeMsgPresenter extends BasePresenter<INoticeMsgView> {

    public static final int PAGE_SIZE = 10;

    public NoticeMsgPresenter(Context context, INoticeMsgView iView) {
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

    public void getNoticeMsgList(boolean isFirst) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<NoticeMsgEntity>> baseEntityCall = getAddCookieApiService().getNoticeMsg(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<NoticeMsgEntity>>() {
            @Override
            public void onSuccess(BaseEntity<NoticeMsgEntity> entity) {
                super.onSuccess(entity);
                NoticeMsgEntity noticeMsgEntity = entity.data;
                isLoading = false;
                iView.getNoticeMsgList(noticeMsgEntity.list, noticeMsgEntity.page, noticeMsgEntity.total_page);
                currentPage = noticeMsgEntity.page;
                allPage = noticeMsgEntity.total_page;
                if (currentPage == 1) {
                    iView.refreshFinish();
                }
                currentPage++;
            }

            @Override
            public void onFailure() {
                isLoading = false;
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                isLoading = false;
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
                getNoticeMsgList(false);
            }
        }
    }
}
