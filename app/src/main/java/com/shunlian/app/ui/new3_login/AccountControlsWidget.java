package com.shunlian.app.ui.new3_login;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.InputFilter;
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
public class AccountControlsWidget extends RelativeLayout {

    private EditText mAccountText;
    private MyImageView mTipIV;
    private OnTextChangeListener mListener;
    private String mHintText;
    private int mHintTextColor;
    private int mTextColor;
    private float mTextSize;
    private int mLineColor;
    private boolean mIsMobile;

    public AccountControlsWidget(Context context) {
        this(context, null);
    }

    public AccountControlsWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccountControlsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AccountControlsWidget, defStyleAttr, 0);
        mHintText = a.getString(R.styleable.AccountControlsWidget_hint_text);
        mHintTextColor = a.getColor(R.styleable.AccountControlsWidget_hint_text_color,
                Color.parseColor("#484848"));
        mTextColor = a.getColor(R.styleable.AccountControlsWidget_text_color,
                Color.parseColor("#484848"));
        mTextSize = a.getDimension(R.styleable.AccountControlsWidget_text_size, 16f);
        mLineColor = a.getColor(R.styleable.AccountControlsWidget_line_color, Color.parseColor("#CCCCCC"));
        mIsMobile = a.getBoolean(R.styleable.AccountControlsWidget_is_mobile, false);
        a.recycle();

        init(context);
        initListener();
    }

    private void init(Context context) {
        //账号
        mAccountText = new EditText(context);
        addView(mAccountText);
        mAccountText.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
        mAccountText.setTextColor(mTextColor);
        mAccountText.setMinHeight(TransformUtil.dip2px(context, 15f));
        RelativeLayout.LayoutParams mAccountParams = (LayoutParams) mAccountText.getLayoutParams();
        mAccountParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mAccountParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mAccountText.setLayoutParams(mAccountParams);
        mAccountText.setBackgroundDrawable(null);
        mAccountText.setHintTextColor(mHintTextColor);
        mAccountText.setHint(mHintText);
        if (mIsMobile){
            mAccountText.setInputType(InputType.TYPE_CLASS_PHONE);
            mAccountText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        }


        //下划线
        int lineHeight = TransformUtil.dip2px(context, 0.5f);
        View mLineView = new View(context);
        mLineView.setBackgroundColor(mLineColor);
        addView(mLineView);

        RelativeLayout.LayoutParams mLineParams = (LayoutParams) mLineView.getLayoutParams();
        mLineParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLineParams.height = lineHeight;
        mLineParams.topMargin = TransformUtil.dip2px(context, 16f);
        mLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLineView.setLayoutParams(mLineParams);

        //删除提示
        mTipIV = new MyImageView(context);
        mTipIV.setImageResource(R.mipmap.icon_search_del);
        addView(mTipIV);
        RelativeLayout.LayoutParams mTipIVParams = (LayoutParams) mTipIV.getLayoutParams();
        mTipIVParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mTipIVParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mTipIV.setLayoutParams(mTipIVParams);
        int i = TransformUtil.dip2px(context, 10);
        mTipIV.setPadding(i, i, i, i);
        mTipIV.setVisibility(GONE);
    }


    private void initListener() {
        mAccountText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
//                System.out.println(String.format("onTextChanged : s=%s  start=%d  before=%d  count=%d",
//                 s,start,before,count));
                if (mListener != null) mListener.onTextChange(s);
                if (s.length() > 0) {
                    mTipIV.setVisibility(VISIBLE);
                } else {
                    mTipIV.setVisibility(GONE);
                }
                if (mIsMobile){//处理手机号
                    if (count == 1) {
                        int length = s.toString().length();
                        if (length == 3 || length == 8) {
                            mAccountText.setText(s + " ");
                            mAccountText.setSelection(mAccountText.getText().length());
                        }
                    }else if (count == 0){
                        int length = s.toString().length();
                        if (length == 4 || length == 9) {
                            mAccountText.setText(mAccountText.getText().toString().trim());
                            mAccountText.setSelection(mAccountText.getText().length());
                        }
                    }
                }
            }
        });

        mTipIV.setOnClickListener(v -> {
            if (mAccountText != null)
                mAccountText.setText("");
            mTipIV.setVisibility(GONE);
        });
    }

    /**
     * 获取内容
     *
     * @return
     */
    public CharSequence getText() {
        if (mAccountText != null) {
            String text = mAccountText.getText().toString().trim();
            text = text.replaceAll(" ", "");
            return text;
        }
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
