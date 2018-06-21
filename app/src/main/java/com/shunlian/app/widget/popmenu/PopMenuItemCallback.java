package com.shunlian.app.widget.popmenu;

import android.app.Activity;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.mylibrary.ImmersionBar;

/**
 * Created by HanHailong on 16/2/18.
 */
public abstract class PopMenuItemCallback implements PopMenuItemListener{

    /**
     * 点击关闭按钮
     * @param view
     */
     public void onClickClose(View view){}

    /**
     * 隐藏回调
     */
    public void onHideCallback(Activity mActivity){
        ImmersionBar.with(mActivity).statusBarColor(R.color.white).
                statusBarDarkFont(true, 0.2f).init();
    }
}
