package com.shunlian.app.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/11/24.
 */

public class VerticalItemDecoration extends RecyclerView.ItemDecoration {
    //item少，当前屏幕能全部显示的情况下，会失效
    private int space;
    private int topMargin;
    private int bottomMargin;
    private Paint mPaint;

    public VerticalItemDecoration(int space, int topMargin, int bottomMargin){

        this.space = space;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
    }

    public VerticalItemDecoration(int space, int topMargin, int bottomMargin, @ColorInt int colorId){
        this.space = space;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
        mPaint = new Paint();
        mPaint.setColor(colorId);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
        outRect.top = topMargin;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            int lastPosition = manager.findLastVisibleItemPosition();
            if (lastPosition + 1 == manager.getItemCount()){
                outRect.bottom = bottomMargin;
            }
        }
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mPaint != null)
            drawHorizontal(c, parent);
    }

    //绘制横向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + space;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
