package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ILookBigImgView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/11/9.
 */

public class LookBigImgPresenter extends BasePresenter<ILookBigImgView> {

    public LookBigImgPresenter(Context context, ILookBigImgView iView) {
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

    public void goodsShare(String type, String blogId, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("id", id);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getSaveCookieApiService().shareSuccessCall(getRequestBody(map));
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.shareGoodsSuccess(blogId, id);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });
    }
}
