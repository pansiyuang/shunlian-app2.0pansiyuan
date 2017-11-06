package com.shunlian.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/11/2.
 */

public class RippleTextView extends TextView {

    private float rippleY;
    private float rippleX;
    private float rippleR;//波纹半径
    private Paint mPaint;
    int tempR = 0;

    public RippleTextView(Context context) {
        this(context,null);
    }

    public RippleTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RippleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#66000000"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


//        System.out.println("rippleY=="+rippleY+"  ;;rippleX=="+rippleX+"  ;;rippleR=="+ rippleR+" ;;tempR=="+tempR);

        drawCircle(canvas);

    }

    private void drawCircle(Canvas canvas) {
        if (tempR <= rippleR){
            tempR +=10;
            canvas.drawCircle(rippleX,rippleY,tempR,mPaint);
            invalidate();
        }else {
            tempR = 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                rippleY = event.getY();
                rippleX = event.getX();
                int measuredWidth = getMeasuredWidth();
                int measuredHeight = getMeasuredHeight();

                if (measuredWidth > measuredHeight){
                    rippleR = rippleX > measuredWidth / 2 ? rippleX : (measuredWidth - rippleX);
                }else {
                    rippleR = rippleY > measuredHeight / 2 ? rippleY : (measuredHeight - rippleY);
                }
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }
}
