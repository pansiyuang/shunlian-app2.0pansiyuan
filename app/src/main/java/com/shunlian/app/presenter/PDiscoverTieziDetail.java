package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DiscoverSucaikuAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.DiscoveryCommentListEntity;
import com.shunlian.app.bean.DiscoveryTieziEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IDiscoverTiezi;
import com.shunlian.app.view.IDiscoverTieziDetail;
import com.shunlian.app.view.IFindCommentListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PDiscoverTieziDetail extends BasePresenter<IDiscoverTieziDetail> {
    private int pageSize=20;
    private boolean isFirst=true;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading;
    private String circle_id;
    private String inv_id;
    private List<DiscoveryCommentListEntity.Mdata.Commentlist> mDatas = new ArrayList<>();

    public PDiscoverTieziDetail(Context context, IDiscoverTieziDetail iView, String circle_id,String inv_id) {
        super(context, iView);
        this.circle_id=circle_id;
        this.inv_id=inv_id;
        getApiData(babyPage,circle_id,inv_id);
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
            getApiData(babyPage,circle_id,inv_id);
        }
    }

    public void getApiData(int page,String circle_id,String inv_id){
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("circle_id", circle_id);
        map.put("inv_id", inv_id);
        map.put("pageSize", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<DiscoveryCommentListEntity>> baseEntityCall = getApiService().discoveryCommentList(map);
        getNetData(isFirst,baseEntityCall, new SimpleNetDataCallback<BaseEntity<DiscoveryCommentListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DiscoveryCommentListEntity> entity) {
                super.onSuccess(entity);
                DiscoveryCommentListEntity data =entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(data.list.total_page);
                mDatas.addAll(data.list.commentlist);
                iView.setApiData(data.list,mDatas);
                isFirst=false;
                iView.setCommentAllCount(data.list.commentcounts);
            }
        });
    }

    public void dianZan(String circle_id,String inv_id, String status){
        Map<String, String> map = new HashMap<>();
        map.put("circle_id", circle_id);
        map.put("inv_id", inv_id);
        map.put("status", status);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().circleInvLike(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity=entity.data;
                iView.dianZan(commonEntity);
            }
        });
    }


    public void faBu(String circle_id,String inv_id, String content){
        Map<String, String> map = new HashMap<>();
        map.put("circle_id", circle_id);
        map.put("inv_id", inv_id);
        map.put("content", content);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().circleAddComment(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(getStringResouce(R.string.discover_pinglunchenggong));
            }
        });
    }
}
