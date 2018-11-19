package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ZanShareEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IDownloadView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/10/25.
 */

public class DownloadPresenter extends BasePresenter<IDownloadView> {
    public static final int PAGE_SIZE = 10;

    public DownloadPresenter(Context context, IDownloadView iView) {
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

    public void getDownloadMsgList(boolean isFirst) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<ZanShareEntity>> baseEntityCall = getAddCookieApiService().getDownloadMsg(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ZanShareEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ZanShareEntity> entity) {
                super.onSuccess(entity);
                ZanShareEntity zanShareEntity = entity.data;
                isLoading = false;
                iView.getMsgList(zanShareEntity.list, zanShareEntity.page, zanShareEntity.total_page);
                currentPage = zanShareEntity.page;
                allPage = zanShareEntity.total_page;
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
                getDownloadMsgList(false);
            }
        }
    }
}
