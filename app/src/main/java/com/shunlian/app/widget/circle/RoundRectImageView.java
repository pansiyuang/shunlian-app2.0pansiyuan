package com.shunlian.app.widget.circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by Administrator on 2018/5/7.
 */

public class RoundRectImageView extends AppCompatImageView {
    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private Paint paint;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;
    private final int radius;

    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        radius = TransformUtil.dip2px(getContext(), 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null||mBitmapShader == null) {
            return;
        }
        if(mBitmap.getHeight() == 0 || mBitmap.getWidth() == 0)
            return;

        try {
            updateBitmapShader();
            paint.setShader(mBitmapShader);
            RectF rectF = new RectF(0,0,getWidth(),getHeight());
            canvas.drawRoundRect(rectF,radius,radius, paint);
            clipBottomLeft(canvas,paint,radius,getWidth(),getHeight());
            clipBottomRight(canvas, paint, radius,getWidth(),getHeight());
        }catch (RuntimeException e){
//            Common.staticToast(getContext(),"内存不足！");
        }catch (OutOfMemoryError error){
//            Common.staticToast(getContext(),"OOM！");
        }
    }

    private void init(){
        if (mBitmap == null) return;
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        init();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        init();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        init();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        init();
    }


    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateBitmapShader() {
        if (mBitmap == null)
            return;
        float scaleWidth =  getWidth() * 1.0f / mBitmap.getWidth();
        float scaleHeight = getHeight() * 1.0f / mBitmap.getHeight();
        LogUtil.zhLogW(scaleWidth+"======bitmap===缩放====="+scaleHeight);
        Matrix matrix = new Matrix();
//        matrix.setScale(scaleWidth,scaleHeight,mBitmap.getWidth()/2.0f,mBitmap.getHeight()/2.0f);
        matrix.postScale(scaleWidth,scaleHeight);
        mBitmapShader.setLocalMatrix(matrix);
    }

    private void clipBottomLeft(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, height - offset, offset, height);
        canvas.drawRect(block, paint);
    }

    private void clipBottomRight(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(width - offset, height - offset, width, height);
        canvas.drawRect(block, paint);
    }
}
