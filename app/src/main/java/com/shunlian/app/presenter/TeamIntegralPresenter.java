package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.TeamIndexEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IChosenView;
import com.shunlian.app.view.TeamIntegralView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/14.
 */

public class TeamIntegralPresenter extends BasePresenter<TeamIntegralView> {
    private int currentMode;
    private String currentTagId;

    public TeamIntegralPresenter(Context context, TeamIntegralView iView) {
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

    /**
     * 我要当对长或者获取口令值
     * @param isShowLoading
     * @param password
     * @param captain
     */
    public void getTeamIndex(boolean isShowLoading,String password,String captain) {
        Map<String, String> map = new HashMap<>();
        if(!TextUtils.isEmpty(password)){
            map.put("password", password);
        }
        if(!TextUtils.isEmpty(captain)){
            map.put("captain", captain);
        }
        sortAndMD5(map);
        Call<BaseEntity<TeamIndexEntity>> baseEntityCall = getApiService().teamIndex(getRequestBody(map));
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<TeamIndexEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TeamIndexEntity> entity) {
                super.onSuccess(entity);
                iView.teamIndex(entity.data);
            }
            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
            }
        });
    }


}
