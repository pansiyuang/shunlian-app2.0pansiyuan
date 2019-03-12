package com.shunlian.app.utils.timer;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by MBENBEN on 2016/11/1 13 : 23.
 */

public abstract class ItemCountDownTimerView extends LinearLayout {

    private Context mContext;

    /**
     * 倒计时控制器
     */
    private CountDownTimer mCountDownTimer;

    private OnCountDownTimerListener OnCountDownTimerListener;

    private long mSeconds;

    /**
     * 天
     */
    private TextView mDayTextView;
    /**
     * 时
     */
    private TextView mHourTextView;

    /**
     * 分
     */
    private TextView mMinTextView;

    /**
     * 秒
     */
    private TextView mSecondTextView;
    private TextView mTimeUnitDay;
    private TextView mTimeUnitSec;
    private TextView mTimeUnitMin;
    private TextView mTtimeUnitHour;


    public ItemCountDownTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public ItemCountDownTimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemCountDownTimerView(Context context) {
        this(context, null);
    }

    /**
     * 获取边框颜色
     *
     * @return
     */
    protected abstract int getStrokeColor();

    /**
     * 设置背景色
     *
     * @return
     */
    protected abstract int getBackgroundColor();

    /**
     * 获取文字颜色
     *
     * @return
     */
    protected abstract int getTextColor();

    /**
     * 获取边框圆角
     *
     * @return
     */
    protected abstract int getCornerRadius();

    /**
     * 获取标签文字大小
     *
     * @return
     */
    protected abstract int getTextSize();

    /**
     * 标签文字是否加粗
     *
     * @return
     */
    protected abstract boolean isTextBold();

    /**
     * 设置边框宽
     *
     * @return
     */
    protected abstract int getStrokeWidth();

    /**
     * 获取标签宽高
     *
     * @return
     */
    protected abstract int[] getLabelWidthHeight();

    /**
     * 设置定时器数值的padding值
     *
     * @return
     */
    public abstract int[] getPadding();

    /***
     * 获取时间单位
     *
     * @return
     */
    protected abstract String[] getTimeUnit();


    /**
     * 设置时间单位颜色
     *
     * @return
     */
    protected abstract int getTimeUnitColor();

    /***
     * 时间单位文字大小
     * @return
     */
    protected abstract int getTimeUnitSize();

    /**
     * 设置时间单位padding
     *
     * @return
     */
    protected abstract int[] getTimeUnitPadding();

    /**
     * 设置时间字体大小
     *
     * @param padding
     */
    public void setTimeUnitPadding(int padding) {
        int i = dip2px(padding);
        if (getIsShowDay()) {
            mTimeUnitDay.setPadding(i, i, i, i);
        }
        mTtimeUnitHour.setPadding(i, i, i, i);
        mTimeUnitMin.setPadding(i, i, i, i);
        mTimeUnitSec.setPadding(i, i, i, i);
        requestLayout();
    }

    /**
     * 是否显示天
     *
     * @return
     */
    protected abstract boolean getIsShowDay();

    private void init() {
        this.setOrientation(HORIZONTAL);// 设置布局排列方式
        setGravity(Gravity.CENTER);

        createView(getPadding());// 创造三个标签
        addLabelView(getTimeUnit());// 添加标签到容器中
    }

    /**
     * 创建时、分、秒的标签
     */
    public void createView(int[] padding) {
        if (!(padding != null && padding.length == 4)) {
            padding = new int[]{0, 0, 0, 0};
        }
        if (getIsShowDay()) {
            mDayTextView = createLabel(padding[0], padding[1], padding[2], padding[3]);
        }
        mHourTextView = createLabel(padding[0], padding[1], padding[2], padding[3]);
        mMinTextView = createLabel(padding[0], padding[1], padding[2], padding[3]);
        mSecondTextView = createLabel(padding[0], padding[1], padding[2], padding[3]);
    }

    /**
     * 添加标签到容器中
     */
    public void addLabelView(String[] parame) {
        removeAllViews();
        if (getIsShowDay()) {//天
            this.addView(mDayTextView);
            mTimeUnitDay = createColon(parame[0]);
            this.addView(mTimeUnitDay);
        }
        //时
        this.addView(mHourTextView);
        mTtimeUnitHour = createColon(parame[1]);
        this.addView(mTtimeUnitHour);
        //分
        this.addView(mMinTextView);
        mTimeUnitMin = createColon(parame[2]);
        this.addView(mTimeUnitMin);
        //秒
        this.addView(mSecondTextView);
        mTimeUnitSec = createColon(parame[3]);
        this.addView(mTimeUnitSec);
    }

    /**
     * 创建标签
     *
     * @return
     */
    private TextView createLabel(int left, int top, int right, int bottom) {
        TextView textView = new GradientTextView(mContext)
                .setTextColor(getTextColor())
                .setStrokeColor(getStrokeWidth(), getStrokeColor())
                .setBackgroundColor(getBackgroundColor())
                .setTextSize(getTextSize())
                .setStrokeRadius(getCornerRadius())
                .setTextWH(getLabelWidthHeight())
                .build();
        textView.setPadding(dip2px(left), dip2px(top), dip2px(right), dip2px(bottom));
        if (isTextBold()) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        return textView;
    }

    /**
     * 创建冒号
     *
     * @return
     */
    private TextView createColon(String divide) {
        TextView textView = new TextView(mContext);
        int color = getTimeUnitColor();
        if (color != 0) {
            textView.setTextColor(color);
        }
        textView.setTextSize(getTimeUnitSize());
        textView.setText(divide);
        int[] timeUnitPadding = getTimeUnitPadding();
        if (!(timeUnitPadding != null && timeUnitPadding.length == 4)) {
            timeUnitPadding = new int[]{0, 0, 0, 0};
        }
        textView.setPadding(dip2px(timeUnitPadding[0]), dip2px(timeUnitPadding[1]),
                dip2px(timeUnitPadding[2]), dip2px(timeUnitPadding[3]));
        return textView;
    }

    /**
     * 创建倒计时
     */
    private void createCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mSeconds * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                setSecond(millisUntilFinished);// 设置秒
            }

            @Override
            public void onFinish() {
                //此处会在-1秒执行，且计时器在1秒时就停止调用onTick方法了
//                OnCountDownTimerListener.onFinish();
            }
        };
    }

    /**
     * 设置秒
     *
     * @param millis
     */
    private void setSecond(long millis) {
        long totalSeconds = millis / 1000;
        String second = (int) (totalSeconds % 60) + "";// 秒
        long totalMinutes = totalSeconds / 60;
        String minute = (int) (totalMinutes % 60) + "";// 分
        long totalHours = totalMinutes / 60;
        String hour = "";
        String day = "";
        if (getIsShowDay()) {
            hour = (int) (totalHours % 24) + "";// 时
            long totalDays = totalHours / 24;
            day = (int) (totalDays % 100) + "";// 天
        } else {
            hour = (int) (totalHours) + "";// 时
        }
        if (!empty(day) && day.length() == 1) {
            day = "0" + day;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }

        if (!empty(day)) {
            if (!"00".equals(day)) {
                mDayTextView.setText(day);
                mDayTextView.setVisibility(VISIBLE);
                mTimeUnitDay.setVisibility(VISIBLE);
            } else {
                mDayTextView.setVisibility(GONE);
                mTimeUnitDay.setVisibility(GONE);
            }
        }

//        if (!"00".equals(hour)) {
            mHourTextView.setText(hour);
//            mHourTextView.setVisibility(VISIBLE);
//            mTtimeUnitHour.setVisibility(VISIBLE);
//        } else {
//            mHourTextView.setVisibility(GONE);
//            mTtimeUnitHour.setVisibility(GONE);
//        }
//        if (!"00".equals(minute)) {
            mMinTextView.setText(minute);
//            mMinTextView.setVisibility(VISIBLE);
//            mTimeUnitMin.setVisibility(VISIBLE);
//        } else {
//            mMinTextView.setVisibility(GONE);
//            mTimeUnitMin.setVisibility(GONE);
//        }
        mSecondTextView.setText(second);
        boolean isDay = false;
        if (getIsShowDay()) {
            isDay = "00".equals(day);
        } else {
            isDay = true;
        }
        if (isDay && "00".equals(hour) && "00".equals(minute) && "01".equals(second)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSecondTextView.setText("00");
                    if (OnCountDownTimerListener != null) {
                        OnCountDownTimerListener.onFinish();
                    }
                }
            }, 1000);
        }
    }

    /**
     * 设置监听事件
     *
     * @param listener
     */
    public void setDownTimerListener(OnCountDownTimerListener listener) {
        this.OnCountDownTimerListener = listener;
    }

    /**
     * 设置时间值
     *
     * @param seconds
     */
    public void setDownTime(long seconds) {
        this.mSeconds = seconds;
    }

    /**
     * 设置时间背景色
     *
     * @param color
     */
    public void setLabelBackgroundColor(int color) {
        if (getIsShowDay()) {
            GradientDrawable backgroundDay = (GradientDrawable) mDayTextView.getBackground();
            backgroundDay.setColor(color);
        }
        GradientDrawable backgroundHour = (GradientDrawable) mHourTextView.getBackground();
        backgroundHour.setColor(color);
        GradientDrawable backgroundMin = (GradientDrawable) mMinTextView.getBackground();
        backgroundMin.setColor(color);
        GradientDrawable backgroundSec = (GradientDrawable) mSecondTextView.getBackground();
        backgroundSec.setColor(color);
        requestLayout();
    }

    /**
     * 设置时间颜色
     *
     * @param color
     */
    public void setTimeTextColor(int color) {
        if (getIsShowDay()) {
            mDayTextView.setTextColor(color);
        }
        mHourTextView.setTextColor(color);
        mMinTextView.setTextColor(color);
        mSecondTextView.setTextColor(color);
        requestLayout();
    }

    /**
     * 设置时间字体大小
     *
     * @param size
     */
    public void setTimeTextSize(int size) {
        if (getIsShowDay()) {
            mDayTextView.setTextSize(size);
        }
        mHourTextView.setTextSize(size);
        mMinTextView.setTextSize(size);
        mSecondTextView.setTextSize(size);
        requestLayout();
    }

    /**
     * 设置时间单位的字体颜色
     *
     * @param color
     */
    public void setTimeUnitTextColor(int color) {
        if (getIsShowDay()) {
            mTimeUnitDay.setTextColor(color);
        }
        mTtimeUnitHour.setTextColor(color);
        mTimeUnitMin.setTextColor(color);
        mTimeUnitSec.setTextColor(color);
        requestLayout();
    }

    /**
     * 设置时间字体大小
     *
     * @param size
     */
    public void setTimeUnitTextSize(int size) {
        if (getIsShowDay()) {
            mTimeUnitDay.setTextSize(size);
        }
        mTtimeUnitHour.setTextSize(size);
        mTimeUnitMin.setTextSize(size);
        mTimeUnitSec.setTextSize(size);
        requestLayout();
    }

    /**
     * 开始倒计时
     */
    public void startDownTimer() {
        if (mSeconds <= 1) {
            if (getIsShowDay()) {
                mDayTextView.setText("00");
            }
            mHourTextView.setText("00");
            mMinTextView.setText("00");
            mSecondTextView.setText("00");
        } else {
            createCountDownTimer();// 创建倒计时
            mCountDownTimer.start();
        }
    }

    public void cancelDownTimer() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
    }

    /**
     * dp-->px
     *
     * @param dipValue
     * @return
     */
    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    private boolean empty(String str) {
        return TextUtils.isEmpty(str);
    }
}
