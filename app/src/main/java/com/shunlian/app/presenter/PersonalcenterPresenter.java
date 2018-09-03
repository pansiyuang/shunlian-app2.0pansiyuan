package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPersonalView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/5.
 */

public class PersonalcenterPresenter extends BasePresenter<IPersonalView> {

    public PersonalcenterPresenter(Context context, IPersonalView iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }


    /**
     *
     * @param
     */
    public void getApiData(){
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<PersonalcenterEntity>> baseEntityCall = getApiService().personalcenter(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<PersonalcenterEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PersonalcenterEntity> entity) {
                super.onSuccess(entity);
                PersonalcenterEntity personalcenterEntity=entity.data;
                iView.getApiData(personalcenterEntity);
            }

        });
    }
    @Override
    protected void initApi() {

    }

}
