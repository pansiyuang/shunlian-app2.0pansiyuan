package com.shunlian.app.utils;

import android.content.Context;
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

public class MVerticalItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int topMargin;
    private int bottomMargin;
    private Paint mPaint;

    public MVerticalItemDecoration(Context context,float space, float topMargin, float bottomMargin){
        this.space = TransformUtil.dip2px(context,space);
        this.topMargin = TransformUtil.dip2px(context,topMargin);
        this.bottomMargin = TransformUtil.dip2px(context,bottomMargin);
    }

    public MVerticalItemDecoration(Context context,float space, float topMargin, float bottomMargin, @ColorInt int colorId){
        this.space = TransformUtil.dip2px(context,space);
        this.topMargin = TransformUtil.dip2px(context,topMargin);
        this.bottomMargin = TransformUtil.dip2px(context,bottomMargin);
        mPaint = new Paint();
        mPaint.setColor(colorId);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
        outRect.top = topMargin;
        if (parent.getChildAdapterPosition(view) + 1 == state.getItemCount()) {
            if (bottomMargin != 0) {
                outRect.bottom = bottomMargin;
            } else {
                outRect.bottom = 0;
            }
        }
        if (topMargin != 0) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = topMargin;
            } else {
                outRect.top = 0;
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
