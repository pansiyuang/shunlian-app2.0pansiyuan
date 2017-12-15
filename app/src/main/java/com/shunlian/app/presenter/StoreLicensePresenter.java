package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.StoreLicenseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.StoreLicenseView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/20.
 */

public class StoreLicensePresenter extends BasePresenter<StoreLicenseView> {

    public StoreLicensePresenter(Context context, StoreLicenseView iView) {
        super(context, iView);
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void checkLicense(String vcode,String seller_id) {
        Map<String, String> map = new HashMap<>();
        map.put("vcode", vcode);
        map.put("seller_id", seller_id);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<StoreLicenseEntity>> baseEntityCall = getAddCookieApiService().storeLicense(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreLicenseEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreLicenseEntity> entity) {
                super.onSuccess(entity);
                iView.setLicense(entity.data.business_licence_number_elc);
            }
        });
    }

    public void getCode() {
        Call<ResponseBody> responseBodyCall = getSaveCookieApiService().graphicalCode();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    byte[] bytes = response.body().bytes();
                    iView.setCode(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    @Override
    protected void initApi() {
        getCode();
    }

}
