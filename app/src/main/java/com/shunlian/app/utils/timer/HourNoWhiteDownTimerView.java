package com.shunlian.app.utils.timer;

import android.content.Context;
import android.util.AttributeSet;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2018/1/19.
 */

public class HourNoWhiteDownTimerView extends ItemCountDownTimerView {

    public HourNoWhiteDownTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HourNoWhiteDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HourNoWhiteDownTimerView(Context context) {
        super(context);
    }

    @Override
    protected int getStrokeColor() {
        return getResources().getColor(R.color.pink_color);
    }

    @Override
    protected int getBackgroundColor() {
        return getResources().getColor(R.color.pink_color);
    }

    @Override
    protected int getTextColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getCornerRadius() {
        return 0;
    }

    @Override
    protected int getTextSize() {
        return 10;
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
      String[] timeUnit = {"天","小时","分","秒"};
//        String[] timeUnit = {":",":",":",""};
        return timeUnit;
    }

    @Override
    protected int getTimeUnitColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getTimeUnitSize() {
        return 12;
    }

    @Override
    protected int[] getTimeUnitPadding() {
        int[] padding = {2,2,2,2};
        return padding;
    }

    @Override
    protected boolean getIsShowDay() {
        return true;
    }
}
