package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.view.IBusinessCardView;

/**
 * Created by Administrator on 2018/4/25.
 */

public class BusinessCardPresenter extends BasePresenter<IBusinessCardView> {


    public BusinessCardPresenter(Context context, IBusinessCardView iView) {
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
