package com.shunlian.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class MyScrollView extends ScrollView {
    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private OnScrollListener onScrollListener;

    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
        return this;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScrollListener != null) {
            if (y + getHeight() >= computeVerticalScrollRange()) {
                //ScrollView滑动到底部了
                onScrollListener.scrollCallBack(true, computeVerticalScrollRange(), y, oldy);
            } else {
                onScrollListener.scrollCallBack(false, computeVerticalScrollRange(), y, oldy);
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    public interface OnScrollListener {
        void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy);
    }
}


