package com.shunlian.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

/**
 * Created by zhanghe on 2018/8/29.
 * 分享获得金蛋提示
 */
public class ObtainGoldenEggsTip extends LinearLayout {


    private MyTextView topTextView;
    private MyTextView eggsCount;

    public ObtainGoldenEggsTip(Context context) {
        this(context,null);
    }

    public ObtainGoldenEggsTip(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ObtainGoldenEggsTip(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setBackgroundDrawable(getShapDrawable());

        addTextView();

        addGoldenEggsCount();
    }

    private void addGoldenEggsCount() {
        LinearLayout whiteLayout = new LinearLayout(getContext());
        whiteLayout.setOrientation(VERTICAL);

        LinearLayout.LayoutParams
                whiteParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int topMargin = TransformUtil.dip2px(getContext(), 15);
        int minHeight = TransformUtil.dip2px(getContext(), 67.5f);
        int minWidth = TransformUtil.dip2px(getContext(), 127f);
        whiteParams.topMargin = topMargin;
        whiteLayout.setMinimumHeight(minHeight);
        whiteLayout.setMinimumWidth(minWidth);

        whiteLayout.setLayoutParams(whiteParams);
        whiteLayout.setGravity(Gravity.CENTER);
        whiteLayout.setBackgroundDrawable(getWhiteDrawable());


        //add eggs
        MyImageView eggsImage = new MyImageView(getContext());
        eggsImage.setImageResource(R.mipmap.img_renwu_fenxiang_jindan);
        whiteLayout.addView(eggsImage);

        //add text
        eggsCount = new MyTextView(getContext());
        eggsCount.setTextColor(getResources().getColor(R.color.pink_color));
        eggsCount.setTextSize(10);
        eggsCount.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        whiteLayout.addView(eggsCount);

        addView(whiteLayout);
    }

    //添加顶部文字
    private void addTextView() {

        LinearLayout textLayout = new LinearLayout(getContext());
        textLayout.setOrientation(HORIZONTAL);
        textLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int topMargin = TransformUtil.dip2px(getContext(), 9);
        layoutParams.topMargin = topMargin;

        textLayout.setLayoutParams(layoutParams);


        //add left line
        textLayout.addView(getViewLine());

        //add tip text
        topTextView = new MyTextView(getContext());
        topTextView.setText("分享成功");
        topTextView.setTextSize(10);
        topTextView.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams
                textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = TransformUtil.dip2px(getContext(), 6);
        textParams.leftMargin = margin;
        textParams.rightMargin= margin;

        topTextView.setLayoutParams(textParams);

        textLayout.addView(topTextView);

        //add right line
        textLayout.addView(getViewLine());

        addView(textLayout);
    }

    //横线
    private View getViewLine() {
        View viewLine = new View(getContext());
        int w = TransformUtil.dip2px(getContext(), 7);
        int h = TransformUtil.dip2px(getContext(), 1);
        ViewGroup.LayoutParams lineParams = new ViewGroup.LayoutParams(w,h);
        viewLine.setLayoutParams(lineParams);
        viewLine.setBackgroundColor(Color.WHITE);
        return viewLine;
    }

    private GradientDrawable getShapDrawable(){
        int[] colors = {Color.parseColor("#FFFB0036"),Color.parseColor("#FFF17044")};
        GradientDrawable
                gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors);

        int i = TransformUtil.dip2px(getContext(), 5);
        gradientDrawable.setCornerRadius(i);
        return gradientDrawable;
    }

    private GradientDrawable getWhiteDrawable(){

        GradientDrawable gradientDrawable = new GradientDrawable();

        int i = TransformUtil.dip2px(getContext(), 4);
        gradientDrawable.setCornerRadius(i);
        gradientDrawable.setColor(Color.WHITE);
        return gradientDrawable;
    }


    public void setTopTextView(CharSequence sequence){
        if (topTextView != null)
            topTextView.setText(sequence);
    }

    public void setEggsCount(CharSequence sequence){
        if (eggsCount != null){
            eggsCount.setText(sequence);
        }
    }

    /**
     * 默认3秒钟后隐藏
     */
    public void show(){
        setVisibility(VISIBLE);
        postDelayed(()->setVisibility(GONE),3000);
    }

    /**
     *
     * @param delayMillis
     */
    public void show(long delayMillis){
        setVisibility(VISIBLE);
        postDelayed(()->hideAnimation(),delayMillis);
    }

    private void hideAnimation(){
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0,Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,-0.4f);

        AlphaAnimation aa = new AlphaAnimation(1,0);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(ta);
        set.addAnimation(aa);
        set.setDuration(350);
        set.setFillAfter(true);
        set.setFillBefore(false);
        set.setInterpolator(new LinearInterpolator());

        startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
