package com.shunlian.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;

import java.util.ArrayList;

/**
 * Created by zhanghe on 2018/8/16.
 * 视频和轮播图包装类
 */
public class VideoBannerWrapper extends RelativeLayout {

    protected DisplayMetrics dm;
    private Context mContext;
    protected int itemWidth;
    protected int itemHeight;
    private CustomScaleViewPager mVP;
    private LinearLayout mAllDot;
    private View mScrollDot;
    private int spaceWidthDot;//点之间的间距
    private int startLeftPosi;//最左侧点的距离

    public VideoBannerWrapper(Context context) {
        this(context,null);
    }

    public VideoBannerWrapper(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoBannerWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        dm = context.getResources().getDisplayMetrics();
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseBanner);
        float scale = ta.getFloat(R.styleable.BaseBanner_bb_scale, -1);

        itemWidth = dm.widthPixels;
        if (scale < 0) {//scale not set in xml
            if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
                itemHeight = LayoutParams.MATCH_PARENT;
            } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
                itemHeight = LayoutParams.WRAP_CONTENT;
            } else {
                int[] systemAttrs = {android.R.attr.layout_height};
                TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
                int h = 0;
                a.recycle();
                itemHeight = h;
            }
        } else {
            if (scale > 1) {
                scale = 1;
            }
            itemHeight = (int) (itemWidth * scale);
        }


        mVP = new CustomScaleViewPager(context);
        LayoutParams lp_vp = new LayoutParams(itemWidth, itemHeight);
        addView(mVP, lp_vp);
        //添加轮播指示点
        addIndicator();
    }

    /**
     * 添加轮播指示点
     */
    private void addIndicator() {
        //全部点
        mAllDot = new LinearLayout(mContext);
        mAllDot.setGravity(Gravity.CENTER_HORIZONTAL);
        mAllDot.setOrientation(LinearLayout.HORIZONTAL);

        addView(mAllDot);

        RelativeLayout.LayoutParams allDotRLParams = (LayoutParams) mAllDot.getLayoutParams();
        allDotRLParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        allDotRLParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        allDotRLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,TRUE);
        allDotRLParams.bottomMargin = dp2px(20);
        mAllDot.setLayoutParams(allDotRLParams);


        //高亮点
        mScrollDot = new View(mContext);

        addView(mScrollDot);

        RelativeLayout.LayoutParams scrollDotParams = (LayoutParams) mScrollDot.getLayoutParams();
        int i = dp2px(6);
        scrollDotParams.width = i;
        scrollDotParams.height = i;
        scrollDotParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,TRUE);
        scrollDotParams.bottomMargin = dp2px(20);
        mScrollDot.setLayoutParams(scrollDotParams);
        mScrollDot.setBackgroundResource(R.drawable.red_dot);

        mVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int v = (int) (spaceWidthDot * (positionOffset + position));
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) mScrollDot.getLayoutParams();
                layoutParams.leftMargin = startLeftPosi + v;
                mScrollDot.setLayoutParams(layoutParams);
            }
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    /**
     * 初始化指示点状态
     * @param size
     */
    private void initIndicatorState(int size) {
        mAllDot.removeAllViews();
        int width = dp2px(6);
        for (int i = 0; i < size; i++) {
            View view_dot = new View(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
            lp.leftMargin = width;
            view_dot.setLayoutParams(lp);
            view_dot.setBackgroundResource(R.drawable.red_dot);
            mAllDot.addView(view_dot);
            GradientDrawable gd = (GradientDrawable) view_dot.getBackground();
            gd.setColor(Color.parseColor("#B2B5B5B5"));
        }

        mAllDot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mAllDot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startLeftPosi = mAllDot.getChildAt(0).getLeft();
                spaceWidthDot = mAllDot.getChildAt(1).getLeft() - startLeftPosi;

                GradientDrawable gd = (GradientDrawable) mScrollDot.getBackground();
                gd.setColor(Color.parseColor("#B2FB0036"));
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) mScrollDot.getLayoutParams();
                layoutParams.leftMargin = startLeftPosi;
                mScrollDot.setLayoutParams(layoutParams);

            }
        });
    }

    /**
     *
     * @param path 视频地址
     * @param pics 轮播图片地址
     * @param type 类型 1优品 2 团购
     */
    public void setBanner(String path, ArrayList<String> pics, int type) {
        if (pics.size() == 1){
            removeView(mAllDot);
            removeView(mScrollDot);
        }else {
            initIndicatorState(pics.size());
        }

        if (mVP != null){
            mVP.setBanner(path,pics);
        }
        setLabelPic(type);
    }

    private void setLabelPic(int type) {
        ImageView imageView = new ImageView(getContext());
        addView(imageView, 1);
        LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
        int i = dp2px(50);
        layoutParams.width = i;
        layoutParams.height = i;
        layoutParams.topMargin = dp2px(70);
        layoutParams.leftMargin = i / 5;
        switch (type){
            case 1:
                imageView.setImageResource(R.mipmap.img_plus_youping_xiao);
                break;
            case 2:
                imageView.setImageResource(R.mipmap.img_biaoqian_tuangou);
                break;
        }
    }

    protected int dp2px(float dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }
}
