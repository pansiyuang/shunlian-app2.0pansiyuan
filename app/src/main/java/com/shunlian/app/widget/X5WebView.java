package com.shunlian.app.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class X5WebView extends WebView {
    private int mMaxHeight = -1;
    private ScrollListener scrollListener;

    public X5WebView(Context context) {
        super(context);
    }

    public X5WebView(Context context, AttributeSet attrs,
                     int defStyle) {
        super(context, attrs, defStyle);
    }


    public X5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public X5WebView setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
        return this;
    }

    public void setMaxHeight(int height){
        mMaxHeight = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(mMaxHeight>-1 && getMeasuredHeight()>mMaxHeight){
            setMeasuredDimension(getMeasuredWidth(), mMaxHeight);
        }
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


