package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ICategoryView;
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

            @Override
            public void onFailure() {
                super.onFailure();
                iView.getFail();
            }
        });
    }
    @Override
    protected void initApi() {

    }

}
