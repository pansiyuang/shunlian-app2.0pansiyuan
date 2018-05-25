package com.shunlian.app.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class CompileScrollView extends NestedScrollView {
    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private OnScrollListener onScrollListener;


    public CompileScrollView(Context context) {
        super(context);
    }

    public CompileScrollView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
    }

    public CompileScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScrollListener != null) {
//            if (y + getHeight() >= computeVerticalScrollRange()) {
//                //ScrollView滑动到底部了
//                onScrollListener.scrollCallBack(true, computeVerticalScrollRange(), y, oldy);
//            } else {
//                onScrollListener.scrollCallBack(false, computeVerticalScrollRange(), y, oldy);
//            }
            onScrollListener.scrollCallBack( y, oldy);
        }
    }

//    public interface OnScrollListener {
//        void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy);
//    }
    public interface OnScrollListener {
        void scrollCallBack( int y, int oldy);
    }
}


