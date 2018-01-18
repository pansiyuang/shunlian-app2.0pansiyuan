package com.shunlian.app.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 专为ViewPager定制的滑动选项卡 HOME
 */
public class PagerSlidingTabStrip extends ReboundHScrollView implements View.OnClickListener {
    private int currentPosition; // 当前位置
    private int lastOffset;
    private int lastScrollX = 0;
    private float currentPositionOffset; // 当前位置偏移量
    private boolean start;
    private boolean allowWidthFull; // 内容宽度无法充满时，允许自动调整Item的宽度以充满
    private boolean allowAlignmentl;//滑动条是否和文本对齐
    private boolean disableViewPager; // 禁用ViewPager
    private View currentSelectedTabView; // 当前标题项
    private Drawable slidingBlockDrawable; // 滑块
    private ViewPager viewPager; // ViewPager
    private ViewGroup tabsLayout; // 标题项布局
    private ViewPager.OnPageChangeListener onPageChangeListener; // 页面改变监听器
    private OnClickTabListener onClickTabListener;
    private OnPagerChangeLis listener;
    private List<View> tabViews;
    private boolean isAllowDraw;//是否允许绘制滑块
    public boolean isReset;//在界面重画前，是否重置，即需要将isAllowDraw设置为true
    private ArgbEvaluator mColorEvaluator;
    private Paint rectPaint;
    private float indicatorHeight=1.5f;
    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }
    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false); // 隐藏横向滑动提示条
        if (attrs != null) {
            TypedArray attrsTypedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.PagerSlidingTabStrip);
            if (attrsTypedArray != null) {
                allowWidthFull = attrsTypedArray.getBoolean(
                        R.styleable.PagerSlidingTabStrip_allowWidthFull, false);
                slidingBlockDrawable = attrsTypedArray
                        .getDrawable(R.styleable.PagerSlidingTabStrip_slidingBlock);
                disableViewPager = attrsTypedArray.getBoolean(
                        R.styleable.PagerSlidingTabStrip_disableViewPager,
                        false);
                allowAlignmentl = attrsTypedArray.getBoolean(
                        R.styleable.PagerSlidingTabStrip_allowAlignment, false);
//				DisplayMetrics dm = getResources().getDisplayMetrics();
//				tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
//				tabTextSize = attrsTypedArray.getDimensionPixelSize(0, tabTextSize);
                attrsTypedArray.recycle();
            }
        }
        mColorEvaluator  = new ArgbEvaluator();
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
    }
//	 public void setTextSize(int textSizePx) {  
//	        this.tabTextSize = textSizePx;  
//	        updateTabStyles();  
//	    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!allowWidthFull)
            return;
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout == null
                || tabsLayout.getMeasuredWidth() >= getMeasuredWidth())
            return;
        if (tabsLayout.getChildCount() <= 0)
            return;
        if (tabViews == null) {
            tabViews = new ArrayList<View>();
        } else {
            tabViews.clear();
        }
        for (int w = 0; w < tabsLayout.getChildCount(); w++) {
            tabViews.add(tabsLayout.getChildAt(w));
        }
        adjustChildWidthWithParent(tabViews, getMeasuredWidth() - tabsLayout.getPaddingLeft() - tabsLayout.getPaddingRight(), widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    /**
     * 调整views集合中的View，让所有View的宽度加起来正好等于parentViewWidth
     *
     * @param views                   子View集合
     * @param parentViewWidth         父Vie的宽度
     * @param parentWidthMeasureSpec  父View的宽度规则
     * @param parentHeightMeasureSpec 父View的高度规则
     */
    private void adjustChildWidthWithParent(List<View> views,
                                            int parentViewWidth, int parentWidthMeasureSpec,
                                            int parentHeightMeasureSpec) {
        // 先去掉所有子View的外边距
        for (View view : views) {
            if (view.getLayoutParams() instanceof MarginLayoutParams) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view
                        .getLayoutParams();
                parentViewWidth -= lp.leftMargin + lp.rightMargin;
            }
        }
        // 去掉宽度大于平均宽度的View后再次计算平均宽度
        int averageWidth = parentViewWidth / views.size();
        int bigTabCount = views.size();
        while (true) {
            Iterator<View> iterator = views.iterator();
            while (iterator.hasNext()) {
                View view = iterator.next();
                if (view.getMeasuredWidth() > averageWidth) {
                    parentViewWidth -= view.getMeasuredWidth();
                    bigTabCount--;
                    iterator.remove();
                }
            }
            averageWidth = parentViewWidth / bigTabCount;
            boolean end = true;
            for (View view : views) {
                if (view.getMeasuredWidth() > averageWidth) {
                    end = false;
                }
            }
            if (end) {
                break;
            }
        }
        // 修改宽度小于新的平均宽度的View的宽度
        for (View view : views) {
            if (view.getMeasuredWidth() < averageWidth) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = averageWidth;
                view.setLayoutParams(layoutParams);
                // 再次测量让新宽度生效
                if (layoutParams instanceof MarginLayoutParams) {
                    measureChildWithMargins(view, parentWidthMeasureSpec, 0, parentHeightMeasureSpec, 0);
                } else {
                    measureChild(view, parentWidthMeasureSpec, parentHeightMeasureSpec);
                }
            }
        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ViewGroup tabViewGroup = getTabsLayout();
        if (tabViewGroup != null) {
            // 初始化滑块位置以及选中状态
            currentPosition = viewPager != null ? viewPager.getCurrentItem()
                    : 0;
            if (!disableViewPager) {
                scrollToChild(currentPosition, 0); // 移动滑块到指定位置
                selectedTab(currentPosition); // 选中指定位置的TAB
            }
            // 给每一个tab设置点击事件，当点击的时候切换Pager
            for (int w = 0; w < tabViewGroup.getChildCount(); w++) {
                View itemView = tabViewGroup.getChildAt(w);
                itemView.setTag(w);
                itemView.setOnClickListener(this);
            }
        }
    }
    @Override
    public void onClick(View v) {
        int index = (Integer) v.getTag();
        if (onClickTabListener != null) {
            onClickTabListener.onClickTab(v, index);
        }
        if (viewPager != null) {
            viewPager.setCurrentItem(index, true);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (disableViewPager)
            return;
        /* 绘制滑块 */
        if (isAllowDraw) {
            ViewGroup tabsLayout = getTabsLayout();
            if (tabsLayout != null && tabsLayout.getChildCount() > 0 && slidingBlockDrawable != null) {
                View currentTab = tabsLayout.getChildAt(currentPosition);
                View nextTab = tabsLayout.getChildAt(currentPosition + 1);
                float tabStrinpleft;
                float nextTabStrinpleft;
                float slidingBlockLeft;
                float slidingBlockRight;
                float nextTabLeft = 0;
                float nextTabRight = 0;
                if (currentTab != null) {
                    if (allowAlignmentl) {
                        tabStrinpleft = ((RelativeLayout) currentTab).getChildAt(0).getLeft();
                        slidingBlockLeft = currentTab.getLeft() + tabStrinpleft;
                        slidingBlockRight = currentTab.getRight() - tabStrinpleft;
                        if (currentPositionOffset > 0f && currentPosition < tabsLayout.getChildCount() - 1) {
                            if (nextTab != null) {
                                nextTabStrinpleft = ((RelativeLayout) nextTab).getChildAt(0).getLeft();
                                nextTabLeft = nextTab.getLeft() + nextTabStrinpleft;
                                nextTabRight = nextTab.getRight() - nextTabStrinpleft;
                            }
                            //add
                            float set = (float) Math.pow(currentPositionOffset,4);
                            slidingBlockLeft = (set*nextTabLeft + (1f - set) * slidingBlockLeft);
                            slidingBlockRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * slidingBlockRight);
                        } else {
                        }
                    } else {
                        slidingBlockLeft = currentTab.getLeft();
                        slidingBlockRight = currentTab.getRight();
                        if (currentPositionOffset > 0f && currentPosition < tabsLayout.getChildCount() - 1) {
                            nextTab = tabsLayout.getChildAt(currentPosition + 1);
                            if (nextTab != null) {
                                nextTabLeft = nextTab.getLeft();
                                nextTabRight = nextTab.getRight();
                                slidingBlockLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * slidingBlockLeft);
                                slidingBlockRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * slidingBlockRight);
                            }
                        }
                    }
                    slidingBlockDrawable.setBounds((int) slidingBlockLeft, (int) (getHeight()-indicatorHeight), (int) slidingBlockRight, getHeight());
                    slidingBlockDrawable.draw(canvas);
                }
            }
        }
    }
    /**
     * 获取布局
     */
    private ViewGroup getTabsLayout() {
        if (tabsLayout == null) {
            if (getChildCount() > 0) {
                tabsLayout = (ViewGroup) getChildAt(0);
            } else {
                removeAllViews();
                tabsLayout = new LinearLayout(getContext());
                addView(tabsLayout, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        return tabsLayout;
    }
    /**
     * 滚动到指定的位置
     */
    private void scrollToChild(int position, int offset) {
        if (isReset) {
            isAllowDraw = true;
        } else {
            isAllowDraw = false;
        }
        ViewGroup tabsLayout = getTabsLayout();
        if (tabsLayout != null && tabsLayout.getChildCount() > 0
                && position < tabsLayout.getChildCount()) {
            View view = tabsLayout.getChildAt(position);
            if (view != null) {
                // 计算新的X坐标
                int newScrollX = view.getLeft() + offset;
                if (position > 0 || offset > 0) {
                    newScrollX -= 240 - getOffset(view.getWidth()) / 2;
                }
                // 如果同上次X坐标不一样就执行滚动
                if (newScrollX != lastScrollX) {
                    lastScrollX = newScrollX;
                    scrollTo(newScrollX, 0);
                }
            }
        }
    }
    /**
     * 获取偏移量
     */
    private int getOffset(int newOffset) {
        if (lastOffset < newOffset) {
            if (start) {
                lastOffset += 1;
                return lastOffset;
            } else {
                start = true;
                lastOffset += 1;
                return lastOffset;
            }
        }
        if (lastOffset > newOffset) {
            if (start) {
                lastOffset -= 1;
                return lastOffset;
            } else {
                start = true;
                lastOffset -= 1;
                return lastOffset;
            }
        } else {
            start = true;
            lastOffset = newOffset;
            return lastOffset;
        }
    }
    /**
     * 选中指定位置的TAB
     */
    private void selectedTab(int currentSelectedTabPosition) {
        ViewGroup tabsLayout = getTabsLayout();
        if (currentSelectedTabPosition > -1 && tabsLayout != null
                && currentSelectedTabPosition < tabsLayout.getChildCount()) {
            if (currentSelectedTabView != null) {
                currentSelectedTabView.setSelected(false);
            }
            currentSelectedTabView = tabsLayout
                    .getChildAt(currentSelectedTabPosition);
            if (currentSelectedTabView != null) {
                currentSelectedTabView.setSelected(true);
            }
        }
    }
    /**
     * 添加Tab
     */
    public void addTab(View tabView, int index) {
        if (tabView != null) {
            getTabsLayout().addView(tabView, index);
            requestLayout();
        }
    }
    /**
     * 添加Tab
     */
    public void addTab(View tabView) {
        addTab(tabView, -1);
    }
    /**
     * 添加Tab
     *
     * @param tabViews 可以一次添加多个Tab
     */
    public void addTab(View... tabViews) {
        if (tabViews != null) {
            for (View view : tabViews) {
                getTabsLayout().addView(view);
            }
            requestLayout();
        }
    }
    /**
     * 添加Tab
     */
    public void addTab(List<View> tabViews) {
        if (tabViews != null) {
            for (View view : tabViews) {
                getTabsLayout().addView(view);
            }
            requestLayout();
        }
    }
    /**
     * 移除一个tab
     *
     * @param index
     */
    public void removeTab(int index) {
        removeTab(index, 1);
    }
    /**
     * 移除tab
     *
     * @param start 开始位置
     * @param count 移除的数量
     */
    public void removeTab(int start, int count) {
        int tabCount = getTabCount();
        if (start < 0 || start > tabCount) {
            start = 0;
        }
        if (count < 0 || count > tabCount) {
            count = 1;
        }
        if (count - start > tabCount) {
            count = tabCount - start;
        }
        getTabsLayout().removeViews(start, count);
        requestLayout();
    }
    /**
     * 移除所有
     */
    public void removeAllTab() {
        getTabsLayout().removeAllViews();
        requestLayout();
    }
    public View getChild(int idx) {
        return getTabsLayout().getChildAt(idx);
    }
//    public View getBadgeView(int i) {
//        return getTabsLayout().getChildAt(i).findViewById(R.id.tab_mes);
//    }
    /**
     * 设置ViewPager
     *
     * @param viewPager ViewPager
     */
    public void setViewPager(ViewPager viewPager) {
        if (disableViewPager)
            return;
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                LogUtil.httpLogW("----------onPageSelected--------position-"+position);
                selectedTab(position);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
                if (listener != null)
                    listener.onChanged(position);
            }
            @Override
            public void onPageScrolled(int nextPagePosition, float positionOffset, int positionOffsetPixels) {
                LogUtil.httpLogW("----------onPageScrolled--------positionOffset-"+positionOffset);
                ViewGroup tabsLayout = getTabsLayout();
                if (nextPagePosition < tabsLayout.getChildCount()) {
                    View view = tabsLayout.getChildAt(nextPagePosition);
                    if (view != null) {
                        currentPosition = nextPagePosition;
                        currentPositionOffset = positionOffset;
                        scrollToChild(nextPagePosition, (int) (positionOffset * view.getWidth()));
                        isAllowDraw = true;
                        invalidate();
                    }
                }
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(nextPagePosition, positionOffset, positionOffsetPixels);
                }
                int mTextNormalColor = getResources().getColor(R.color.my_gray_three);
                int mTextSelectedColor = getResources().getColor(R.color.pink_color);
                int  evaluateCurrent =(int) mColorEvaluator.evaluate(positionOffset,mTextSelectedColor , mTextNormalColor);//当前tab的颜色值
                int  evaluateThe =(int) mColorEvaluator.evaluate(positionOffset, mTextNormalColor, mTextSelectedColor);// 将要到tab的颜色值
                if (positionOffset != 0.0f) {
                        RelativeLayout mLeft = (RelativeLayout) getChild(nextPagePosition);
                        if(mLeft!=null){
                            TextView title_left = (TextView) mLeft.getChildAt(0);
                                title_left.setTextColor(evaluateCurrent);
                        }
                        RelativeLayout mRight = (RelativeLayout) getChild(nextPagePosition+1);
                        if(mRight!=null){
                            TextView title_right = (TextView) mRight.getChildAt(0);
                                title_right.setTextColor(evaluateThe);
                        }
                }else {
                    for(int i=0;i<getTabsLayout().getChildCount();i++) {
                        RelativeLayout mLeft = (RelativeLayout) getChild(i);
                        if(mLeft!=null){
                            TextView title_left = (TextView) mLeft.getChildAt(0);
                            if(i==nextPagePosition){
                                title_left.setTextColor( mTextSelectedColor);
                            }else {
                                title_left.setTextColor(mTextNormalColor);
                            }
                        }
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.httpLogW("----------onPageScrollStateChanged--------state-"+state);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        requestLayout();
    }
    /**
     * 设置Page切换监听器
     *
     * @param onPageChangeListener Page切换监听器
     */
    public void setOnPageChangeListener(
            ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
    /**
     * 设置是否充满屏幕
     *
     * @param allowWidthFull true：当内容的宽度无法充满屏幕时，自动调整每一个Item的宽度以充满屏幕
     */
    public void setAllowWidthFull(boolean allowWidthFull) {
        this.allowWidthFull = allowWidthFull;
        requestLayout();
    }
    /**
     * 设置滑块图片
     */
    public void setSlidingBlockDrawable(Drawable slidingBlockDrawable) {
        this.slidingBlockDrawable = slidingBlockDrawable;
        requestLayout();
    }
    /**
     * 获取Tab总数
     */
    public int getTabCount() {
        ViewGroup tabsLayout = getTabsLayout();
        return tabsLayout != null ? tabsLayout.getChildCount() : 0;
    }
    /**
     * 设置Tab点击监听器
     *
     * @param onClickTabListener Tab点击监听器
     */
    public void setOnClickTabListener(OnClickTabListener onClickTabListener) {
        this.onClickTabListener = onClickTabListener;
    }
    /**
     * 设置不使用ViewPager
     *
     * @param disableViewPager 不使用ViewPager
     */
    public void setDisableViewPager(boolean disableViewPager) {
        this.disableViewPager = disableViewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(onPageChangeListener);
            viewPager = null;
        }
        requestLayout();
    }
    /**
     * Tab点击监听器
     *
     * @author xiaopan
     */
    public interface OnClickTabListener {
        public void onClickTab(View tab, int index);
    }
    public void setOnPagerChange(OnPagerChangeLis l) {
        this.listener = l;
    }
    public interface OnPagerChangeLis {
        void onChanged(int page);
    }
    /**
     * 每次标题栏重新绘制的时候一定要记得强制重绘滑块，不然滑块会没有,所以强制画滑动条
     */
    public void needTabStrip() {
        isAllowDraw = true;
        isReset = true;
    }
}