package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMyPageView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/11/5.
 */

public class MyPagePresenter extends BasePresenter<IMyPageView> {

    public MyPagePresenter(Context context, IMyPageView iView) {
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

    public void setInfo(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> setinfo = getAddCookieApiService().setinfo(getRequestBody(map));
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                if ("signature".equals(key)) {
                    iView.setSignature(value);
                }
                Common.staticToast(entity.message);
            }
        });
    }

    public void getBlogList(String memberId, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("type", type);
        sortAndMD5(map);

        Call<BaseEntity<HotBlogsEntity>> baseEntityCall = getApiService().getblogs(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HotBlogsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HotBlogsEntity> entity) {
                super.onSuccess(entity);
                HotBlogsEntity hotBlogsEntity = entity.data;
                isLoading = false;
                iView.getFocusblogs(hotBlogsEntity);
                iView.refreshFinish();
            }

            @Override
            public void onFailure() {
                isLoading = false;
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                isLoading = false;
                super.onErrorCode(code, message);
            }
        });
    }

}
