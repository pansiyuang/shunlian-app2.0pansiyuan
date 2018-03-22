package com.shunlian.app.widget.nestedrefresh.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.shunlian.app.widget.nestedrefresh.interf.DragListener;
import com.shunlian.app.widget.nestedrefresh.interf.onLoadMoreListener;


/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/12/11
 * Version  1.0
 * Description:
 */

public class BaseFooter extends FrameLayout implements onLoadMoreListener,DragListener {


    public BaseFooter(Context context) {
        super(context);
    }

    public BaseFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onDrag(int dy,int offset) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onReset() {

    }

    @Override
    public int getDragMaxOffset(View rootView,View target,int targetHeigth) {
        return 0;
    }

    @Override
    public int getDragTriggerOffset(View rootView,View target,int targetHeigth) {
        return 0;
    }

    @Override
    public int getRefreshOrLoadMoreHeight(View rootView, View target, int targetHeight) {
        return 0;
    }
    @Override
    public void onLoadMore() {

    }
}
