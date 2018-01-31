package com.shunlian.app.widget.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.shunlian.app.R;

/**
 * 简单月视图
 */


public class CustomMonthView extends MonthView {

    private int mRadius;
    private int markRadius;
    private int margin;
    private Paint markPaint;

    public CustomMonthView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = dip2px(getContext(), 14f);
        markRadius = dip2px(getContext(), 2f);
        margin = dip2px(getContext(), 6f);
        mSchemePaint.setStyle(Paint.Style.FILL);
        markPaint = new Paint();
        markPaint.setStyle(Paint.Style.FILL);
        markPaint.setAntiAlias(true);
        markPaint.setColor(getContext().getResources().getColor(R.color.pink_color));
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        mSelectedPaint.setColor(getContext().getResources().getColor(R.color.pink_color));
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        if (hasScheme) {
            cy = y + mItemHeight / 2 + margin + mRadius;
            canvas.drawCircle(cx, cy, markRadius, markPaint);
        }
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2 + margin + mRadius;
        canvas.drawCircle(cx, cy, markRadius, markPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    @Override
    protected void onDrawCurrentDay(Canvas canvas, Calendar calendar, int x, int y, boolean isCurrentDay) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        if (isCurrentDay) {
            mSelectedPaint.setColor(Color.parseColor("#FCEAEE"));
            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
