package com.shunlian.app.utils.timer;

import android.content.Context;
import android.util.AttributeSet;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2018/1/19.
 */

public class DayNoBlackDownTimerView extends ItemCountDownTimerView {

    public DayNoBlackDownTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DayNoBlackDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayNoBlackDownTimerView(Context context) {
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
        return getResources().getColor(R.color.new_text);
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
      String[] timeUnit = {"天","小时","分钟","秒结束"};
//        String[] timeUnit = {":",":",":",""};
        return timeUnit;
    }

    @Override
    protected int getTimeUnitColor() {
        return getResources().getColor(R.color.new_text);
    }

    @Override
    protected int getTimeUnitSize() {
        return 10;
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
