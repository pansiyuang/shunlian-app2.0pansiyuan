package com.shunlian.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by Administrator on 2017/10/30.
 */

public class MyTextView extends TextView {

    private boolean aBoolean;
    private int control_width;
    private int control_height;
    private int margin;
    private int margin_left;
    private int margin_top;
    private int margin_bottom;
    private int margin_right;

    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
//        Typeface face = Typeface.createFromAsset (context.getAssets(),"hkbd.ttf");
//        setTypeface (face);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyTextView, defStyleAttr, 0);
        aBoolean = a.getBoolean(R.styleable.MyTextView_tv_control_adapter, false);
        control_width = a.getInteger(R.styleable.MyTextView_tv_control_width, 0);
        control_height = a.getInteger(R.styleable.MyTextView_tv_control_height, 0);
        margin = a.getInteger(R.styleable.MyTextView_tv_margin, 0);
        margin_left = a.getInteger(R.styleable.MyTextView_tv_margin_left, 0);
        margin_top = a.getInteger(R.styleable.MyTextView_tv_margin_top, 0);
        margin_right = a.getInteger(R.styleable.MyTextView_tv_margin_right, 0);
        margin_bottom = a.getInteger(R.styleable.MyTextView_tv_margin_bottom, 0);
        a.recycle();
//        init();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (aBoolean){
            if (margin != 0){
                int marginLR = TransformUtil.countRealWidth(getContext(), margin);
                int marginTB = TransformUtil.countRealHeight(getContext(), margin);
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams){
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMargins(marginLR,marginTB,marginLR,marginTB);
                }
            }else {
                int real_left = TransformUtil.countRealWidth(getContext(), margin_left);
                int real_right = TransformUtil.countRealWidth(getContext(), margin_right);
                int real_top = TransformUtil.countRealHeight(getContext(), margin_top);
                int real_bottom = TransformUtil.countRealHeight(getContext(), margin_bottom);
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams){
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMargins(real_left,real_top,real_right,real_bottom);
                }
            }
            if (control_width == 0 || control_height == 0) {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (control_width == 0 && control_height != 0) {
                    int realHeight = TransformUtil.countRealHeight(getContext(), control_height);
                    layoutParams.height = realHeight;
                } else if (control_width != 0 && control_height == 0) {
                    int realWidth = TransformUtil.countRealWidth(getContext(), control_width);
                    layoutParams.width = realWidth;
                }
                setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (aBoolean) {
            if (control_width == 0 && control_height == 0){
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }else if (control_width != 0 && control_height != 0){
                int[] realWH = TransformUtil.countRealWH(getContext(), control_width, control_height);
                setMeasuredDimension(realWH[0],realWH[1]);
            }
        }
    }
    /**
     * 设置赚多少
     */
    public MyTextView setEarnMoney(String text,int bigSize){
        if (TextUtils.isEmpty(text)){
            this.setVisibility(View.GONE);
            return this;
        }else {
            this.setVisibility(VISIBLE);
        }
        int start=0;
        int end=text.length();
        if (text.contains("赚"))
            start=text.indexOf("赚")+1;
        if (text.contains("起"))
            end=text.indexOf("起");
        if (text.contains("元"))
            end=text.indexOf("元");
        if (text.contains("￥"))
            start=text.indexOf("￥")+1;
        String value=text.substring(start,end);
        this.setText(Common.changeTextSize(text,value,bigSize));
        return this;
    }
    /**
     * 设置中划线
     */
    public MyTextView setStrikethrough(){
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        return this;
    }

    /**
     * 设置控件大小，宽高都为像素
     *
     * @param width
     * @param height
     */
    public void setWHProportion(int width, int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int[] realWH = TransformUtil.countRealWH(getContext(), width, height);
        int real_w = realWH[0];
        int real_h = realWH[1];
        if (layoutParams == null){
            layoutParams = new ViewGroup.LayoutParams(real_w,real_h);
        }else {
            layoutParams.width = real_w;
            layoutParams.height = real_h;
        }
        setLayoutParams(layoutParams);
    }

}
