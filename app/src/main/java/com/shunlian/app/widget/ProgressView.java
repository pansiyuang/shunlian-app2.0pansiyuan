package com.shunlian.app.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by Administrator on 2017/12/1.
 */

public class ProgressView extends View {
    private final int bottomColor = Color.parseColor("#CCCCCC");
    private final int pinkColor = Color.parseColor("#FB0030");

    private final int anmiDuation = 1500; //动画时间

    private  int ringWidth = 3; //环的宽度;

    private int angle = 360;
    private Paint bottomPaint; //底色画笔
    private Paint animPaint; //动画画笔
    private boolean isGay = false;
    private ValueAnimator valueAnimator;

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        ringWidth = TransformUtil.dip2px(getContext(),1.5f);
        bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomPaint.setStyle(Paint.Style.STROKE);
        bottomPaint.setStrokeWidth(ringWidth);
        bottomPaint.setColor(bottomColor);

        animPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animPaint.setStyle(Paint.Style.STROKE);
        animPaint.setStrokeWidth(ringWidth);
        animPaint.setColor(pinkColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2f, getHeight() / 2f);
        canvas.rotate(-90);
        RectF rectF = new RectF(0 - (getWidth() / 2f) + (ringWidth / 2f), 0 - (getWidth() / 2f) + (ringWidth / 2f), (getWidth() / 2f) - (ringWidth / 2f), (getHeight() / 2f) - (ringWidth / 2f));
        canvas.drawArc(rectF, 0, 360, false, bottomPaint);

        if (isGay) {
            canvas.drawArc(rectF, 360, angle - 360, false, animPaint);
        } else {
            canvas.drawArc(rectF, 0, angle, false, animPaint);
        }


    }

    public boolean isRunning() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            return true;
        }
        return false;
    }

    public void startAnimation() {
        if (isRunning()) {
            return;
        }
        valueAnimator = ValueAnimator.ofInt(0, angle);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                isGay = !isGay;
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                angle = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.setDuration(anmiDuation);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    public void stopAnimation() {
        if (isRunning()) {
            valueAnimator.end();
            angle = 360;
        }
    }

    public void releaseAnimation() {
        if (isRunning()) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
            angle = 360;
        }
    }
}
