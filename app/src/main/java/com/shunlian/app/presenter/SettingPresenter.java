package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.view.ISettingView;

/**
 * Created by Administrator on 2018/4/24.
 */

public class SettingPresenter extends BasePresenter<ISettingView> {


    public SettingPresenter(Context context, ISettingView iView) {
        super(context, iView);
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
    protected void initApi() {

    }
}
