package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.InvitationEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IInvitationRecordeView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/28.
 */

public class InvitationRecordPresenter extends BasePresenter<IInvitationRecordeView> {
    public static final int PAGE_SIZE = 20;

    public InvitationRecordPresenter(Context context, IInvitationRecordeView iView) {
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

    public void getInviteHistory(boolean isShowLoading) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<InvitationEntity>> baseEntityCall = getApiService().inviteHistory(getRequestBody(map));
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<InvitationEntity>>() {
            @Override
            public void onSuccess(BaseEntity<InvitationEntity> entity) {
                InvitationEntity invitationEntity = entity.data;
                InvitationEntity.Pager pager = invitationEntity.pager;
                isLoading = false;
                currentPage = pager.page;
                allPage = pager.total_page;
                iView.getInvitationRecord(currentPage, allPage, invitationEntity.list);
                currentPage++;
                super.onSuccess(entity);
            }

            @Override
            public void onErrorData(BaseEntity<InvitationEntity> invitationEntityBaseEntity) {
                iView.showFailureView(0);
                super.onErrorData(invitationEntityBaseEntity);
            }

            @Override
            public void onFailure() {
                iView.showFailureView(0);
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                iView.showFailureView(0);
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
            if (currentPage < allPage) {
                getInviteHistory(false);
            }
        }
    }
}
