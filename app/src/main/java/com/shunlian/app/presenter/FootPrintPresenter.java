package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IFootPrintView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/23.
 */

public class FootPrintPresenter extends BasePresenter<IFootPrintView> {
    public static final int PAGE_SIZE = 20;
    private String currentYear;
    private String currentMonth;

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


    public void getMarkCalendar() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().getmarkCalendar(requestBody);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity = entity.data;
                iView.getCalendarList(commonEntity.calendar);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void getMarklist(String year, String month, boolean isShowLoading) {
        getMarklist(year, month, "", isShowLoading);
    }

    public void initPage() {
        currentPage = 1;
    }

    public void getMarklist(String year, String month, String day, boolean isShowLoading) {
        currentYear = year;
        currentMonth = month;
        Map<String, String> map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        if (!TextUtils.isEmpty(day)) {
            map.put("date", day);
        }
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<FootprintEntity>> baseEntityCall = getAddCookieApiService().getmarklist(requestBody);
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<FootprintEntity>>() {
            @Override
            public void onSuccess(BaseEntity<FootprintEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                FootprintEntity footprintEntity = entity.data;
                currentPage = Integer.parseInt(entity.data.page);
                allPage = Integer.parseInt(entity.data.total_page);
                iView.getMarkList(footprintEntity.mark_data, footprintEntity.date_info, currentPage, allPage);
                currentPage++;
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void deleteBatch(String ids) {
        if (Common.loginPrompt()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("ids", ids);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().deleteBatch(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.delSuccess(entity.message, entity.data.date_info);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getMarklist(currentYear, currentMonth, false);
            }
        }
    }
}
