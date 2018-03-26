package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.adapter.DiscoverNewAdapter;
import com.shunlian.app.adapter.DiscoverSucaikuAdapter;
import com.shunlian.app.adapter.TieziCommentAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IADiscoverSucaiku;
import com.shunlian.app.view.ICategoryView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/5.
 */

public class PADiscoverSucaiku extends BasePresenter<IADiscoverSucaiku> {

    public PADiscoverSucaiku(Context context, IADiscoverSucaiku iView) {
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
    public void dianZan(String material_id, String status, final DiscoverSucaikuAdapter.SucaikuHolder viewHolder){
        Map<String, String> map = new HashMap<>();
        map.put("material_id", material_id);
        map.put("status", status);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().discoveryPraise(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.dianZan(viewHolder);
            }
        });
    }

    public void dianZans(String circle_id, String status, final DiscoverNewAdapter.NewHolder viewHolder){
        Map<String, String> map = new HashMap<>();
        map.put("circle_id", circle_id);
        map.put("status", status);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().circleTopicLike(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.dianZans(viewHolder);
            }
        });
    }

    public void dianZanss(String circle_id, String comment_id,String inv_id,String status, final TieziCommentAdapter.CommentHolder viewHolder){
        Map<String, String> map = new HashMap<>();
        map.put("circle_id", circle_id);
        map.put("comment_id", comment_id);
        map.put("inv_id", inv_id);
        map.put("status", status);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().circleCommentLike(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.dianZanss(viewHolder);
            }
        });
    }


}
