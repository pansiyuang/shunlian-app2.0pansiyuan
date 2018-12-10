package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IAddGoodsView;
import com.shunlian.app.view.IView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/2/28.
 */

public class EmptyPresenter extends BasePresenter{

    public EmptyPresenter(Context context, IView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }


    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
