package com.shunlian.app.widget.empty;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

/**
 * Created by Administrator on 2017/11/28.
 * 网络和空界面
 */

public class NetAndEmptyInterface extends LinearLayout {

    private MyImageView imageView;
    private MyTextView textView;
    private MyButton button;

    public NetAndEmptyInterface(Context context) {
        this(context, null);
    }

    public NetAndEmptyInterface(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetAndEmptyInterface(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.white));
        setGravity(Gravity.CENTER);

        addImageView();

        addTextView();

        addButton();

    }

    private void addImageView() {
        imageView = new MyImageView(getContext());
        addView(imageView,0);
    }

    private void addTextView() {
        textView = new MyTextView(getContext());
        textView.setTextColor(getResources().getColor(R.color.color_value_6c));
        textView.setTextSize(15);
        textView.setGravity(Gravity.CENTER);
        addView(textView,1);
        LayoutParams textParams1 = (LayoutParams) textView.getLayoutParams();
        textParams1.topMargin = TransformUtil.dip2px(getContext(), 20);
        textView.setLayoutParams(textParams1);
    }

    private int paddingTop = 0;
    public void setTextPaddingTop(int top){
        this.paddingTop = TransformUtil.dip2px(getContext(), top);
        LayoutParams textParams1 = (LayoutParams) textView.getLayoutParams();
        textParams1.topMargin = paddingTop;
    }

    private void addButton() {
        button = new MyButton(getContext());
        button.setBackgroundResource(R.drawable.rounded_rectangle_lin_gray_4px);
        button.setTextSize(15);
        button.setGravity(Gravity.CENTER);
        button.setTextColor(getResources().getColor(R.color.new_gray));
        button.setWHProportion(250, 65);
        addView(button,2);
        LayoutParams buttonParams = (LayoutParams) button.getLayoutParams();
        buttonParams.topMargin = TransformUtil.dip2px(getContext(), 30);
        button.setLayoutParams(buttonParams);
    }
    /**
     * 网络异常
     *
     * @return 返回button控件，以便于直接设置点击监听
     */
    public MyButton setNetExecption() {
        if (imageView == null){
            removeViewAt(0);
            addImageView();
        }
        imageView.setImageResource(R.mipmap.img_empty_wuwangluo);

        if (textView == null){
            removeViewAt(1);
            addTextView();
        }
        textView.setText("网络状态待提升");

        if (button == null){
            removeViewAt(2);
            addButton();
        }
        button.setText("点击重试");

        return button;
    }

    /**
     * 设置空页面图片
     *
     * @param resId
     * @return
     */
    public NetAndEmptyInterface setImageResource(@DrawableRes int resId) {
        if (imageView == null){
            removeViewAt(0);
            addImageView();
        }
        imageView.setImageResource(resId);
        return this;
    }

    /**
     * 设置文字说明
     *
     * @param text
     * @return
     */
    public NetAndEmptyInterface setText(CharSequence text) {
        if (textView == null){
            removeViewAt(1);
            addTextView();
        }
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(GONE);
        } else {
            textView.setVisibility(VISIBLE);
            textView.setText(text);
        }
        return this;
    }

    /**
     * 设置按钮上面的文字
     *
     * @param text
     * @return 返回button控件，以便于直接设置点击监听
     */
    public MyButton setButtonText(CharSequence text) {
        if (button == null){
            removeViewAt(2);
            addButton();
        }
        if (TextUtils.isEmpty(text)) {
            button.setVisibility(GONE);
        } else {
            button.setVisibility(VISIBLE);
            button.setText(text);
        }
        return button;
    }
}
