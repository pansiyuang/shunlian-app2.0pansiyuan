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
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by Administrator on 2018/11/5.
 */

public class DiscoveryGuideView extends RelativeLayout {
    private int[] locationFirst, locationSecond;

    public DiscoveryGuideView(Context context) {
        super(context);
    }

    public DiscoveryGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscoveryGuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageLocation(int[] location1, int[] location2) {
        locationFirst = location1;
        locationSecond = location2;
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
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        if (locationFirst != null) {
            canvas.drawCircle(locationFirst[0], locationFirst[1], TransformUtil.dip2px(getContext(), 19), paint);
            Bitmap bitmapTop = BitmapFactory.decodeResource(getResources(), R.mipmap.img_faxian_ying);
            canvas.drawBitmap(bitmapTop, locationFirst[0] + TransformUtil.dip2px(getContext(), 7), locationFirst[1] + TransformUtil.dip2px(getContext(), 25), bitMapPaint);
        }
        if (locationSecond != null) {
            canvas.drawCircle(locationSecond[0], locationSecond[1], TransformUtil.dip2px(getContext(), 22), paint);
            Bitmap bitmapBottom = BitmapFactory.decodeResource(getResources(), R.mipmap.img_faxian_dao);
            canvas.drawBitmap(bitmapBottom, locationSecond[0], locationSecond[1] - bitmapBottom.getHeight() - TransformUtil.dip2px(getContext(), 10), bitMapPaint);
        }
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
