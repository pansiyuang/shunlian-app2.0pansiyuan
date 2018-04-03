package com.shunlian.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class MyWebView extends WebView {

    private ScrollListener scrollListener;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs,
                     int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
        return this;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        if (y + getHeight() >= computeVerticalScrollRange()) {
            //WebView滑动到底部了
            if (scrollListener != null) {
                scrollListener.scrollCallBack(true, computeVerticalScrollRange(), y, oldy);
            }
        } else {
            if (scrollListener != null) {
                scrollListener.scrollCallBack(false, computeVerticalScrollRange(), y, oldy);
            }
        }
    }

    public interface ScrollListener {
        void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy);
    }

}


