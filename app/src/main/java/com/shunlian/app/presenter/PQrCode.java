package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GetQrCardEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IQrCode;
import com.shunlian.app.view.ISignInView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PQrCode extends BasePresenter<IQrCode> {
    public PQrCode(Context context, IQrCode iView) {
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
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<GetQrCardEntity>> baseEntityCall = getApiService().userGetQrCard(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetQrCardEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetQrCardEntity> entity) {
                super.onSuccess(entity);
                GetQrCardEntity data = entity.data;
                if (data != null) {
                    LogUtil.httpLogW("GetQrCardEntity:" + data);
                    iView.setApiData(data);
                }
            }
        });
    }

}
