package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.TeamListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMemberPageView;
import com.shunlian.app.view.ITeamPageView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/11/5.
 */

public class TeamHistoryPresenter extends BasePresenter<ITeamPageView> {
    protected boolean isLoading = false;//是否正在加载
    public TeamHistoryPresenter(Context context, ITeamPageView iView) {
        super(context, iView);
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {
        currentPage = 1;
        allPage = 1;
    }

    @Override
    protected void initApi() {
        currentPage = 1;
        allPage = 1;
    }

    public void refreshData() {
        currentPage = 1;
        allPage = 1;
        teamListInfo(false);
    }
    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                teamListInfo(false);
            }
        }
    }

    public void teamListInfo(boolean isLoad) {
        Map<String, String> map = new HashMap<>();
        map.put("page",currentPage+"");
        map.put("page_size",page_size+"");
        sortAndMD5(map);
        Call<BaseEntity<TeamListEntity>> setinfo = getAddCookieApiService().teamLog(map);
        getNetData(isLoad, setinfo, new SimpleNetDataCallback<BaseEntity<TeamListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TeamListEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                if(entity.data!=null&&entity.data!=null) {
                    currentPage = Integer.valueOf(entity.data.page);
                    allPage = Integer.valueOf(entity.data.total_page);
                     iView.teamListInfo(entity.data.list,entity.data,currentPage);
                    if(entity.data.list!=null&&entity.data.list.size()>0) {
                        currentPage++;
                    }
                }

            }
        });
    }


}
