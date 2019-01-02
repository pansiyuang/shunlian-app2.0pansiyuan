package com.shunlian.app.widget.popmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.shunlian.app.R;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 弹出菜单
 * Created by HanHailong on 16/2/17.
 */
public class PopMenu {


    /**
     * 默认的列数为3个
     */
    private static final int DEFAULT_COLUMN_COUNT = 3;

    /**
     * 动画时间
     */
    private static final int DEFAULT_DURATION = 300;

    /**
     * 拉力系数
     */
    private static final int DEFAULT_TENSION = 40;
    /**
     * 摩擦力系数
     */
    private static final int DEFAULT_FRICTION = 5;

    /**
     * item水平之间的间距
     */
    private static final int DEFAULT_HORIZONTAL_PADDING = 0;
    /**
     * item竖直之间的间距
     */
    private static final int DEFAULT_VERTICAL_PADDING = 15;

    private Activity mActivity;
    private int mColumnCount;
    private List<PopMenuItem> mMenuItems = new ArrayList<>();
    private FrameLayout mAnimateLayout;
    private GridLayout mGridLayout;
    private int mDuration;
    private double mTension;
    private double mFriction;
    private int mHorizontalPadding;
    private int mVerticalPadding;
    private PopMenuItemCallback popMenuItemCallback;
    private boolean isFull;

    private int mScreenWidth;
    private int mScreenHeight;

    private boolean isShowing = false;
//    private Handler mHander;

    private PopMenu(Builder builder) {
        this.mActivity = builder.activity;
        this.mMenuItems.clear();
        this.mMenuItems.addAll(builder.itemList);

        this.mColumnCount = builder.columnCount;
        this.mDuration = builder.duration;
        this.mTension = builder.tension;
        this.mFriction = builder.friction;
        this.mHorizontalPadding = builder.horizontalPadding;
        this.mVerticalPadding = builder.verticalPadding;
        this.popMenuItemCallback = builder.popMenuItemCallback;
        this.isFull = builder.isfull;

        mScreenWidth = mActivity.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = mActivity.getResources().getDisplayMetrics().heightPixels;

    }

    /**
     * 显示菜单
     */
    public void show() {
        buildAnimateGridLayout();

        if (mAnimateLayout.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) mAnimateLayout.getParent();
            viewGroup.removeView(mAnimateLayout);
        }

        ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        ViewGroup contentView = (ViewGroup) decorView.findViewById(android.R.id.content);
        contentView.addView(mAnimateLayout);

        //执行显示动画
        showSubMenus(mGridLayout);

        isShowing = true;
        if (!isFull)
        ImmersionBar.with(mActivity).shareSever(0.8f).init();
    }

    /**
     * 隐藏菜单
     */
    public void hide() {
        try {
            //先执行消失的动画
            if (isShowing && mGridLayout != null) {
                hideSubMenus(mGridLayout, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
                        ViewGroup contentView = (ViewGroup) decorView.findViewById(android.R.id.content);
                        contentView.removeView(mAnimateLayout);
                    }
                });
                isShowing = false;
                popMenuItemCallback.onHideCallback(mActivity);
//            if (mHander==null)
//                mHander=new Handler();
//            mHander.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    popMenuItemCallback.onHideCallback(mActivity);
//                }
//            },60);
            }
        }catch (Exception e ){

        }

    }

    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 构建动画布局
     */
    private void buildAnimateGridLayout() {
        mAnimateLayout = new FrameLayout(mActivity);
        mAnimateLayout.setBackgroundColor(Common.getResources().getColor(R.color.dim20));
        mAnimateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popMenuItemCallback != null){
                    popMenuItemCallback.onClickClose(view);
                }
                hide();
            }
        });
        mGridLayout = new GridLayout(mActivity);
        mGridLayout.setColumnCount(mColumnCount);
        mGridLayout.setBackgroundColor(Common.getResources().getColor(R.color.white));
        mGridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        int hPadding = dp2px(mActivity, mHorizontalPadding);
//        int vPadding = dp2px(mActivity, mVerticalPadding);
        int itemWidth = (mScreenWidth - (mColumnCount + 1) * hPadding) / mColumnCount;

//        int rowCount = mMenuItems.size() % mColumnCount == 0 ? mMenuItems.size() / mColumnCount :
//                mMenuItems.size() / mColumnCount + 1;

//        int topMargin = (mScreenHeight - (itemWidth + vPadding) * rowCount + vPadding) / 8 * 7;

        for (int i = 0; i < mMenuItems.size(); i++) {
            final int position = i;
            PopSubView subView = new PopSubView(mActivity);
            PopMenuItem menuItem = mMenuItems.get(i);
            subView.setPopMenuItem(menuItem);
            subView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popMenuItemCallback != null) {
                        popMenuItemCallback.onItemClick(PopMenu.this, position);
                    }
                    hide();
                }
            });

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = itemWidth;
            lp.leftMargin = hPadding;
            lp.setGravity(Gravity.CENTER);
//            if (i / mColumnCount == 0) {
//                lp.topMargin = topMargin;
//            } else {
//                lp.topMargin = vPadding;
//            }
            mGridLayout.addView(subView, lp);
        }
        FrameLayout.LayoutParams mGridLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                TransformUtil.dip2px(mActivity,165), Gravity.BOTTOM );
        mAnimateLayout.addView(mGridLayout,mGridLayoutParams);


        FrameLayout titleLayout=new FrameLayout(mActivity);
        FrameLayout.LayoutParams myLinearLayoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                TransformUtil.dip2px(mActivity,50),Gravity.BOTTOM);
        myLinearLayoutParams.bottomMargin= TransformUtil.dip2px(mActivity,165);
        titleLayout.setBackgroundColor(Common.getResources().getColor(R.color.white));
        titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ImageView mCloseIv = new ImageView(mActivity);
        mCloseIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mCloseIv.setImageResource(R.mipmap.ion_xiaodian_cha);
        mCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popMenuItemCallback != null){
                    popMenuItemCallback.onClickClose(v);
                }
                hide();
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin=TransformUtil.dip2px(mActivity,20);
        titleLayout.addView(mCloseIv, layoutParams);

        TextView textView = new TextView(mActivity);
        FrameLayout.LayoutParams tvLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        textView.setText("分享");
        textView.setTextColor(Common.getResources().getColor(R.color.new_text));
        textView.setTextSize(18);
        titleLayout.addView(textView, tvLp);

        View view = new View(mActivity);
        FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                TransformUtil.dip2px(mActivity,0.5f), Gravity.BOTTOM);
        view.setBackgroundColor(Common.getResources().getColor(R.color.light_gray_three));
        titleLayout.addView(view, viewParams);

        mAnimateLayout.addView(titleLayout,myLinearLayoutParams);
    }

    /**
     * show sub menus with animates
     *
     * @param viewGroup
     */
    private void showSubMenus(ViewGroup viewGroup) {
//        if (viewGroup == null) return;
//        int childCount = viewGroup.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View view = viewGroup.getChildAt(i);
//            animateViewDirection(viewGroup,view, mScreenHeight, 0, mTension, mFriction);
//        }
        animateViewDirection(viewGroup, null, mScreenHeight, 0, mTension, mFriction);

    }

    /**
     * hide sub menus with animates
     *
     * @param viewGroup
     * @param listener
     */
    private void hideSubMenus(ViewGroup viewGroup, final AnimatorListenerAdapter listener) {
        if (viewGroup == null) return;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.animate().translationY(mScreenHeight).setDuration(mDuration).setListener(listener).start();
        }
    }

    /**
     * 弹簧动画
     *
     * @param
     * @param from
     * @param to
     * @param tension  拉力系数
     * @param friction 摩擦力系数
     */
    private void animateViewDirection(ViewGroup viewGroup, final View v, float from, float to, double tension, double friction) {
//        Spring spring = mSpringSystem.createSpring();
//        spring.setCurrentValue(from);
//        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(tension, friction));
//        spring.addListener(new SimpleSpringListener() {
//            @Override
//            public void onSpringUpdate(Spring spring) {
//                v.setTranslationY((float) spring.getCurrentValue());
//            }
//        });
//        spring.setEndValue(to);
        SpringChain springChain = SpringChain.create();

        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View view = viewGroup.getChildAt(i);
            springChain.addSpring(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    view.setTranslationY((float) spring.getCurrentValue());

                }
            });
        }


        List<Spring> springs = springChain.getAllSprings();
        for (int i = 0; i < springs.size(); i++) {
//            springs.get(i).setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(tension, friction));
            springs.get(i).setCurrentValue(from);
        }
        //setControlSpringIndex(0)设置单排起始位置
        springChain.setControlSpringIndex(0).getControlSpring().setEndValue(to);

    }


    /**
     * dp 2 px
     *
     * @param context
     * @param dpVal
     * @return
     */
    protected int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static class Builder {

        private Activity activity;
        private int columnCount = DEFAULT_COLUMN_COUNT;
        private List<PopMenuItem> itemList = new ArrayList<>();
        private int duration = DEFAULT_DURATION;
        private double tension = DEFAULT_TENSION;
        private double friction = DEFAULT_FRICTION;
        private int horizontalPadding = DEFAULT_HORIZONTAL_PADDING;
        private int verticalPadding = DEFAULT_VERTICAL_PADDING;
        private PopMenuItemCallback popMenuItemCallback;
        private boolean isfull=false;

        public Builder setIsfull(boolean isfull) {
            this.isfull = isfull;
            return this;
        }

        public Builder attachToActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder columnCount(int count) {
            this.columnCount = count;
            return this;
        }

        public Builder addMenuItem(PopMenuItem menuItem) {
            this.itemList.add(menuItem);
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder tension(double tension) {
            this.tension = tension;
            return this;
        }

        public Builder friction(double friction) {
            this.friction = friction;
            return this;
        }

        public Builder horizontalPadding(int padding) {
            this.horizontalPadding = padding;
            return this;
        }

        public Builder verticalPadding(int padding) {
            this.verticalPadding = padding;
            return this;
        }

        public Builder setOnItemClickListener(PopMenuItemCallback listener) {
            this.popMenuItemCallback = listener;
            return this;
        }

        public PopMenu build() {
            final PopMenu popMenu = new PopMenu(this);
            return popMenu;
        }
    }
}
