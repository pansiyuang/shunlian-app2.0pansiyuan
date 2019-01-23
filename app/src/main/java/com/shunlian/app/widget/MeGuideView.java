package com.shunlian.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by Administrator on 2018/11/5.
 */

public class MeGuideView extends View {
    private int[] locationFirst, locationSecond;

    public MeGuideView(Context context) {
        super(context);
    }

    public MeGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeGuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageLocation(int[] location1) {
        locationFirst = location1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        Paint paint = new Paint();
        Paint bitMapPaint = new Paint();
        paint.setAntiAlias(true);
        bitMapPaint.setAntiAlias(true);

        paint.setColor(Color.parseColor("#CC000000"));

        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        if (locationFirst != null) {
            canvas.drawCircle(locationFirst[0]-TransformUtil.dip2px(getContext(), 8), locationFirst[1], TransformUtil.dip2px(getContext(), 21), paint);
            Bitmap bitmapTop = BitmapFactory.decodeResource(getResources(), R.mipmap.img_me_ying_guide);
            canvas.drawBitmap(bitmapTop, locationFirst[0]-bitmapTop.getWidth()+TransformUtil.dip2px(getContext(), 26) , locationFirst[1], bitMapPaint);
        }
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }
}
