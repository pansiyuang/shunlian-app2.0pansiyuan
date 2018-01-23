package com.shunlian.app.utils.timer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 标签制作
 *
 * @author Linhai Gu
 */
public class GradientTextView {

    private GradientDrawable mGradientDrawable;
    private TextView mLabelTextView;
    private Context mContext;

    public GradientTextView(Context context) {
        this.mContext = context;
        mGradientDrawable = new GradientDrawable();
        mLabelTextView = new TextView(mContext);
    }

    /**
     * dp-->px
     * @param dipValue
     * @return
     */
    private int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 是否为空
     * @param str
     * @return
     */
    private boolean empty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 转换成颜色值
     * @param color
     * @return
     */
    private int parseColor(String color) {
        return Color.parseColor("#".concat(color));
    }

    /**
     * 标签字体颜色
     * @param color
     * @return
     */
    public GradientTextView setTextColor(int color) {
        if (color == 0){
            mLabelTextView.setTextColor(parseColor("ffffff"));//默认白色字体
        }else {
            mLabelTextView.setTextColor(color);
        }
        return this;
    }

    /**
     * 标签背景颜色
     * @param color
     * @return
     */
    public GradientTextView setBackgroundColor(int color) {
        if (color == 0){
            mGradientDrawable.setColor(parseColor("ffffff"));//默认白色背景
        }else {
            mGradientDrawable.setColor(color);
        }
        return this;
    }

    /**
     * 设置外边框颜色和宽度
     * @param strokeWidth 边框宽
     * @param color 边框颜色
     * @return
     */
    public GradientTextView setStrokeColor(int strokeWidth,int color) {
        if (color == 0){
            mGradientDrawable.setStroke(dip2px(strokeWidth),parseColor("ffffff"));//默认边框线白色
        }else {
            mGradientDrawable.setStroke(dip2px(strokeWidth),color);
        }
        return this;
    }

    /**
     * 设置圆角
     * @param radius
     * @return
     */
    public GradientTextView setStrokeRadius(int radius) {
        mGradientDrawable.setCornerRadius(dip2px(radius));
        return this;
    }

    /**
     * 设置标签内容
     * @param info
     * @return
     */
    public GradientTextView setLabelText(String info) {
        if (!empty(info)) {
            mLabelTextView.setText(info);
        }
        return this;
    }

    /**
     * 设置标签大小
     * @param size
     * @return
     */
    public GradientTextView setTextSize(int size) {
        mLabelTextView.setTextSize(size);
        return this;
    }

    /**
     * 构造TextView
     * @return
     */
    public TextView build() {
        mLabelTextView.setBackgroundDrawable(mGradientDrawable);
        mLabelTextView.setGravity(Gravity.CENTER);
        return mLabelTextView;
    }

    /**
     * 设置宽高
     * @param labelWidthHeight
     * @return
     */
    public GradientTextView setTextWH(int[] labelWidthHeight) {
        if (labelWidthHeight != null && labelWidthHeight.length == 2){
            int width = dip2px(labelWidthHeight[0]);
            int height = dip2px(labelWidthHeight[1]);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width,height);
            mLabelTextView.setLayoutParams(layoutParams);
        }
        return this;
    }
}
