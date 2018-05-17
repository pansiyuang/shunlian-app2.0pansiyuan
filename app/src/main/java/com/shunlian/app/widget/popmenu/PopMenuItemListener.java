package com.shunlian.app.widget.popmenu;

import android.view.View;

/**
 * Created by HanHailong on 16/2/18.
 */
public interface PopMenuItemListener {
    /**
     * Item点击事件
     *
     * @param popMenu
     * @param position
     */
    public void onItemClick(PopMenu popMenu, int position);

    /**
     * 点击关闭按钮
     * @param view
     */
    default void onClickClose(View view){}

    /**
     * 隐藏回调
     */
    default void onHideCallback(){}
}
