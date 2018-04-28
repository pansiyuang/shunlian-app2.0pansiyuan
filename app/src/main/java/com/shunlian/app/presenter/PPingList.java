package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PPingList extends BasePresenter<IPingList> {
    private int pageSize = 20;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading;
    private String id;
    private List<CorePingEntity.MData> mDatas=new ArrayList<>();

    public PPingList(Context context, IPingList iView,String id) {
        super(context, iView);
        this.id=id;
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


    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getApiData();
        }
    }


    public void getApiData(){
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("page", String.valueOf(babyPage));
        map.put("page_size", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<CorePingEntity>> baseEntityCall = getApiService().branddetail(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CorePingEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CorePingEntity> entity) {
                super.onSuccess(entity);
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(entity.data.total_page);
                mDatas.addAll(entity.data.goods_list);
                iView.setApiData(entity.data,mDatas);
            }
        });

    }

}
