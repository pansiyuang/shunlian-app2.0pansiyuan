package com.shunlian.app.utils.timer;

import android.content.Context;
import android.util.AttributeSet;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2018/1/19.
 */

public class HoneRedDownTimerView extends ItemCountDownTimerView {

    public HoneRedDownTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HoneRedDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HoneRedDownTimerView(Context context) {
        super(context);
    }

    @Override
    protected int getStrokeColor() {
        return getResources().getColor(R.color.pink_color);
    }

    @Override
    protected int getBackgroundColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getTextColor() {
        return getResources().getColor(R.color.pink_color);
    }

    @Override
    protected int getCornerRadius() {
        return 5;
    }

    @Override
    protected int getTextSize() {
        return 7;
    }

    @Override
    protected boolean isTextBold() {
        return true;
    }

    @Override
    protected int getStrokeWidth() {
        return 0;
    }

    @Override
    protected int[] getLabelWidthHeight() {
        int[] wh = {10,10};
        return wh;
    }

    @Override
    public int[] getPadding() {
        return null;
    }

    @Override
    protected String[] getTimeUnit() {
//      String[] timeUnit = {"天","时","分","秒"};
        String[] timeUnit = {"天",":",":",""};
        return timeUnit;
    }

    @Override
    protected int getTimeUnitColor() {
        return getResources().getColor(R.color.pink_color);
    }

    @Override
    protected int getTimeUnitSize() {
        return 7;
    }

    @Override
    protected int[] getTimeUnitPadding() {
        int[] padding = {0,0,0,0};
        return padding;
    }

    @Override
    protected boolean getIsShowDay() {
        return true;
    }
}
