package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FindSelectShopEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IFindSelectShopView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
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


    /**
     * 关注店铺
     * @param storeId
     */
    public void followStore(String storeId){
        if (Common.loginPrompt()){
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("storeIds",storeId);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addMark(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.followSuccess();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }
}
