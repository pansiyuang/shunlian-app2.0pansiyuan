package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.HelpSearchEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISearchQView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PSearchQuestion extends BasePresenter<ISearchQView> {

    public PSearchQuestion(Context context, ISearchQView iView) {
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

    public void getSearchTips(String keyWord) {
        Map<String, String> map = new HashMap<>();
        map.put("keyword", keyWord);
        sortAndMD5(map);
        Call<BaseEntity<HelpSearchEntity>> baseEntityCall = getApiService().helpcenterSearch(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HelpSearchEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HelpSearchEntity> entity) {
                super.onSuccess(entity);
                HelpSearchEntity data = entity.data;
                if (data != null) {
                    iView.setApiData(data.list);
                }
            }
        });
    }
}
