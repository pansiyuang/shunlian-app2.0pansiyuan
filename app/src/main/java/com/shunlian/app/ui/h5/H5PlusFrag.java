package com.shunlian.app.ui.h5;

import com.shunlian.app.R;
import com.shunlian.app.bean.H5CallEntity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.mylibrary.ImmersionBar;

/**
 * Created by Administrator on 2017/12/26.
 */

public class H5PlusFrag extends H5Frag {


    protected void jsCallback(H5CallEntity h5CallEntity) {

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ImmersionBar.with(this).fitsSystemWindows(true)
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }
    @Override
    public void onBack() {
        super.onBack();
        ((MainActivity) activity).mainPageClick();
    }

}
