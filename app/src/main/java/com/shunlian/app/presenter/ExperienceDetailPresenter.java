package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IExperienceDetailView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ExperienceDetailPresenter extends BasePresenter<IExperienceDetailView> {

    private String mExperience_id;
    private final String page_size = "10";

    public ExperienceDetailPresenter(Context context, IExperienceDetailView iView,
                                     String experience_id) {
        super(context, iView);
        mExperience_id = experience_id;
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
    public void initApi() {
        Map<String,String> map = new HashMap<>();
        map.put("experience_id",mExperience_id);
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",page_size);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().experienceDetail(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);

            }
        });
    }
}
