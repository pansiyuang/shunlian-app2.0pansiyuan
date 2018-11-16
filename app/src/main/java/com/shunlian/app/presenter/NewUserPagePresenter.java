package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMyPageView;
import com.shunlian.app.view.INewUserGoodsView;
import com.shunlian.app.view.INewUserPageView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/11/5.
 */

public class NewUserPagePresenter extends BasePresenter<INewUserPageView> {

    public NewUserPagePresenter(Context context, INewUserPageView iView) {
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

    public void adlist() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<AdUserEntity>> setinfo = getAddCookieApiService().adlist(map);
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<AdUserEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AdUserEntity> entity) {
                super.onSuccess(entity);
                iView.bannerList(entity.data.list,entity.data.isNew);
            }
        });

    }

    public void cartlist(boolean isGoBuy) {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<NewUserGoodsEntity>> setinfo = getAddCookieApiService().cartlist(map);
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<NewUserGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<NewUserGoodsEntity> entity) {
                super.onSuccess(entity);
                iView.goodCartList(entity.data.list,isGoBuy);
            }
        });
    }

    public void deletecart(String cid) {
        Map<String, String> map = new HashMap<>();
        map.put("cid",cid);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> setinfo = getAddCookieApiService().deletecart(map);
        getNetData(false, setinfo, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.delCart(cid);
            }
        });
    }
}
