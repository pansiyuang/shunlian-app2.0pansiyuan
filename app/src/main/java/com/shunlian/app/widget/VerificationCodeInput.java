package com.shunlian.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

import java.util.ArrayList;
import java.util.List;

public class VerificationCodeInput extends LinearLayout implements TextWatcher, View.OnKeyListener {

    private int box = 4;
    private int childPadding;
    private Listener listener;
    private List<EditText> mEditTextList = new ArrayList<>();
    private int currentPosition = 0;
    private Context mContext;

    public VerificationCodeInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeInput);
        box = a.getInt(R.styleable.vericationCodeInput_box, 4);

        childPadding = (int) a.getDimension(R.styleable.vericationCodeInput_padding, TransformUtil.dip2px(mContext, 10));
        a.recycle();
        initViews();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();


    }

    private void initViews() {
        for (int i = 0; i < box; i++) {
            MyEditText editText = new MyEditText(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            editText.setOnKeyListener(this);
            editText.setTag(i);
            editText.setTextColor(Color.BLACK);
            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            editText.setBackgroundDrawable(null);
            Drawable drawable = getResources().getDrawable(R.drawable.edittext_line);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            editText.setCompoundDrawables(null, null, null, drawable);
            editText.setCompoundDrawablePadding(TransformUtil.dip2px(mContext, 10));
            if (i == 0){
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
            }
            editText.setEms(1);
            editText.addTextChangedListener(this);
            addView(editText, i);
            mEditTextList.add(editText);
        }
    }

    private void backFocus() {
        int count = getChildCount();
        EditText editText;
        for (int i = count - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() == 1) {
                editText.requestFocus();
                editText.setSelection(1);
                return;
            }
        }
    }

    private void focus() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < box; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                break;
            } else {
                stringBuilder.append(content);
                if (listener != null && i == mEditTextList.size() - 1) {
                    listener.onComplete(stringBuilder.toString());
                }
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void setOnCompleteListener(Listener listener) {
        this.listener = listener;
    }

    @Override

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        if (count > 0) {
            View child = getChildAt(0);
            int cHeight = child.getMeasuredHeight();
            int cWidth = child.getMeasuredWidth();
            int maxH = cHeight;
            int maxW = cWidth * box + childPadding * (box - 1);
            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec), resolveSize(maxH, heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int parentWidth = getMeasuredWidth();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            child.setVisibility(View.VISIBLE);
            int cWidth = (parentWidth - (childPadding * (childCount - 1))) / childCount;
            int cHeight = child.getMeasuredHeight();
            int cl, cr;
            if (i == 0) {
                cl = 0;
                cr = cWidth;
            } else {
                cl = (i * cWidth) + (i * childPadding);
                cr = (i + 1) * cWidth + (i * childPadding);
            }
            int ct = 0;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0 && count >= 1 && currentPosition != mEditTextList.size() - 1) {
            currentPosition++;
            mEditTextList.get(currentPosition).requestFocus();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
        } else {
            focus();
            checkAndCommit();
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        EditText editText = (EditText) view;
        if (keyCode == KeyEvent.KEYCODE_DEL && editText.getText().length() == 0) {
            int action = event.getAction();
            currentPosition = (int) editText.getTag();
            if (currentPosition != 0 && action == KeyEvent.ACTION_DOWN) {
                currentPosition--;
                mEditTextList.get(currentPosition).requestFocus();
                mEditTextList.get(currentPosition).setText("");
            }
        }
        return false;
    }

    public interface Listener {
        void onComplete(String content);
    }
}