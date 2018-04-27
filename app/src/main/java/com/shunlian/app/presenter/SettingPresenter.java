package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ISettingView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/24.
 */

public class SettingPresenter extends BasePresenter<ISettingView> {


    public SettingPresenter(Context context, ISettingView iView) {
        super(context, iView);
        initApi();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> setting = getApiService().setting(map);
        getNetData(true,setting,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                iView.pushSwitch(data.push_on);
                iView.aboutUrl(data.about_url);
                iView.downAppQRCode(data.qr_img_for_app_down);
            }
        });
    }


    public void updatePushSetting(String push_on){
        Map<String,String> map = new HashMap<>();
        map.put("push_on",push_on);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .updatePushSet(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback
                <BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
            }
        });
    }
}
