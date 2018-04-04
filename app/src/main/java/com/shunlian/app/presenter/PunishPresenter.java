package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.view.IPunishView;

/**
 * Created by Administrator on 2018/4/3.
 */

public class PunishPresenter extends BasePresenter<IPunishView> {

    public PunishPresenter(Context context, IPunishView iView) {
        super(context, iView);
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
