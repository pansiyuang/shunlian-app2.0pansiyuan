package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.CategoryFiltrateView;
import com.shunlian.app.view.DayDayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class DayDayPresenter extends BasePresenter<DayDayView> {

    public DayDayPresenter(Context context, DayDayView iView) {
        super(context, iView);
    }

    public void initApiData() {
        Map<String, String> map = new HashMap<>();
//        map.put("status", String.valueOf(status));
//        map.put("page", String.valueOf(page));
//        map.put("page_size", String.valueOf(pageSize));
        sortAndMD5(map);
        Call<BaseEntity<ActivityListEntity>> baseEntityCall = getApiService().activityList(map);

        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ActivityListEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<ActivityListEntity> entity) {
                        super.onSuccess(entity);
                        isLoading = false;
                        ActivityListEntity data = entity.data;
                        iView.getApiData(data);
//                        allPage = Integer.parseInt(data.max_page);
//                        currentPage = Integer.parseInt(data.page);
//                        iView.setNicknameAndAvatar(data.nickname,data.avatar);
//                        iView.commentList(data.list, Integer.parseInt(data.page), allPage);
                    }

                    @Override
                    public void onErrorCode(int code, String message) {
                        super.onErrorCode(code, message);
                        isLoading = false;
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        isLoading = false;
                    }
                });
    }
    @Override
    protected void initApi() {

    }
    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
