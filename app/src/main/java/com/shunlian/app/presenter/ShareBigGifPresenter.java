package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PlusDataEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IShareBifGifView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/28.
 */

public class ShareBigGifPresenter extends BasePresenter<IShareBifGifView> {

    public ShareBigGifPresenter(Context context, IShareBifGifView iView) {
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

    public void getPlusData() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<PlusDataEntity>> baseEntityCall = getAddCookieApiService().getPlusData(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<PlusDataEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PlusDataEntity> entity) {
                super.onSuccess(entity);
                iView.getPlusData(entity.data);
            }
        });
    }
}
