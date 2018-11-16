package com.shunlian.app.widget.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DebugUtils;
import android.util.Log;
import android.view.View;

import com.shunlian.app.R;
import com.zh.chartlibrary.common.DensityUtil;

/**
 * DrawHook
 * Created by Zane on 2015/3/4.
 */
public class DrawHookView extends View {
    //绘制圆弧的进度值
    private int progress = 0;
    //线1的x轴
    private int line1_x = 0;
    //线1的y轴
    private int line1_y = 0;
    //线2的x轴
    private int line2_x = 0;
    //线2的y轴
    private int line2_y = 0;
    private boolean isUpdateFinfish = false;

    public DrawHookView(Context context) {
        super(context);
    }

    public DrawHookView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawHookView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //绘制

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        progress=progress+5;

        /**
         * 绘制圆弧
         */
        Paint paint = new Paint();
        //设置画笔颜色
        paint.setColor(getResources().getColor(R.color.pink_color));
        //设置圆弧的宽度
        paint.setStrokeWidth(DensityUtil.dip2px(getContext(),3));
        //设置圆弧为空心
        paint.setStyle(Paint.Style.STROKE);
        //消除锯齿
        paint.setAntiAlias(true);
        //获取圆心的x坐标
        int center = getWidth() / 2;
        int center1 = center - getWidth() / 5;
        //圆弧半径
        int radius = getWidth() / 2 - 5;

//        //定义的圆弧的形状和大小的界限
        RectF rectF = new RectF(center - radius -1, center - radius -1 ,center + radius + 1, center + radius + 1);

        //根据进度画圆弧
        canvas.drawArc(rectF, 0, 360 * progress / 100, false, paint);

        /**
         * 绘制对勾
         */
        //先等圆弧画完，才话对勾
        if(progress >= 100) {
            if(line1_x < radius / 3) {
                if(line1_x < (radius / 3)-5){
                    line1_x=line1_x+3;
                    line1_y=line1_y+3;
                }else{
                    line1_x++;
                    line1_y++;
                }
            }
            //画第一根线
            canvas.drawLine(center1, center, center1 + line1_x, center + line1_y, paint);

            if (line1_x == radius / 3) {
                line2_x = line1_x;
                line2_y = line1_y;
                line1_x++;
                line1_y++;
            }
            if (line1_x >= radius / 3 && line2_x <= radius) {
                if(line2_x <= radius-5){
                    line2_x=line2_x+3;
                    line2_y=line2_y-3;
                }else {
                    line2_x++;
                    line2_y--;
                    if(line2_x>=radius){
                        isUpdateFinfish  = true;
                    }
                }
            }
            Log.d("test","绘制："+"line1_x:"+line1_x+" line1_y:"+line1_y+" line2_x:"+line2_x+" line2_y:"+line2_y);
            //画第二根线
            canvas.drawLine(center1 + line1_x - 1, center + line1_y, center1 + line2_x, center + line2_y, paint);
            if(!isUpdateFinfish){
                invalidate();
            }
        }

        //每隔10毫秒界面刷新
        if(progress<100) {
            invalidate();
        }
    }

    public void setInitData() {
        //绘制圆弧的进度值
       progress = 0;
       line1_x = 0;
       line1_y = 0;
       line2_x = 0;
       line2_y = 0;
       isUpdateFinfish = false;

    }
}
