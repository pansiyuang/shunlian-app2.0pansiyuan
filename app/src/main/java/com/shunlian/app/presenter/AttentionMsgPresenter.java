package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AttentionMsgEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IAttentionMsgView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/10/23.
 */

public class AttentionMsgPresenter extends BasePresenter<IAttentionMsgView> {

    public static final int PAGE_SIZE = 10;

    public AttentionMsgPresenter(Context context, IAttentionMsgView iView) {
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

    public void getAttentionMsgList(boolean isFirst) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<AttentionMsgEntity>> baseEntityCall = getApiService().getAttentionMsg(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AttentionMsgEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AttentionMsgEntity> entity) {
                super.onSuccess(entity);
                AttentionMsgEntity attentionMsgEntity = entity.data;
                isLoading = false;
                iView.getAttentionMsgList(attentionMsgEntity.list, attentionMsgEntity.page, attentionMsgEntity.total_page);
                currentPage = attentionMsgEntity.page;
                allPage = attentionMsgEntity.total_page;

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

    //1关注，2取消关注
    public void focusUser(int type, String memberId) {
        Map<String, String> map = new HashMap<>();
        if (type == 0) {
            map.put("type", "1");
        } else {
            map.put("type", "2");
        }
        map.put("member_id", memberId);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().focusUser(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.focusUser(type, memberId);
                Common.staticToast(entity.message);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
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
                getAttentionMsgList(false);
            }
        }
    }
}
