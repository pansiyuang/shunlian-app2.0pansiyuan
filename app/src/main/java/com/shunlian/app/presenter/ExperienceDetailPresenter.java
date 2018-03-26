package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.view.IExperienceDetailView;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ExperienceDetailPresenter extends BasePresenter<IExperienceDetailView> {

    private String mExperience_id;

    public ExperienceDetailPresenter(Context context, IExperienceDetailView iView,
                                     String experience_id) {
        super(context, iView);
        mExperience_id = experience_id;
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

    }
}
