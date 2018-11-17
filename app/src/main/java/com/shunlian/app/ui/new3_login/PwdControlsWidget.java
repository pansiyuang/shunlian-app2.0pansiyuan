package com.shunlian.app.ui.new3_login;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

/**
 * Created by zhanghe on 2018/11/16.
 * 账号控件
 */
public class PwdControlsWidget extends RelativeLayout {

    private EditText mPwdText;
    private MyImageView mIVShowPwd;
    private MyImageView mTipIV;

    private boolean isShowPwd;//是否显示密码，默认隐藏
    private OnTextChangeListener mListener;
    private String mHintText;
    private int mHintTextColor;
    private int mTextColor;
    private float mTextSize;
    private int mLineColor;

    public PwdControlsWidget(Context context) {
        this(context, null);
    }

    public PwdControlsWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdControlsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AccountControlsWidget, defStyleAttr, 0);
        mHintText = a.getString(R.styleable.AccountControlsWidget_hint_text);
        mHintTextColor = a.getColor(R.styleable.AccountControlsWidget_hint_text_color,
                Color.parseColor("#484848"));
        mTextColor = a.getColor(R.styleable.AccountControlsWidget_text_color,
                Color.parseColor("#484848"));
        mTextSize = a.getDimension(R.styleable.AccountControlsWidget_text_size, 16f);
        mLineColor = a.getColor(R.styleable.AccountControlsWidget_line_color, Color.parseColor("#CCCCCC"));
        a.recycle();
        init(context);
        initListener();
    }

    private void init(Context context) {
        //密码
        mPwdText = new EditText(context);
        addView(mPwdText);
        mPwdText.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
        mPwdText.setTextColor(mTextColor);
        mPwdText.setMinHeight(TransformUtil.dip2px(context, 16f));
        LayoutParams mAccountParams = (LayoutParams) mPwdText.getLayoutParams();
        mAccountParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mAccountParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mPwdText.setLayoutParams(mAccountParams);
        mPwdText.setBackgroundDrawable(null);
        mPwdText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPwdText.setHintTextColor(mHintTextColor);
        mPwdText.setHint(mHintText);

        //下划线
        int lineHeight = TransformUtil.dip2px(context, 0.5f);
        View mLineView = new View(context);
        mLineView.setBackgroundColor(mLineColor);
        addView(mLineView);
        LayoutParams mLineParams = (LayoutParams) mLineView.getLayoutParams();
        mLineParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLineParams.height = lineHeight;
        mLineParams.topMargin = TransformUtil.dip2px(context, 16f);
        mLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLineView.setLayoutParams(mLineParams);


        int i = TransformUtil.dip2px(context, 10);
        //显示隐藏密码
        mIVShowPwd = new MyImageView(context);
        mIVShowPwd.setImageResource(R.mipmap.icon_login_eyes_h);
        mIVShowPwd.setId(R.id.new3_pwd);
        addView(mIVShowPwd);
        RelativeLayout.LayoutParams mIVShowPwdParams = (LayoutParams) mIVShowPwd.getLayoutParams();
        mIVShowPwdParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mIVShowPwdParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mIVShowPwd.setLayoutParams(mIVShowPwdParams);
        mIVShowPwd.setPadding(i, i, i, i);


        //删除提示
        mTipIV = new MyImageView(context);
        mTipIV.setImageResource(R.mipmap.icon_search_del);
        addView(mTipIV);
        RelativeLayout.LayoutParams mTipIVParams = (LayoutParams) mTipIV.getLayoutParams();
        mTipIVParams.addRule(RelativeLayout.LEFT_OF, mIVShowPwd.getId());
        mTipIVParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mTipIV.setLayoutParams(mTipIVParams);
        mTipIV.setPadding(i, i, 0, i);
        mTipIV.setVisibility(GONE);
    }

    private void initListener() {
        mPwdText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (mListener != null) mListener.onTextChange(s);
                if (s.length() > 0) {
                    mTipIV.setVisibility(VISIBLE);
                } else {
                    mTipIV.setVisibility(GONE);
                }
            }
        });

        mTipIV.setOnClickListener(v -> {
            if (mPwdText != null) {
                mPwdText.setText("");
                mTipIV.setVisibility(GONE);
            }
        });

        mIVShowPwd.setOnClickListener(v -> {
            isShowPwd = !isShowPwd;
            if (isShowPwd) {
                mPwdText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                mIVShowPwd.setImageResource(R.mipmap.icon_login_eyes_n);
            } else {
                mIVShowPwd.setImageResource(R.mipmap.icon_login_eyes_h);
                mPwdText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            mPwdText.setSelection(mPwdText.getText().length());
        });
    }

    /**
     * 获取内容
     *
     * @return
     */
    public CharSequence getText() {
        if (mPwdText != null) return mPwdText.getText();
        return "";
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnTextChangeListener(OnTextChangeListener listener) {
        mListener = listener;
    }

    /**
     * 文字改变监听
     */
    public interface OnTextChangeListener {
        void onTextChange(CharSequence sequence);
    }
}
