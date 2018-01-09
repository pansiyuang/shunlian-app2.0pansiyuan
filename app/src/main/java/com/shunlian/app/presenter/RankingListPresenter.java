package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RankingListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IRankingListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/9.
 */

public class RankingListPresenter extends BasePresenter <IRankingListView>{

    private String op_id;

    public RankingListPresenter(Context context, IRankingListView iView, String id) {
        super(context, iView);
        op_id = id;
        initApi();
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

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        map.put("op_cid",op_id);
        sortAndMD5(map);

        Call<BaseEntity<RankingListEntity>> baseEntityCall = getApiService().rankingList(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<RankingListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<RankingListEntity> entity) {

                iView.rankingCategoryList(entity.data.category);
                iView.rankingGoodsList(entity.data.goods);
            }
        });
    }
}
