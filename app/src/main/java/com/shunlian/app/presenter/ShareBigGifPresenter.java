package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PlusDataEntity;
import com.shunlian.app.bean.PlusMemberEntity;
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

    public void getPlusData(int type,boolean isFirst) {
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(type));
        sortAndMD5(map);

        Call<BaseEntity<PlusDataEntity>> baseEntityCall = getAddCookieApiService().getPlusData(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<PlusDataEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PlusDataEntity> entity) {
                super.onSuccess(entity);
                iView.getPlusData(entity.data);
            }
        });
    }

    public void getPlusMember() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<PlusMemberEntity>> baseEntityCall = getAddCookieApiService().getPlusMember(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<PlusMemberEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PlusMemberEntity> entity) {
                super.onSuccess(entity);
                iView.getPlusMember(entity.data.list);
            }
        });
    }
}
