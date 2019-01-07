package com.shunlian.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by zhanghe on 2018/9/1.
 */

public class DowntimeLayout extends View {


    private Paint mPaintBG;
    private int rad;
    private int progressrad;
    private int gap;
    private Paint mProgressPaint;
    float currentProgressW;
    private int maxProgressW;
    private int maxProgressH;
    private RectF bgRect;
    private RectF progressRect;
    private Paint mTextPaint;
    private int grayColor;
    private String format = "%s:%s:%s";
    private String text = "";
    private PorterDuffXfermode xformode;
    private Bitmap srcBitmap;
    private CountDownTimer mCountDownTimer;
    private long mSeconds;
    private long mMaxProgress = 1;
    private float mProgresRatio;
    public static final String RECEIVE = "领取金蛋";
    private DownTimeCompleteListener mListener;
    private OnClickListener mLi;

    public DowntimeLayout(Context context) {
        super(context);
        init(context);
    }


    public DowntimeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public DowntimeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rad = TransformUtil.dip2px(context, 10f);
        grayColor = getResources().getColor(R.color.value_484848);
        mPaintBG = new Paint();
        mPaintBG.setAntiAlias(true);
        mPaintBG.setColor(Color.WHITE);
        mPaintBG.setStyle(Paint.Style.FILL);

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(getResources().getColor(R.color.pink_color));
        mProgressPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(grayColor);
        mTextPaint.setTextSize(rad);

        progressrad = TransformUtil.dip2px(context, 9f);
        gap = rad / 10;

        bgRect = new RectF();
        progressRect = new RectF();
        xformode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        bgRect.left = 0;
        bgRect.top = 0;
        bgRect.right = measuredWidth;
        bgRect.bottom = measuredHeight;
        canvas.drawRoundRect(bgRect, rad, rad, mPaintBG);

        //计算最大进度
        maxProgressW = measuredWidth - gap;
        maxProgressH = measuredHeight - gap;

        if (currentProgressW >= maxProgressW) currentProgressW = maxProgressW;
        if (RECEIVE.equals(text))currentProgressW = maxProgressW;

        //绘制进度
        progressRect.left = gap;
        progressRect.top = gap;
        if (currentProgressW < getMeasuredHeight()) progressRect.right = maxProgressH;
        else progressRect.right = currentProgressW;
        progressRect.bottom = maxProgressH;
        canvas.drawRoundRect(progressRect, progressrad, progressrad, mProgressPaint);

        //绘制文字
        srcBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float mTextHeight = fontMetrics.bottom - fontMetrics.descent - fontMetrics.ascent;
        float mTextWidth = mTextPaint.measureText(text);
        Canvas srcCanvas = new Canvas(srcBitmap);
        srcCanvas.drawText(text, 0, text.length(),
                measuredWidth / 2 - mTextWidth / 2, measuredHeight / 2 + mTextHeight / 2, mTextPaint);
        mTextPaint.setXfermode(xformode);
        mTextPaint.setColor(Color.WHITE);
        srcCanvas.drawRoundRect(progressRect, progressrad, progressrad, mTextPaint);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        mTextPaint.setColor(grayColor);
        mTextPaint.setXfermode(null);
        updateProgress();
    }

    public void setSecond(long second, long maxProgress) {
        //LogUtil.zhLogW(maxProgress+"==maxProgress========second=="+second);
        mSeconds = second;
        mMaxProgress = maxProgress;

        //mSeconds = 10;
        //mMaxProgress = 10;
        if (mMaxProgress == 0)mMaxProgress = 1;

        mProgresRatio = (mMaxProgress - mSeconds) *1.0f / mMaxProgress;
        currentProgressW = (maxProgressW * mProgresRatio);
    }

    /**
     * 创建倒计时
     */
    private void createCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mSeconds * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                updateSecond(millisUntilFinished);// 设置秒
            }

            @Override
            public void onFinish() {
                //LogUtil.zhLogW("=onFinish====text===="+text);
                text = RECEIVE;
                invalidate();
                if (mListener != null){
                    mListener.onComplete();
                }
            }
        };
    }

    /**
     * 开始倒计时
     */
    public void startDownTimer() {
        if (mSeconds <= 1) {
            text = RECEIVE;
            invalidate();
        } else {
            createCountDownTimer();// 创建倒计时
            mCountDownTimer.start();
        }
    }

    private void  updateProgress() {
        currentProgressW += (maxProgressW * 1.0f / mMaxProgress);
        /*LogUtil.zhLogW(String.format("最大进度：%d    ; 当前进度：%f   ; 当前时间：%s",
                maxProgressW,currentProgressW,text));*/
    }

    public void cancelDownTimer() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
    }

    private void updateSecond(long millis) {
        long totalSeconds = millis / 1000;
        String second = (int) (totalSeconds % 60) + "";// 秒
        long totalMinutes = totalSeconds / 60;
        String minute = (int) (totalMinutes % 60) + "";// 分
        long totalHours = totalMinutes / 60;
        String hour = totalHours + "";

        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }

        text = String.format(format, hour, minute, second);
        //LogUtil.zhLogW("======updateSecond======="+text);
        invalidate();
    }


    public void setDownTimeComplete(DownTimeCompleteListener listener){

        mListener = listener;
    }

    /**
     * 是否可以点击
     * @return
     */
    public boolean isClickable(){
        if (RECEIVE.equals(text)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (!MyOnClickListener.isFastClick() && mLi != null){
                    mLi.click();
                }
                break;
        }
        return true;
    }

    public interface DownTimeCompleteListener{
       void onComplete();
    }

    public void setOnClickListener(OnClickListener listener){

        mLi = listener;
    }
    public interface OnClickListener{
        void click();
    }

    public void detachView() {
        cancelDownTimer();
        mCountDownTimer = null;
        bgRect = null;
        progressRect = null;
        mPaintBG = null;
        mProgressPaint = null;
        mTextPaint = null;
    }
}
