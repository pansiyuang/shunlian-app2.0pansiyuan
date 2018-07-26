package com.shunlian.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by zhanghe on 2018/7/23.
 */

public class AnimationEditText extends RelativeLayout {

    private TextView tvAnim;

    public AnimationEditText(Context context) {
        this(context,null);
    }

    public AnimationEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AnimationEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = TransformUtil.dip2px(getContext(),50);
        setLayoutParams(layoutParams);
        removeAllViews();
        addTipView();
        addEditText();

        this.setOnClickListener(v -> {
            runAnimation();
        });
    }

    private void runAnimation() {
        /*TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0
                ,Animation.RELATIVE_TO_SELF, 0,Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,-2);

        ta.setDuration(200);
        if (tvAnim != null) {
            tvAnim.setVisibility(VISIBLE);
            tvAnim.setAnimation(ta);
        }*/


    }

    private void addEditText() {
        TextView tv_tip = new TextView(getContext());
        tv_tip.setTextSize(15);
        tv_tip.setTextColor(getResources().getColor(R.color.color_value_6c));
        tv_tip.setText("请输入手机号");

        addView(tv_tip);
    }

    private void addTipView() {

        tvAnim = new TextView(getContext());
        tvAnim.setTextSize(15);
        tvAnim.setTextColor(getResources().getColor(R.color.color_value_6c));
        tvAnim.setText("手机号");
        addView(tvAnim);
        tvAnim.setVisibility(GONE);
    }


}
