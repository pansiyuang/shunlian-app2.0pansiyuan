package com.shunlian.app.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/11/16.
 */

public class MyNestedScrollView extends NestedScrollView {
    private OnScrollListener onScrollListener;

    public MyNestedScrollView(Context context) {
        this(context, null);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        Rect rect = new Rect();
//        getHitRect(rect);
//        MyTextView mtv_xiangqing = (MyTextView) findViewById(R.id.mtv_xiangqing);
//        Point point = new Point();
//        boolean childVisibleRect = getChildVisibleRect(mtv_xiangqing, rect, point);
//        System.out.println("====childVisibleRect======"+childVisibleRect);
//        System.out.println("====rect======"+rect);
//        System.out.println("====point======"+point);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        System.out.println("l==" + l + "  t==" + t + "  oldl==" + oldl + "  oldt==" + oldt);
        if (onScrollListener != null) {
            //ScrollView滑动到底部了
            onScrollListener.scrollCallBack(true, computeVerticalScrollRange(), t, oldt);
        } else {
            onScrollListener.scrollCallBack(false, computeVerticalScrollRange(), t, oldt);
        }
    }

    public interface OnScrollListener {
        void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }
}
