package com.shunlian.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/10/24.
 */

public class MyEditText extends AppCompatEditText {
    private String hintContent;
    private Context mContext;
    private boolean aBoolean;
    private int control_width;
    private int control_height;
    private int margin;
    private int margin_left;
    private int margin_top;
    private int margin_bottom;
    private int margin_right;

    public MyEditText(Context context) {
        this(context,null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
       
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setCursorDrawableColor(R.drawable.edit_cursor);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyEditText,defStyleAttr,0);
        hintContent = a.getString(R.styleable.MyEditText_hintText);
        aBoolean = a.getBoolean(R.styleable.MyEditText_et_control_adapter, false);
        control_width = a.getInteger(R.styleable.MyEditText_et_control_width, 0);
        control_height = a.getInteger(R.styleable.MyEditText_et_control_height, 0);
        margin = a.getInteger(R.styleable.MyEditText_et_margin, 0);
        margin_left = a.getInteger(R.styleable.MyEditText_et_margin_left, 0);
        margin_top = a.getInteger(R.styleable.MyEditText_et_margin_top, 0);
        margin_right = a.getInteger(R.styleable.MyEditText_et_margin_right, 0);
        margin_bottom = a.getInteger(R.styleable.MyEditText_et_margin_bottom, 0);
        a.recycle();
        setHintSize(hintContent);
    }

    /**
     * 代码设置光标颜色
     */
    public void setCursorDrawableColor(int drawable) {
        try {//修改光标的颜色（反射）
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(this, drawable);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void setHintSize(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        SpannableString ss = new SpannableString(content);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(TransformUtil.sp2px(mContext, 12));
        ss.setSpan(ass, 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setHint(ss);
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

        layoutParams.width = real_w;
        layoutParams.height = real_h;
        setLayoutParams(layoutParams);
    }
}
