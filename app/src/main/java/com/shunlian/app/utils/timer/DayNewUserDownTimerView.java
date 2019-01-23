package com.shunlian.app.utils.timer;

import android.content.Context;
import android.util.AttributeSet;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2018/1/19.
 */

public class DayNewUserDownTimerView extends ItemCountDownTimerView {

    public DayNewUserDownTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DayNewUserDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayNewUserDownTimerView(Context context) {
        super(context);
    }

    @Override
    protected int getStrokeColor() {
        return getResources().getColor(R.color.white_10dim);
    }

    @Override
    protected int getBackgroundColor() {
        return getResources().getColor(R.color.white_10dim);
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
        return 13;
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
        int[] wh = {22,15};
        return wh;
    }

    @Override
    public int[] getPadding() {
        return new int[]{0,-2,0,2};
    }

    @Override
    protected String[] getTimeUnit() {
      String[] timeUnit = {"天","时","分","秒"};
//        String[] timeUnit = {":",":",":",""};
        return timeUnit;
    }

    @Override
    protected int getTimeUnitColor() {
        return getResources().getColor(R.color.value_8f8f8f);
    }

    @Override
    protected int getTimeUnitSize() {
        return 14;
    }

    @Override
    protected int[] getTimeUnitPadding() {
        int[] padding = {5,5,5,5};
        return padding;
    }

    @Override
    protected boolean getIsShowDay() {
        return true;
    }
}
