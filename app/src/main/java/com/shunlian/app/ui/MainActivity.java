package com.shunlian.app.ui;

import com.shunlian.app.R;
import com.shunlian.app.presenter.TestPresenter;

public class MainActivity extends BaseActivity {


    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        TestPresenter testPresenter = new TestPresenter();


    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
