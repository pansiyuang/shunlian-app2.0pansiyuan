package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.ServiceEntity;
import com.shunlian.app.view.ISwitchOtherView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/20.
 */

public class SwitchOtherPresenter extends BasePresenter<ISwitchOtherView> {

    public SwitchOtherPresenter(Context context, ISwitchOtherView iView) {
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

    public void getTransferChatUserList(String platformType, String shopId, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("platform_type", platformType);
        if (!isEmpty(shopId)) {
            map.put("shop_id", shopId);
        }
        map.put("user_id", userId);
        sortAndMD5(map);
        Call<BaseEntity<ServiceEntity>> baseEntityCall = getAddCookieApiService().getTransferChatUserList(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ServiceEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ServiceEntity> entity) {
                ServiceEntity serviceEntity = entity.data;
                iView.getMemberList(serviceEntity.list,serviceEntity.service_num);
                super.onSuccess(entity);
            }
        });

    }
}
