package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.CategoryFiltrateView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class CategoryFiltratePresenter extends BasePresenter<CategoryFiltrateView> {
    private String cid, keyword;
    private boolean isSecond;

    public CategoryFiltratePresenter(Context context, CategoryFiltrateView iView, String cid, String keyword) {
        super(context, iView);
        this.cid = cid;
        this.keyword = keyword;
        initApi();
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("cid", cid);
        map.put("keyword", keyword);
        sortAndMD5(map);
        Call<BaseEntity<GetListFilterEntity>> baseEntityCall = getApiService().getListFilter(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetListFilterEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetListFilterEntity> entity) {
                super.onSuccess(entity);
                iView.getListFilter(entity.data);
                initGps();
            }
        });
    }

    public void initGps() {
        Location location = Common.getGPS((Activity) context);
        if (location != null) {
            Map<String, String> map = new HashMap<>();
            map.put("lng", String.valueOf(location.getLongitude()));
            map.put("lat", String.valueOf(location.getLatitude()));
            sortAndMD5(map);

            RequestBody requestBody = getRequestBody(map);
            Call<BaseEntity<DistrictGetlocationEntity>> baseEntityCall = getAddCookieApiService().districtGetlocation(requestBody);
            getNetData(isSecond, baseEntityCall, new SimpleNetDataCallback<BaseEntity<DistrictGetlocationEntity>>() {
                @Override
                public void onSuccess(BaseEntity<DistrictGetlocationEntity> entity) {
                    super.onSuccess(entity);
                    DistrictGetlocationEntity data = entity.data;
                    if (data != null) {
                        LogUtil.httpLogW("DistrictGetlocationEntity:" + data);
                        iView.getGps(data);
                        isSecond=true;
                    }
                }
            });
        }
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
