package com.shunlian.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/10/24.
 */

public class MyEditText extends EditText {
    private String hintContent;
    private Context mContext;

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setCursorDrawableColor(R.drawable.edit_cursor);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyEditText);
        hintContent = a.getString(R.styleable.MyEditText_hintText);
        setHintSize(hintContent);
        a.recycle();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        setHint(new SpannableString(ss));
    }
}
