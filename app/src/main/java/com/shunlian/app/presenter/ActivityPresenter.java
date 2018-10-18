package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.view.IActivityView;
import com.shunlian.app.view.IView;

/**
 * Created by Administrator on 2018/10/18.
 */

public class ActivityPresenter extends BasePresenter implements IActivityView {
    public static final int PAGE_SIZE = 10;
    public String currentKeyword;

    public ActivityPresenter(Context context, IView iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    protected void initApi() {

    }
}
