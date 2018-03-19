package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GuanzhuEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IGuanzhuView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/16.
 */

public class GuanzhuPresenter extends BasePresenter<IGuanzhuView> {

    public GuanzhuPresenter(Context context, IGuanzhuView iView) {
        super(context, iView);
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<GuanzhuEntity>> baseEntityCall = getApiService().foucsHome(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<GuanzhuEntity>>(){
            @Override
            public void onSuccess(BaseEntity<GuanzhuEntity> entity) {
                super.onSuccess(entity);
                GuanzhuEntity data = entity.data;
                currentPage = Integer.parseInt(data.page);
                allPage = Integer.parseInt(data.total_page);
                iView.setGuanzhuList(data.dynamic_list,currentPage,allPage);
            }
        });
    }
}
