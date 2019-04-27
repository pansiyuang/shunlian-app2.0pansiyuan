package com.shunlian.app.shunlianyoupin;

import android.content.Context;

import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.BasePresenter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Administrator on 2019/4/3.
 */

public class ShunlianyoupinPresenter extends BasePresenter<IYouPinBackView> {
    private int babyPage = 1;//当前页数
    private final int count = 5;//获取数量
    private int babyAllPage = 0;//总共页数
    private boolean babyIsLoading;
    private List<YouPingListEntity.lists> babyDatas = new ArrayList<>();


    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;

            initPingPaiData( babyPage, count);
        }
    }
    public void resetBaby() {
        babyPage = 1;
        babyIsLoading = true;
        babyDatas.clear();
        initPingPaiData( babyPage, count);
        initbanner("1");
    }

    public ShunlianyoupinPresenter(Context context, IYouPinBackView iView) {
        super(context, iView);
    }
    public void clear(){
        babyDatas.clear();
    }
    public void initPingPaiData(int page, int pageSize){
        HashMap<String, String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("pageSize",String.valueOf(pageSize));
        sortAndMD5(map);
        Call<BaseEntity<YouPingListEntity>> youlist = getApiService().getYoulist("http://api-mt-front.v2.shunliandongli.com/product/productlist",map);
        getNetData(true,youlist,new SimpleNetDataCallback<BaseEntity<YouPingListEntity>>(){
            public void onSuccess(BaseEntity<YouPingListEntity> entity) {
                super.onSuccess(entity);
                if (entity!=null&&entity.data!=null&&!isEmpty(entity.data.list)){
                    YouPingListEntity data = entity.data;
                    babyIsLoading = false;
                    babyPage++;
                    babyAllPage = Integer.parseInt(data.pager.total_page);
                    babyDatas.addAll(entity.data.list);
                    iView.getPingPaiData(data,babyAllPage,Integer.parseInt(data.pager.page),babyDatas);
                }else {
                    isLoading = false;
                    iView.showDataEmptyView(1);
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
                iView.showDataEmptyView(2);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
                iView.showDataEmptyView(2);
            }

        });

    }



    public void initbanner(String type){
        HashMap<String, String> map = new HashMap<>();
        map.put("type",type);
        sortAndMD5(map);
        Call<BaseEntity<YouPingbannerEntity>> baseEntityCall = getApiService().getYouPinBanner("http://api-mt-front.v2.shunliandongli.com/home/banners",map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<YouPingbannerEntity>>() {
            @Override
            public void onSuccess(BaseEntity<YouPingbannerEntity> entity) {
                super.onSuccess(entity);
                if (entity!=null&&entity.data!=null&&!isEmpty(entity.data.list)){
                    iView.getBannerData(entity.data.list);
                }else {
                    isLoading = false;
                    iView.showDataEmptyView(1);
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
                iView.showDataEmptyView(1);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
                iView.showDataEmptyView(1);
            }
        });

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



}
