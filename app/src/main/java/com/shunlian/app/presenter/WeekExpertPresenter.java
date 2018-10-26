package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ExpertEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IWeekExpertView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/10/19.
 */

public class WeekExpertPresenter extends BasePresenter<IWeekExpertView> {

    public WeekExpertPresenter(Context context, IWeekExpertView iView) {
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

    public void getWeekExpertList() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<ExpertEntity>> baseEntityCall = getAddCookieApiService().weekExpertList(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ExpertEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ExpertEntity> entity) {
                super.onSuccess(entity);
                ExpertEntity expertEntity = entity.data;
                iView.expertList(expertEntity.list);
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
}
