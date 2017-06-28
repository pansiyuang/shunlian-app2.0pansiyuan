package com.shunlian.app.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.shunlian.app.utils.TransformUtil;


//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

/**
 * Created by zhang on 2017/6/24 14 : 02.
 */

public class DoubleCircleView extends CustomAnimation {

    private Paint bluePaint;
    private Paint redPaint;
    private float radius = 8;
    private float spacing = 0;
    private float MAX_SPACING = 20;
    private float animatedValue;
    private ValueAnimator valueAnimator;
    private boolean isStartAnimation = false;

    public DoubleCircleView(Context context) {
        this(context,null);
    }


    public DoubleCircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public DoubleCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        redPaint = new Paint();
        redPaint.setAntiAlias(true);
        redPaint.setColor(Color.parseColor("#ff0000"));
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setStrokeWidth(4);

        bluePaint = new Paint();
        bluePaint.setAntiAlias(true);
        bluePaint.setColor(Color.parseColor("#0000ff"));
        bluePaint.setStyle(Paint.Style.FILL);
        bluePaint.setStrokeWidth(4);

        radius = TransformUtil.dip2px(getContext(),4);
        MAX_SPACING = TransformUtil.dip2px(getContext(),10);
    }


    @Override
    public void startAnimation(){
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1500);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setInterpolator(new LinearInterpolator());

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animatedValue = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
        }
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
            isStartAnimation = true;
            spacing = MAX_SPACING;
        }

    }

    @Override
    public void stopAnimation(){
        if (valueAnimator != null){
//            valueAnimator.removeAllUpdateListeners();
//            valueAnimator = null;
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
                isStartAnimation = false;
                spacing = 0;
                invalidate();
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rotateAnimation(canvas);
    }

    /*
    旋转动画
     */
    private void rotateAnimation(Canvas canvas){

        if (isStartAnimation) {
            canvas.rotate(360 * animatedValue, getWidth() / 2, getHeight() / 2);
        }

        canvas.drawCircle(getWidth() / 2 - spacing,getHeight() / 2,radius,redPaint);
        canvas.drawCircle(getWidth() / 2 + spacing,getHeight() / 2,radius,bluePaint);
    }

    @Override
    public void pullDownHeight(int touchPosiotin) {
        super.pullDownHeight(touchPosiotin);
        if (touchPosiotin < 50)
            return;
        spacing = touchPosiotin / 5.0f;
        if (spacing > 20){
            spacing = 20;
        }else if (spacing <= 0){
            spacing = 0;
        }
        invalidate();
    }
}
