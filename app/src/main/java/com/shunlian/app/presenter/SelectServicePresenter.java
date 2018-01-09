package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISelectServiceView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/27.
 */

public class SelectServicePresenter extends BasePresenter<ISelectServiceView> {

    public SelectServicePresenter(Context context, ISelectServiceView iView) {
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

    public void getRefundInfo(String ogId) {
        Map<String, String> map = new HashMap<>();
        map.put("og_id", ogId);
        sortAndMD5(map);

        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<RefundDetailEntity.RefundDetail.Edit>> baseEntityCall = getAddCookieApiService().getrefundinfo(requestBody);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<RefundDetailEntity.RefundDetail.Edit>>() {
            @Override
            public void onSuccess(BaseEntity<RefundDetailEntity.RefundDetail.Edit> entity) {
                if (entity.data != null) {
                    iView.getRefundInfo(entity.data);
                }
                super.onSuccess(entity);
            }
        });
    }
}
