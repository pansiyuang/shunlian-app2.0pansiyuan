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

/**
 * Created by Administrator on 2017/12/1.
 */

public class ProgressView extends View {
    private final int bottomColor = Color.parseColor("#CCCCCC");
    private final int pinkColor = Color.parseColor("#FB0030");
    private final int anmiDuation = 1500; //动画时间

    private final int ringWidth = 4; //环的宽度;

    private int angle = 360;
    private Paint bottomPaint; //底色画笔
    private Paint animPaint; //动画画笔
    private boolean isGay = true;
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
        canvas.drawArc(rectF, 0, angle, false, animPaint);
    }

    public void startAnimation() {
        valueAnimator = ValueAnimator.ofInt(0, angle);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                if (isGay) {
                    bottomPaint.setColor(pinkColor);
                    animPaint.setColor(bottomColor);
                    isGay = false;
                } else {
                    bottomPaint.setColor(bottomColor);
                    animPaint.setColor(pinkColor);
                    isGay = true;
                }
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

    public void releaseAnimation() {
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
        }
    }
}
