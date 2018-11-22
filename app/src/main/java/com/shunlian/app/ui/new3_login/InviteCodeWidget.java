package com.shunlian.app.ui.new3_login;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

/**
 * Created by zhanghe on 2018/11/16.
 * 邀请码控件
 */
public class InviteCodeWidget extends RelativeLayout {

    private EditText mInviteCodeText;
    private MyImageView mTipIV;
    private OnTextChangeListener mListener;
    private String mHintText;
    private int mHintTextColor;
    private int mTextColor;
    private float mTextSize;
    private int mLineColor;
    private boolean mIsMobile;
    private MyTextView mStrategyTV;
    private String mStrategyUrl;

    public InviteCodeWidget(Context context) {
        this(context, null);
    }

    public InviteCodeWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InviteCodeWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoginNew3ControlsWidget, defStyleAttr, 0);
        mHintText = a.getString(R.styleable.LoginNew3ControlsWidget_hint_text);
        mHintTextColor = a.getColor(R.styleable.LoginNew3ControlsWidget_hint_text_color,
                Color.parseColor("#484848"));
        mTextColor = a.getColor(R.styleable.LoginNew3ControlsWidget_text_color,
                Color.parseColor("#484848"));
        mTextSize = a.getDimension(R.styleable.LoginNew3ControlsWidget_text_size, 16f);
        mLineColor = a.getColor(R.styleable.LoginNew3ControlsWidget_line_color, Color.parseColor("#CCCCCC"));
        mIsMobile = a.getBoolean(R.styleable.LoginNew3ControlsWidget_is_mobile, false);
        a.recycle();

        init(context);
        initListener();
    }

    private void init(Context context) {
        //邀请码
        mInviteCodeText = new EditText(context);
        addView(mInviteCodeText);
        mInviteCodeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mInviteCodeText.setTextColor(mTextColor);
        mInviteCodeText.setMinHeight(TransformUtil.dip2px(context, 15f));
        LayoutParams mAccountParams = (LayoutParams) mInviteCodeText.getLayoutParams();
        mAccountParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mAccountParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mInviteCodeText.setLayoutParams(mAccountParams);
        mInviteCodeText.setBackgroundDrawable(null);
        mInviteCodeText.setHintTextColor(mHintTextColor);
        mInviteCodeText.setHint(mHintText);
        if (mIsMobile) {
            mInviteCodeText.setInputType(InputType.TYPE_CLASS_PHONE);
            mInviteCodeText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        }


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


        int radius = TransformUtil.dip2px(context, 12);
        //邀请码攻略
        int minWidth = TransformUtil.dip2px(context, 78);
        mStrategyTV = new MyTextView(context);
        mStrategyTV.setText("邀请码攻略");
        mStrategyTV.setTextSize(12);
        mStrategyTV.setId(R.id.new3_invite_code);
        addView(mStrategyTV);
        //位置
        LayoutParams mStrategyParams = (LayoutParams) mStrategyTV.getLayoutParams();
        mStrategyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mStrategyParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mStrategyTV.setLayoutParams(mStrategyParams);
        //状态
        mStrategyTV.setTextColor(Color.parseColor("#3673FB"));
        mStrategyTV.setGravity(Gravity.CENTER);
        mStrategyTV.setMinHeight(radius * 2);
        mStrategyTV.setMinWidth(minWidth);
        //背景
        GradientDrawable mStrategyBG = new GradientDrawable();
        mStrategyBG.setColor(Color.parseColor("#0D3673FB"));
        mStrategyBG.setCornerRadius(radius);
        mStrategyTV.setBackgroundDrawable(mStrategyBG);

        //删除提示
        int i = TransformUtil.dip2px(context, 10);
        mTipIV = new MyImageView(context);
        mTipIV.setImageResource(R.mipmap.icon_search_del);
        addView(mTipIV);
        RelativeLayout.LayoutParams mTipIVParams = (LayoutParams) mTipIV.getLayoutParams();
        mTipIVParams.addRule(RelativeLayout.LEFT_OF, mStrategyTV.getId());
        mTipIVParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mTipIV.setLayoutParams(mTipIVParams);
        mTipIV.setPadding(i, i, i, i);
        mTipIV.setVisibility(GONE);
    }


    public void setInviteCodeText(String codeText){
        if (TextUtils.isEmpty(codeText) && mInviteCodeText != null){
            mInviteCodeText.setText("");
            return;
        }
        if (mInviteCodeText != null){
            mInviteCodeText.setText(codeText);
            mInviteCodeText.setEnabled(false);
        }
        if (mTipIV != null){
            mTipIV.setVisibility(GONE);
        }
    }


    private void initListener() {
        mInviteCodeText.addTextChangedListener(new SimpleTextWatcher() {
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
                if (mIsMobile) {//处理手机号
                    if (count == 1) {
                        int length = s.toString().length();
                        if (length == 3 || length == 8) {
                            mInviteCodeText.setText(s + " ");
                            mInviteCodeText.setSelection(mInviteCodeText.getText().length());
                        }
                    } else if (count == 0) {
                        int length = s.toString().length();
                        if (length == 4 || length == 9) {
                            mInviteCodeText.setText(mInviteCodeText.getText().toString().trim());
                            mInviteCodeText.setSelection(mInviteCodeText.getText().length());
                        }
                    }
                }
            }
        });

        mTipIV.setOnClickListener(v -> {
            if (mInviteCodeText != null) {
                mInviteCodeText.setText("");
                mTipIV.setVisibility(GONE);
            }
        });

        mStrategyTV.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mStrategyUrl)){
                H5X5Act.startAct(getContext(), mStrategyUrl, H5X5Act.MODE_SONIC);
            }
        });
    }

    /**
     * 获取内容
     *
     * @return
     */
    public CharSequence getText() {
        if (mInviteCodeText != null) {
            String text = mInviteCodeText.getText().toString().trim();
            text = text.replaceAll(" ", "");
            return text;
        }
        return "";
    }

    /**
     * 设置攻略地址
     * @param url
     */
    public void setStrategyUrl(String url){
        this.mStrategyUrl = url;
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
