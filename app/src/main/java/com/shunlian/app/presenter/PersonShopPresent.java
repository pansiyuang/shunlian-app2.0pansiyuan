package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PersonShopEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPersonShopView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/2.
 */

public class PersonShopPresent extends BasePresenter<IPersonShopView> {

    public PersonShopPresent(Context context, IPersonShopView iView) {
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

    public void getPersonShopData(String memberId){
        Map<String, String> map = new HashMap<>();
        map.put("member_id", memberId);
        sortAndMD5(map);
        Call<BaseEntity<PersonShopEntity>> baseEntityCall = getApiService().personShop(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<PersonShopEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PersonShopEntity> entity) {
                super.onSuccess(entity);
                PersonShopEntity personShopEntity = entity.data;
                iView.getPersonShop(personShopEntity);
            }
        });
    }
}
