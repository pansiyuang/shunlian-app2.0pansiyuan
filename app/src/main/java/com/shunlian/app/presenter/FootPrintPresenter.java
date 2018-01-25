package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IFootPrintView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/23.
 */

public class FootPrintPresenter extends BasePresenter<IFootPrintView> {
    public FootPrintPresenter(Context context, IFootPrintView iView) {
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

    public void getMarkCalendar(String year, String month) {
        Map<String, String> map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().getmarkCalendar(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                if (1000 == entity.code) {
                    CommonEntity commonEntity = entity.data;
                    iView.getCalendarList(commonEntity.calendar);
                } else {
                    Common.staticToast(entity.message);
                }
            }
        });
    }

    public void getMarklist(String year, String month, String page, String pageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        map.put("page", page);
        map.put("pageSize", pageSize);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<FootprintEntity>> baseEntityCall = getAddCookieApiService().getmarklist(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<FootprintEntity>>() {
            @Override
            public void onSuccess(BaseEntity<FootprintEntity> entity) {
                super.onSuccess(entity);
                if (1000 == entity.code) {
                    FootprintEntity footprintEntity = entity.data;
                    iView.getMarkList(footprintEntity.mark_data);
                } else {
                    Common.staticToast(entity.message);
                }
            }
        });
    }
}
