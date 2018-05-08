package com.shunlian.app.widget.refresh.turkey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.shunlian.app.R;


public class FirstSetpView extends View{

	private Bitmap right;
	private Bitmap left;
	private Bitmap peopleWithGoods;
	private int measuredWidth;
	private int measuredHeight;
	private float mCurrentProgress;
	private int mCurrentAlpha;
	private Paint mPaint;
	private Bitmap scaledTurkey;
	private Bitmap scaledLetter;
	public FirstSetpView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public FirstSetpView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FirstSetpView(Context context) {
		super(context);
		init();
	}
	private void init(){
		//文字bitmap
		right = BitmapFactory.decodeResource(getResources(), R.mipmap.turkey_pulls);
		//火鸡bitmap
		left = BitmapFactory.decodeResource(getResources(), R.mipmap.turkey_pull);
		//这是后面动画中的最后一张图片，拿这张图片的作用是用它的宽高来测量
		//我们这个自定义View的宽高
		peopleWithGoods = BitmapFactory.decodeResource(getResources(), R.mipmap.turkey_release);
		//来个画笔，我们注意到火鸡和文字都有一个渐变效果的，我们用
		//mPaint.setAlpha来实现这个渐变的效果
		mPaint = new Paint();
		//首先设置为完全透明
		mPaint.setAlpha(0);
	}

	/**
	 * 测量方法
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}
	//测量宽度
	private int measureWidth(int widthMeasureSpec){
		int result = 0;
		int size = MeasureSpec.getSize(widthMeasureSpec);
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		if (MeasureSpec.EXACTLY == mode) {
			result = size;
		}else {
			result = peopleWithGoods.getWidth();
			if (MeasureSpec.AT_MOST == mode) {
				result = Math.min(result, size);
			}
		}
		return result;
	}
	//测量高度
	private int measureHeight(int heightMeasureSpec){
		int result = 0;
		int size = MeasureSpec.getSize(heightMeasureSpec);
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		if (MeasureSpec.EXACTLY == mode) {
			result = size;
		}else {
			result = peopleWithGoods.getHeight();
			if (MeasureSpec.AT_MOST == mode) {
				result = Math.min(result, size);
			}
		}
		return result;
	}
	//在这里面拿到测量后的宽和高，w就是测量后的宽，h是测量后的高
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		measuredWidth = w;
		measuredHeight = h;
		//根据测量后的宽高来对火鸡做一个缩放
		try {
			scaledTurkey = Bitmap.createScaledBitmap(left, measuredWidth * 98 / 219, measuredHeight, true);
			//根据测量后的宽高来对文字做一个缩放
			scaledLetter = Bitmap.createScaledBitmap(right, measuredWidth * 123 / 219, measuredHeight * 51 / 93, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 绘制方法
	 * @param canvas
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//由于文字和火鸡要分别来画，所以使用save和restore方法
		//save
		//画文字
		//restore
		//save
		//画火鸡
		//restore
		canvas.save();
//		canvas.scale(mCurrentProgress, mCurrentProgress ,  measuredWidth-scaledLetter.getWidth()/2 , measuredHeight/2);
		canvas.scale(mCurrentProgress, mCurrentProgress ,  measuredWidth/2+scaledLetter.getWidth()/2, measuredHeight/2);
		mPaint.setAlpha(mCurrentAlpha);
//		canvas.drawBitmap(scaledLetter, measuredWidth-scaledLetter.getWidth(), measuredHeight/2-scaledLetter.getHeight()/2, mPaint);
		canvas.drawBitmap(scaledLetter, measuredWidth-scaledLetter.getWidth(), measuredHeight/2-scaledLetter.getHeight()/2, mPaint);
		canvas.restore();
		canvas.save();
		canvas.scale(mCurrentProgress, mCurrentProgress , scaledTurkey.getWidth()/2, measuredHeight/2);
		mPaint.setAlpha(mCurrentAlpha);
		canvas.drawBitmap(scaledTurkey, 0,0,mPaint);
		canvas.restore();
	}

	/**
	 * 根据进度来对火鸡和文字进行缩放
	 * @param currentProgress
	 */
	public void setCurrentProgress(float currentProgress){
		this.mCurrentProgress = currentProgress;
		this.mCurrentAlpha = (int) (currentProgress*255);
	}
}
