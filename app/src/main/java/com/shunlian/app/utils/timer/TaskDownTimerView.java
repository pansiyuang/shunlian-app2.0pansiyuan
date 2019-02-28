package com.shunlian.app.utils.timer;

import android.content.Context;
import android.util.AttributeSet;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2018/1/19.
 */

public class TaskDownTimerView extends ItemCountDownTimerView {

    public TaskDownTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TaskDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskDownTimerView(Context context) {
        super(context);
    }

    @Override
    protected int getStrokeColor() {
        return getResources().getColor(R.color.task_orange);
    }

    @Override
    protected int getBackgroundColor() {
        return getResources().getColor(R.color.task_orange);
    }

    @Override
    protected int getTextColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getCornerRadius() {
        return 2;
    }

    @Override
    protected int getTextSize() {
        return 8;
    }

    @Override
    protected boolean isTextBold() {
        return false;
    }

    @Override
    protected int getStrokeWidth() {
        return 0;
    }

    @Override
    protected int[] getLabelWidthHeight() {
        int[] wh = {15,15};
        return wh;
    }

    @Override
    public int[] getPadding() {
        return null;
    }

    @Override
    protected String[] getTimeUnit() {
//      String[] timeUnit = {"天","时","分","秒"};
        String[] timeUnit = {":",":",":",""};
        return timeUnit;
    }

    @Override
    protected int getTimeUnitColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getTimeUnitSize() {
        return 14;
    }

    @Override
    protected int[] getTimeUnitPadding() {
        int[] padding = {2,0,2,2};
        return padding;
    }

    @Override
    protected boolean getIsShowDay() {
        return false;
    }
}
