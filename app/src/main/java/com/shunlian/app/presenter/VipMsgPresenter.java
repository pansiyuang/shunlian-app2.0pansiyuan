package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IView;
import com.shunlian.app.view.IVipMsgView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/8.
 */

public class VipMsgPresenter extends BasePresenter<IVipMsgView> {
    public boolean isVipMsg = true;
    public static final int PAGE_SIZE = 20;

    public VipMsgPresenter(Context context, IVipMsgView iView) {
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

    public void getVipMessageList(boolean isShow) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        isVipMsg = true;

        Call<BaseEntity<StoreMsgEntity>> sysmessage = getApiService().getVipMessage(map);
        getNetData(isShow, sysmessage, new SimpleNetDataCallback<BaseEntity<StoreMsgEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreMsgEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                StoreMsgEntity storeMsgEntity = entity.data;
                iView.getStoreMsgs(storeMsgEntity.list, storeMsgEntity.page, storeMsgEntity.total);
                currentPage = storeMsgEntity.page;
                allPage = storeMsgEntity.total;
                currentPage++;
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
            }
        });
    }

    public void getOrderMessageList(boolean isShow) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        isVipMsg = false;

        Call<BaseEntity<StoreMsgEntity>> sysmessage = getApiService().getOrderMessage(map);
        getNetData(isShow, sysmessage, new SimpleNetDataCallback<BaseEntity<StoreMsgEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreMsgEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                StoreMsgEntity storeMsgEntity = entity.data;
                iView.getOrderMsgs(storeMsgEntity.list, storeMsgEntity.page, storeMsgEntity.total);
                currentPage = storeMsgEntity.page;
                allPage = storeMsgEntity.total;
                currentPage++;
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                if (isVipMsg) {
                    getVipMessageList(false);
                } else {
                    getOrderMessageList(false);
                }
            }
        }
    }
}
