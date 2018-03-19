package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.FindSelectShopEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IFindSelectShopView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/15.
 */

public class FindSelectShopPresenter extends BasePresenter<IFindSelectShopView> {

    public FindSelectShopPresenter(Context context, IFindSelectShopView iView) {
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

        Call<BaseEntity<FindSelectShopEntity>> baseEntityCall = getApiService().recommendFollow(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<FindSelectShopEntity>>(){
            @Override
            public void onSuccess(BaseEntity<FindSelectShopEntity> entity) {
                super.onSuccess(entity);
                iView.setList(entity.data.store_list);
            }
        });

    }
}
