package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ExpertEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ISearchExpertView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/10/24.
 */

public class SearchExpertPresenter extends BasePresenter<ISearchExpertView> {
    private String currentKeyword;
    public static final int PAGE_SIZE = 10;

    public SearchExpertPresenter(Context context, ISearchExpertView iView) {
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

    public void getSearchExpertList(boolean isFirst, String keyword) {
        currentKeyword = keyword;
        Map<String, String> map = new HashMap<>();
        if (!isEmpty(keyword)) {
            map.put("key_word", keyword);
        }
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<ExpertEntity>> baseEntityCall = getApiService().searchExpert(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ExpertEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ExpertEntity> entity) {
                super.onSuccess(entity);
                ExpertEntity expertEntity = entity.data;
                isLoading = false;
                iView.getExpertList(expertEntity.list, expertEntity.pager.page, expertEntity.pager.total_page);
                currentPage = expertEntity.pager.page;
                allPage = expertEntity.pager.total_page;
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
                getSearchExpertList(false, currentKeyword);
            }
        }
    }
}
