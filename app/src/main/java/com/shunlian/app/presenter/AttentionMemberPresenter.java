package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.MemberEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IAttentionMemberView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/10/25.
 */

public class AttentionMemberPresenter extends BasePresenter<IAttentionMemberView> {

    public static int PAGE_SIZE = 10;
    private String currentMemberId;

    public AttentionMemberPresenter(Context context, IAttentionMemberView iView) {
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

    public void getAttentionList(boolean isFirst) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<MemberEntity>> baseEntityCall = getAddCookieApiService().focusList(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<MemberEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MemberEntity> entity) {
                super.onSuccess(entity);
                MemberEntity memberEntity = entity.data;
                isLoading = false;
                iView.getAttentionList(memberEntity.list, memberEntity.pager.page, memberEntity.pager.total_page);
                currentPage = memberEntity.pager.page;
                allPage = memberEntity.pager.total_page;
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

    public void getTaAttentionList(boolean isFirst, String memberId) {
        currentMemberId = memberId;
        Map<String, String> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<MemberEntity>> baseEntityCall = getAddCookieApiService().taFocusList(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<MemberEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MemberEntity> entity) {
                super.onSuccess(entity);
                MemberEntity memberEntity = entity.data;
                isLoading = false;
                iView.getAttentionList(memberEntity.list, memberEntity.pager.page, memberEntity.pager.total_page);
                currentPage = memberEntity.pager.page;
                allPage = memberEntity.pager.total_page;
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
                if (isEmpty(currentMemberId)) {
                    getAttentionList(false);
                } else {
                    getTaAttentionList(false, currentMemberId);
                }
            }
        }
    }

}
