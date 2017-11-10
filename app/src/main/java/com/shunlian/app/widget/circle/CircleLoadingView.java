package com.shunlian.app.widget.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2017/10/27.
 */

public class CircleLoadingView extends View {

    /**
     * View的高度
     */
    private int mHeight;

    /**
     * View的宽度
     */
    private int mWidth;


    /**
     * 屏幕的宽度
     */
    private int mScreenWidth;

    /**
     * 屏幕的高度
     */
    private int mScreenHeight;

    /**
     * 画笔的宽度
     */
    private final int circleWidth = 4;


    private Paint mCirclePaint;

    private Paint mPaint;


    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getSceenSize();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = mScreenWidth / 5;
        mHeight = mWidth;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centre = getWidth() / 2; //获取圆心的x坐标
        int radius = (centre - circleWidth / 2); //圆环的半径
        canvas.drawCircle(centre, centre, radius, mPaint);

        super.onDraw(canvas);
    }

    public void getSceenSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(circleWidth);
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.new_gray));
    }
}
