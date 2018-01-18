package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.JoinGoodsEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IRecommmendView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/11.
 */

public class RecommmendPresenter extends BasePresenter<IRecommmendView> {

    public RecommmendPresenter(Context context, IRecommmendView iView) {
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

    public void getRecommmendGoods(String joinSign, String cateId) {
        Map<String, String> map = new HashMap<>();
        map.put("join_sign", joinSign);
        map.put("cate_id", cateId);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<JoinGoodsEntity>> baseEntityCall = getApiService().getRecommmendGoods(requestBody);
            getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<JoinGoodsEntity>>() {
                @Override
                public void onSuccess(BaseEntity<JoinGoodsEntity> entity) {
                    JoinGoodsEntity joinGoodsEntity = entity.data;
                    if (joinGoodsEntity != null) {
                        iView.getJoinGoods(joinGoodsEntity);
                    }
                    super.onSuccess(entity);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
