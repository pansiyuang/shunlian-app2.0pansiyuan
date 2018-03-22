package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DiscoveryCircleEntity;
import com.shunlian.app.bean.DiscoveryMaterialEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IDiscoverQuanzi;
import com.shunlian.app.view.IDiscoverSucaiku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PDiscoverQuanzi extends BasePresenter<IDiscoverQuanzi> {
    private int pageSize=20;
    private boolean isFirst=true;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading;
    private List<DiscoveryCircleEntity.Mdata.Content> mDatas = new ArrayList<>();

    public PDiscoverQuanzi(Context context, IDiscoverQuanzi iView) {
        super(context, iView);
        getApiData(babyPage);
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

    public void resetBaby() {
        babyPage = 1;
        babyIsLoading = true;
        mDatas.clear();
        getApiData(babyPage);
    }

    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getApiData(babyPage);
        }
    }

    public void getApiData(int page){
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("pageSize", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<DiscoveryCircleEntity>> baseEntityCall = getApiService().discoveryCircle(map);
        getNetData(isFirst,baseEntityCall, new SimpleNetDataCallback<BaseEntity<DiscoveryCircleEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DiscoveryCircleEntity> entity) {
                super.onSuccess(entity);
                DiscoveryCircleEntity data =entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(data.list.total_page);
                mDatas.addAll(data.list.list);
                iView.setApiData(data.list,mDatas);
                isFirst=false;
            }
        });
    }

}
