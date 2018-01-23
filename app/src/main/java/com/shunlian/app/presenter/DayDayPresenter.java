package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.DayDayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class DayDayPresenter extends BasePresenter<DayDayView> {
    private int babyPage = 1;//当前页数
    private final int count = 20;//获取数量
    private String babyId = "";
    private int babyAllPage = 0;
    private boolean babyIsLoading;
    private List<ActivityListEntity.MData.Good.MList> babyDatas = new ArrayList<>();

    public DayDayPresenter(Context context, DayDayView iView) {
        super(context, iView);
    }

    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            initApiData(babyId, babyPage, count);
        }
    }

    public void resetBaby(String id) {
        babyPage = 1;
        babyId = id;
        babyIsLoading = true;
        babyDatas.clear();
        initApiData(id, babyPage, count);
    }

    public void initApiData(String id,int page,int pageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("page", String.valueOf(page));
        map.put("page_size", String.valueOf(pageSize));
        sortAndMD5(map);
        Call<BaseEntity<ActivityListEntity>> baseEntityCall = getApiService().activityList(map);

        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ActivityListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ActivityListEntity> entity) {
                super.onSuccess(entity);
                ActivityListEntity data = entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(data.datas.goods.allPage);
                babyDatas.addAll(data.datas.goods.list);
                iView.getApiData(data, babyAllPage,babyPage,babyDatas);
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
