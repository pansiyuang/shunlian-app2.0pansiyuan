package com.shunlian.app.ui.new3_login;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

/**
 * Created by zhanghe on 2018/11/16.
 * 账号控件
 */
public class AccountControlsWidget extends RelativeLayout {

    public AccountControlsWidget(Context context) {
        this(context,null);
    }

    public AccountControlsWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AccountControlsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        //账号
        EditText mAccountText = new EditText(context);
        addView(mAccountText);
        mAccountText.setTextSize(16);
        mAccountText.setTextColor(Color.parseColor("#484848"));
        mAccountText.setMinHeight(TransformUtil.dip2px(context, 15f));
        RelativeLayout.LayoutParams mAccountParams = (LayoutParams) mAccountText.getLayoutParams();
        mAccountParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mAccountParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mAccountText.setLayoutParams(mAccountParams);
        mAccountText.setBackgroundDrawable(null);
        mAccountText.setHintTextColor(Color.parseColor("#CCCCCC"));
        mAccountText.setHint("请输入您的用户账号");

        //下划线
        int lineHeight = TransformUtil.dip2px(context, 0.5f);
        View mLineView = new View(context);
        mLineView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        addView(mLineView);

        RelativeLayout.LayoutParams mLineParams = (LayoutParams) mLineView.getLayoutParams();
        mLineParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLineParams.height = lineHeight;
        mLineParams.topMargin = TransformUtil.dip2px(context, 16f);
        mLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLineView.setLayoutParams(mLineParams);

        //删除提示
        MyImageView mTipIV = new MyImageView(context);
        mTipIV.setImageResource(R.mipmap.icon_search_del);
        addView(mTipIV);
        RelativeLayout.LayoutParams mTipIVParams = (LayoutParams) mTipIV.getLayoutParams();
        mTipIVParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mTipIVParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mTipIV.setLayoutParams(mTipIVParams);
        int i = TransformUtil.dip2px(context, 10);
        mTipIV.setPadding(i,i,i,i);
    }
}
