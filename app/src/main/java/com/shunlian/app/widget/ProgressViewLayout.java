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

public class ProgressViewLayout extends View {


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
    private String text = "00%";
    private PorterDuffXfermode xformode;
    private Bitmap srcBitmap;
    private CountDownTimer mCountDownTimer;
    private long mSeconds;
    private long mMaxProgress = 1;
    private float mProgresRatio;

    public ProgressViewLayout(Context context) {
        super(context);
        init(context);
    }


    public ProgressViewLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public ProgressViewLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        maxProgressW = TransformUtil.dip2px(context, 90f);
        maxProgressH = TransformUtil.dip2px(context, 12f);;

        rad = TransformUtil.dip2px(context, 10f);
        grayColor = getResources().getColor(R.color.pink_color);
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
        if (currentProgressW >= maxProgressW) currentProgressW = maxProgressW;
        //绘制进度
        progressRect.left = 0;
        progressRect.top = 0;
//        if (currentProgressW < getMeasuredHeight()) {
//            progressRect.right = maxProgressH;
//        }else{
            progressRect.right = currentProgressW;
//        }
        progressRect.bottom = maxProgressH;
        if(currentProgressW<maxProgressH){
            canvas.drawCircle(currentProgressW / 2, maxProgressH / 2, maxProgressH / 2, mProgressPaint);
        }else {
            canvas.drawRoundRect(progressRect, progressrad, progressrad, mProgressPaint);
        }
//        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
//        int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
//        mProgressPaint.setColor(Color.parseColor("#1B1E33"));
//        canvas.drawRoundRect(new RectF(0, 0, currentProgressW, maxProgressH), maxProgressH / 2, maxProgressH / 2, mProgressPaint);
//        mProgressPaint.setXfermode(xfermode);
//        mProgressPaint.setColor(Color.parseColor("#FFD600"));
//        canvas.drawRoundRect(new RectF(maxProgressW-currentProgressW, 0, maxProgressW, maxProgressH), maxProgressH / 2, maxProgressH / 2, mProgressPaint);
//        mProgressPaint.setXfermode(null);
//        canvas.restoreToCount(saved);
//
//        //绘制文字
        srcBitmap = Bitmap.createBitmap(maxProgressW, maxProgressH, Bitmap.Config.ARGB_8888);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float mTextHeight = fontMetrics.bottom - fontMetrics.descent - fontMetrics.ascent;
        float mTextWidth = mTextPaint.measureText(text);
        Canvas srcCanvas = new Canvas(srcBitmap);
        srcCanvas.drawText(text, 0, text.length(),
                maxProgressW  - mTextWidth -progressrad/2, maxProgressH / 2+mTextHeight/3, mTextPaint);
        mTextPaint.setXfermode(xformode);
        mTextPaint.setColor(Color.WHITE);
        srcCanvas.drawRoundRect(progressRect, progressrad, progressrad, mTextPaint);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        mTextPaint.setColor(grayColor);
        mTextPaint.setXfermode(null);
//        updateProgress();
    }

    public void setSecond(long second, long maxProgress) {
        //LogUtil.zhLogW(maxProgress+"==maxProgress========second=="+second);
        mSeconds = second;
        mMaxProgress = maxProgress;
        text = second+"%";
        //mSeconds = 10;
        //mMaxProgress = 10;
        if (mMaxProgress == 0)mMaxProgress = 1;

        mProgresRatio = (mSeconds) *1.0f / mMaxProgress;
        currentProgressW = (maxProgressW * mProgresRatio);
        invalidate();
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


    public void detachView() {
        cancelDownTimer();
        bgRect = null;
        progressRect = null;
        mPaintBG = null;
        mProgressPaint = null;
        mTextPaint = null;
    }
}
