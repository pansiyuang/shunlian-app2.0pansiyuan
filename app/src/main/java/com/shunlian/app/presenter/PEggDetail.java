package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.EggDetailEntity;
import com.shunlian.app.bean.HotRdEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IAishang;
import com.shunlian.app.view.IEggDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PEggDetail extends BasePresenter<IEggDetail> {
    private List<EggDetailEntity.Out> mDatas = new ArrayList<>();


    public PEggDetail(Context context, IEggDetail iView) {
        super(context, iView);
        getApiData();
    }

    @Override
    protected void initApi() {

    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    public void refreshBaby() {
        if (!pageIsLoading && currentPage <= allPage) {
            pageIsLoading = true;
            getApiData();
        }
    }


    public void getApiData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", "10");
        sortAndMD5(map);

        Call<BaseEntity<EggDetailEntity>> baseEntityCall = getApiService().eggDetail(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EggDetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EggDetailEntity> entity) {
                super.onSuccess(entity);
                EggDetailEntity data = entity.data;
                pageIsLoading = false;
                currentPage++;
                allPage = Integer.parseInt(data.total);
                allPage = Integer.parseInt(data.total);
                mDatas.addAll(data.list);
                iView.getApiData(allPage,currentPage,mDatas);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
                pageIsLoading=false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
                pageIsLoading=false;
            }
        });

    }
}
