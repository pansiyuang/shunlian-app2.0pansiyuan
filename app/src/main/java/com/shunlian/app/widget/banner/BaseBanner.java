package com.shunlian.app.widget.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.DiscoveryCircleEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.sideslip.callbak.OnSlideListenerAdapter;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.OnPageChange;

public abstract class BaseBanner<E, T extends BaseBanner<E, T>> extends RelativeLayout {
    protected static final String TAG = BaseBanner.class.getSimpleName();
    protected ScheduledExecutorService stse;
    protected Context context;
    protected DisplayMetrics dm;
    protected int dimColor;
    public int  layoutRes=R.layout.layout_kanner_round_indicator;
    /**
     * ViewPager
     */
    protected ViewPager vp;
    protected LayoutParams lp_vp;
    protected List<E> list = new ArrayList<>();
    protected int currentPositon;
    protected int lastPositon;
    protected InnerBannerAdapter innerAdapter;

    protected long delay;
    protected long period;
    protected boolean isAutoScrollEnable;
    protected boolean isAutoScrolling;
    protected int scrollSpeed = 450;
    protected Class<? extends ViewPager.PageTransformer> transformerClass;

    /**
     * top parent of indicators
     */
    protected RelativeLayout rl_bottom_bar_parent;
    protected int itemWidth;
    protected int itemHeight;

    /**
     * container of indicators and title
     */
    protected LinearLayout ll_bottom_bar;
    protected boolean isBarShowWhenLast;

    /**
     * container of indicators
     */
    protected LinearLayout ll_indicator_container;

    protected float scale;

    private MyTextView titleOne, titleTwo;

    private List<StoreIndexEntity.Body.Datas> mDatas;

    private float slideBackWidth;//侧滑宽度
    private boolean isSlideBackDispatch;//是否进行侧滑兼容处理

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            scrollToNextItem(currentPositon);
        }
    };
    private boolean isSlideBackLock;//侧滑是否关闭
    //listener
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private ViewPager.OnPageChangeListener internelPageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            currentPositon = position % list.size();

            setCurrentIndicator(currentPositon);

            if (onPageChanged!=null)
            onPageChanged.onPageChange(currentPositon);

            textChange(currentPositon + 1);

            ll_bottom_bar.setVisibility(currentPositon == list.size() - 1 && !isBarShowWhenLast ? GONE : VISIBLE);

            lastPositon = currentPositon;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };
    private OnItemClickL onItemClickL;
    private onPageChanged onPageChanged;

    public BaseBanner(Context context) {
        this(context, null, 0);
    }

    public BaseBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        if (context instanceof BaseActivity) {
            isSlideBackDispatch = true;
            handlerSlideBack();
        }

        dm = context.getResources().getDisplayMetrics();

        //get custom attr
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseBanner);
        scale = ta.getFloat(R.styleable.BaseBanner_bb_scale, -1);

        boolean isLoopEnable = ta.getBoolean(R.styleable.BaseBanner_bb_isLoopEnable, true);
        delay = ta.getInt(R.styleable.BaseBanner_bb_delay, 5);
        period = ta.getInt(R.styleable.BaseBanner_bb_period, 5);
        isAutoScrollEnable = ta.getBoolean(R.styleable.BaseBanner_bb_isAutoScrollEnable, true);
        dimColor = ta.getColor(R.styleable.BaseBanner_bb_dimColor, -1);

        int barColor = ta.getColor(R.styleable.BaseBanner_bb_barColor, Color.TRANSPARENT);
        isBarShowWhenLast = ta.getBoolean(R.styleable.BaseBanner_bb_isBarShowWhenLast, true);
        int indicatorGravity = ta.getInt(R.styleable.BaseBanner_bb_indicatorGravity, Gravity.CENTER);
        float barPaddingLeft = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingLeft, dp2px(10));
        float barPaddingTop = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingTop, dp2px(indicatorGravity == Gravity.CENTER ? 6 : 2));
        float barPaddingRight = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingRight, dp2px(10));
        float barPaddingBottom = ta.getDimension(R.styleable.BaseBanner_bb_barPaddingBottom, dp2px(indicatorGravity == Gravity.CENTER ? 6 : 2));
        int textColor = ta.getColor(R.styleable.BaseBanner_bb_textColor, Color.parseColor("#ffffff"));
        float textSize = ta.getDimension(R.styleable.BaseBanner_bb_textSize, sp2px(12.5f));
        boolean isTitleShow = ta.getBoolean(R.styleable.BaseBanner_bb_isTitleShow, true);
        boolean isIndicatorShow = ta.getBoolean(R.styleable.BaseBanner_bb_isIndicatorShow, true);
        ta.recycle();

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        //create ViewPager
        vp = isLoopEnable ? new LoopViewPager(context) : new ViewPager(context);
        itemWidth = dm.widthPixels;
        if (scale < 0) {//scale not set in xml
            if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
                Log.d(TAG, "MATCH_PARENT--->" + height);
                itemHeight = LayoutParams.MATCH_PARENT;
            } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
                Log.d(TAG, "WRAP_CONTENT--->" + height);
                itemHeight = LayoutParams.WRAP_CONTENT;
            } else {
                int[] systemAttrs = {android.R.attr.layout_height};
                TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
//                int h = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                int h = 0;
                a.recycle();
                Log.d(TAG, "EXACT_NUMBER--->" + h);
                itemHeight = h;
            }
        } else {
            if (scale > 1) {
                scale = 1;
            }
            itemHeight = (int) (itemWidth * scale);
            Log.d(TAG, "scale--->" + scale);
        }


        lp_vp = new LayoutParams(itemWidth, itemHeight);
        LogUtil.augusLogW("yxf33---"+itemWidth);
        LogUtil.augusLogW("yxf44---"+itemHeight);
        addView(vp, lp_vp);

        //top parent of indicators
        rl_bottom_bar_parent = new RelativeLayout(context);
        addView(rl_bottom_bar_parent, lp_vp);

        //container of indicators and title
        ll_bottom_bar = new LinearLayout(context);
        LayoutParams lp2 = new LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rl_bottom_bar_parent.addView(ll_bottom_bar, lp2);

        ll_bottom_bar.setBackgroundColor(barColor);
        ll_bottom_bar.setPadding((int) barPaddingLeft, (int) barPaddingTop, (int) barPaddingRight, (int) barPaddingBottom);
        ll_bottom_bar.setClipChildren(false);
        ll_bottom_bar.setClipToPadding(false);

        //container of indicators
        ll_indicator_container = new LinearLayout(context);
        ll_indicator_container.setGravity(Gravity.CENTER);
        ll_indicator_container.setVisibility(isIndicatorShow ? VISIBLE : INVISIBLE);
        ll_indicator_container.setClipChildren(false);
        ll_indicator_container.setClipToPadding(false);


        if (indicatorGravity == Gravity.CENTER) {
            ll_bottom_bar.setGravity(Gravity.CENTER);
            ll_bottom_bar.addView(ll_indicator_container);
        } else {
            if (indicatorGravity == Gravity.RIGHT) {
                ll_bottom_bar.setGravity(Gravity.CENTER_VERTICAL);
                ll_bottom_bar.addView(ll_indicator_container);

            } else if (indicatorGravity == Gravity.LEFT) {
                ll_bottom_bar.setGravity(Gravity.CENTER_VERTICAL);
                ll_bottom_bar.addView(ll_indicator_container);

            }
        }
    }

    /**
     * create viewpager item layout
     */
    public abstract View onCreateItemView(int position);

    /**
     * create indicator
     */
    public abstract View onCreateIndicator();

    /**
     * set indicator show status, select or unselect
     */
    public abstract void setCurrentIndicator(int position);

    /**
     * Override this method to set title content when vp scroll to the position,
     * also you can set title attr,such as textcolor and etc.
     * if setIndicatorGravity == Gravity.CENTER,do nothing.
     */
    public void onTitleSlect(TextView tv, int position) {
    }

    public void textChange(int currentPositon) {
        if (mDatas != null && mDatas.size() > 1) {
            titleOne.setText(currentPositon + "/" + mDatas.size());
            titleTwo.setText(mDatas.get(currentPositon - 1).description);
        }
    }

    /**
     * set data source list
     */
    public void setBanner(List<E> list) {
        this.list = list;
        if (list != null && list.size() == 1) {
            removeAllViews();
            MyImageView myImageView = new MyImageView(getContext());
            myImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            GlideUtils.getInstance().loadImage(getContext(), myImageView, (String) list.get(0));
            addView(myImageView, 0, lp_vp);
            myImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickL != null) {
                        onItemClickL.onItemClick(0);
                    }
                }
            });
        }
        startScroll();
    }

    public void setBanners(List<E> list, MyTextView mtv_one, MyTextView mtv_two) {
        this.list = list;
        mDatas = (List<StoreIndexEntity.Body.Datas>) list;
        titleOne = mtv_one;
        titleOne.setVisibility(VISIBLE);
        titleTwo = mtv_two;
        if (mDatas != null && mDatas.size() == 1) {
            removeAllViews();
            MyImageView myImageView = new MyImageView(getContext());
            myImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideUtils.getInstance().loadImage(getContext(), myImageView, mDatas.get(0).whole_thumb);
            titleOne.setVisibility(GONE);
            titleTwo.setText(mDatas.get(0).description);
            addView(myImageView, 0, lp_vp);
            myImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickL != null) {
                        onItemClickL.onItemClick(0);
                    }
                }
            });
        }
        startScroll();
    }

    /**
     * set scroll delay before start scroll,unit second,default 5 seconds
     */
    public T setDelay(long delay) {
        this.delay = delay;
        return (T) this;
    }

    /**
     * set scroll period,unit second,default 5 seconds
     */
    public T setPeriod(long period) {
        this.period = period;
        return (T) this;
    }

    /**
     * set auto scroll enable for LoopViewPager,default true
     */
    public T setAutoScrollEnable(boolean isAutoScrollEnable) {
        this.isAutoScrollEnable = isAutoScrollEnable;
        return (T) this;
    }

    /**
     * set page transformer,only valid for API 3.0 and up since V1.1.0
     */
    public T setTransformerClass(Class<? extends ViewPager.PageTransformer> transformerClass) {
        this.transformerClass = transformerClass;
        return (T) this;
    }

    /**
     * set bootom bar color,default transparent
     */
    public T setBarColor(int barColor) {
        ll_bottom_bar.setBackgroundColor(barColor);
        return (T) this;
    }

    /**
     * set bottom bar show or not when the position is the last,default true
     */
    public T setBarShowWhenLast(boolean isBarShowWhenLast) {
        this.isBarShowWhenLast = isBarShowWhenLast;
        return (T) this;
    }

    /**
     * set bottom bar padding,unit dp
     */
    public T barPadding(float left, float top, float right, float bottom) {
        ll_bottom_bar.setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom));
        return (T) this;
    }


    /**
     * set indicator show or not,default true
     */
    public T setIndicatorShow(boolean isIndicatorShow) {
        ll_indicator_container.setVisibility(isIndicatorShow ? VISIBLE : INVISIBLE);
        return (T) this;
    }

    /**
     * scroll to next item
     */
    private void scrollToNextItem(int position) {
        position++;
        vp.setCurrentItem(position);
    }

    /**
     * set viewpager
     */
    private void setViewPager() {
        innerAdapter = new InnerBannerAdapter();
        vp.setAdapter(innerAdapter);
        vp.setOffscreenPageLimit(list.size());

        try {
            if (transformerClass != null) {
                vp.setPageTransformer(true, transformerClass.newInstance());
                if (isLoopViewPager()) {
                    scrollSpeed = 550;
                    setScrollSpeed();
                }
            } else {
                if (isLoopViewPager()) {
                    scrollSpeed = 450;
                    setScrollSpeed();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (internelPageListener != null) {
            vp.removeOnPageChangeListener(internelPageListener);
        }
        vp.addOnPageChangeListener(internelPageListener);
    }

    public void startScroll() {
        if (list == null) {
            throw new IllegalStateException("Data source is empty,you must setBanner(List<E> list) before startScroll()");
        }
        if (onPageChanged!=null)
        onPageChanged.onPageChange(currentPositon);
        textChange(currentPositon + 1);
        setViewPager();
        //create indicator
        View indicatorViews = onCreateIndicator();
        if (indicatorViews != null) {
            ll_indicator_container.removeAllViews();
            ll_indicator_container.addView(indicatorViews);
        }

        goOnScroll();
    }

    /**
     * for LoopViewPager
     */
    public void goOnScroll() {
        if (!isValid()) {
            return;
        }

        if (isAutoScrolling) {
            return;
        }
        if (isLoopViewPager() && isAutoScrollEnable) {
            pauseScroll();
            stse = Executors.newSingleThreadScheduledExecutor();
            stse.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    handler.obtainMessage().sendToTarget();
                }
            }, delay, period, TimeUnit.SECONDS);
            isAutoScrolling = true;
            Log.d(TAG, this.getClass().getSimpleName() + "--->goOnScroll()");
        } else {
            isAutoScrolling = false;
        }
    }

    /**
     * for LoopViewPager
     */
    public void pauseScroll() {
        if (stse != null) {
            stse.shutdown();
            stse = null;
        }
        Log.d(TAG, this.getClass().getSimpleName() + "--->pauseScroll()");

        isAutoScrolling = false;
    }

    //ViewPager
    public ViewPager getViewPager() {
        return vp;
    }

    private void handlerSlideBack() {
        if (isSlideBackDispatch) {
            BaseActivity ba = (BaseActivity) getContext();
            slideBackWidth = DeviceInfoUtil.getDeviceWidth(context)
                    * ba.getSlideBackEdgePercent();
            isSlideBackLock = ba.getIsSlideBackLock();
            if (!isSlideBackLock)
                ba.setSlideBackListener(new OnSlideListenerAdapter() {
                    @Override
                    public void onSlide(float percent) {
                        super.onSlide(percent);
                        if (percent == 0) {
                            goOnScroll();
                        }
                    }
                });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        LogUtil.zhLogW(ev.getX()+"================="+ev.getAction());

//        LogUtil.zhLogW("slideBackWidth==="+slideBackWidth);

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseScroll();
                float currentScrollPosition = ev.getX();
                if (isSlideBackDispatch && !isSlideBackLock && currentScrollPosition < slideBackWidth) {
                    return false;
                }
//                LogUtil.zhLogW("dispatchTouchEvent--->ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                goOnScroll();
//                LogUtil.zhLogW("dispatchTouchEvent--->ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                goOnScroll();
//                LogUtil.zhLogW("dispatchTouchEvent--->ACTION_CANCEL");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility != VISIBLE) {
//            Log.d(TAG, this.getClass().getSimpleName() + "--->onWindowVisibilityChanged");
            pauseScroll();
        } else {
            goOnScroll();
        }
    }

    /**
     * set scroll speed
     */
    private void setScrollSpeed() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
            FixedSpeedScroller myScroller = new FixedSpeedScroller(context, interpolator, scrollSpeed);
            mScroller.set(vp, myScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int dp2px(float dp) {
        float scale = this.context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }

    private float sp2px(float sp) {
        final float scale = this.context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    protected boolean isLoopViewPager() {
        return vp instanceof LoopViewPager;
    }

    protected boolean isValid() {
        if (vp == null) {
            Log.e(TAG, "ViewPager is not exist!");
            return false;
        }

        if (list == null || list.size() == 0) {
            Log.e(TAG, "DataList must be not empty!");
            return false;
        }

        return true;
    }

//    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
//        onPageChangeListener = listener;
//    }

    public void onPageChangeCall(onPageChanged onPageChanged) {
        this.onPageChanged = onPageChanged;
    }

    public interface onPageChanged {
        void onPageChange(int position);
    }


    public void setOnItemClickL(OnItemClickL onItemClickL) {
        this.onItemClickL = onItemClickL;
    }

    public interface OnItemClickL {
        void onItemClick(int position);
    }

    private class InnerBannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View inflate = onCreateItemView(position);
            inflate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickL != null) {
                        onItemClickL.onItemClick(position);
                    }
                }
            });
            container.addView(inflate);

            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
