package com.shunlian.app.ui.new3_login;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.shunlian.app.utils.TransformUtil;

/**
 * Created by zhanghe on 2018/11/16.
 * 账号控件
 */
public class PwdControlsWidget extends RelativeLayout {

    public PwdControlsWidget(Context context) {
        this(context,null);
    }

    public PwdControlsWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PwdControlsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        //密码
        EditText mPwdText = new EditText(context);
        addView(mPwdText);
        mPwdText.setTextSize(16);
        mPwdText.setTextColor(Color.parseColor("#484848"));
        mPwdText.setMinHeight(TransformUtil.dip2px(context, 16f));
        LayoutParams mAccountParams = (LayoutParams) mPwdText.getLayoutParams();
        mAccountParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mAccountParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mPwdText.setLayoutParams(mAccountParams);
        mPwdText.setBackgroundDrawable(null);
        mPwdText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPwdText.setHintTextColor(Color.parseColor("#CCCCCC"));
        mPwdText.setHint("请输入您的密码");

        //下划线
        int lineHeight = TransformUtil.dip2px(context, 0.5f);
        View mLineView = new View(context);
        mLineView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        addView(mLineView);

        LayoutParams mLineParams = (LayoutParams) mLineView.getLayoutParams();
        mLineParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLineParams.height = lineHeight;
        mLineParams.topMargin = TransformUtil.dip2px(context, 16f);
        mLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLineView.setLayoutParams(mLineParams);


    }
}
