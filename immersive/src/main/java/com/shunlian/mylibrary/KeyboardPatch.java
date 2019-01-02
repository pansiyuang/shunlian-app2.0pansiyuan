package com.shunlian.mylibrary;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 解决底部输入框和软键盘的问题
 * Created by geyifeng on 2017/5/17.
 */
public class KeyboardPatch {
    private Activity mActivity;
    private View mDecorView;
    private View mContentView;
    private boolean flag = false;
    private Window mWindow;
    private View mChildView;
    private ImmersionBar mImmersionBar;
    private final int mStatusBarHeight;
    private final int mActionBarHeight;
    private int mPaddingLeft = 0, mPaddingTop = 0, mPaddingRight = 0, mPaddingBottom = 0;

    private KeyboardPatch(Activity activity,ImmersionBar immersionBar) {
        this(activity, activity.findViewById(android.R.id.content),immersionBar);
    }

    private KeyboardPatch(Activity activity, View contentView,ImmersionBar immersionBar) {
        mImmersionBar = immersionBar;
        this.mActivity = activity;
        mWindow = activity.getWindow();
        this.mDecorView = mWindow.getDecorView();
        FrameLayout frameLayout = mDecorView.findViewById(android.R.id.content);
        mChildView = frameLayout.getChildAt(0);
        if (mChildView != null) {
            mPaddingLeft = mChildView.getPaddingLeft();
            mPaddingTop = mChildView.getPaddingTop();
            mPaddingRight = mChildView.getPaddingRight();
            mPaddingBottom = mChildView.getPaddingBottom();
        }
        this.mContentView = mChildView != null ? mChildView : frameLayout;
        BarConfig barConfig = new BarConfig(mActivity);
        mStatusBarHeight = barConfig.getStatusBarHeight();
        mActionBarHeight = barConfig.getActionBarHeight();
        if (contentView.equals(activity.findViewById(android.R.id.content))) {
            this.flag = false;
        } else {
            this.flag = true;
        }
    }

    /**
     * Patch keyboard patch.
     *
     * @param activity the activity
     * @return the keyboard patch
     */
    public static KeyboardPatch patch(Activity activity,ImmersionBar immersionBar) {
        return new KeyboardPatch(activity,immersionBar);
    }

    /**
     * Patch keyboard patch.
     *
     * @param activity    the activity
     * @param contentView 界面容器，指定布局的根节点
     * @return the keyboard patch
     */
    public static KeyboardPatch patch(Activity activity, View contentView) {
        return new KeyboardPatch(activity, contentView,null);
    }

    /**
     * 监听layout变化
     */
    public void enable() {
        enable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void enable(int mode) {
        mWindow.setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);//当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时，所要调用的回调函数的接口类
        }
    }

    /**
     * 取消监听
     */
    public void disable() {
        disable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void disable(int mode) {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }
    private int mTempKeyboardHeight;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (mDecorView == null || mContentView == null || mImmersionBar == null)return;
            int bottom = 0, keyboardHeight, navigationBarHeight = ImmersionBar.getNavigationBarHeight(mActivity);
            Rect r = new Rect();
            mDecorView.getWindowVisibleDisplayFrame(r); //获取当前窗口可视区域大小的
            keyboardHeight = mContentView.getHeight() - r.bottom;
            if(keyboardHeight != mTempKeyboardHeight){
                mTempKeyboardHeight = keyboardHeight;
                if (!ImmersionBar.checkFitsSystemWindows(mWindow.getDecorView().findViewById(android.R.id.content))) {
                    if (mChildView != null) {
                        if (mImmersionBar!=null) {
                            BarParams barParams = mImmersionBar.getBarParams();
                            if (barParams != null) {
                                if (barParams.isSupportActionBar) {
                                    keyboardHeight += mActionBarHeight + mStatusBarHeight;
                                }
                                if (barParams.fits) {
                                    keyboardHeight += mStatusBarHeight;
                                }
                            }
                        }
                        if (keyboardHeight > navigationBarHeight) {
                            bottom = keyboardHeight + mPaddingBottom;
                        }
                        mContentView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, bottom);
                    } else {
                        bottom = mContentView.getPaddingBottom();
                        keyboardHeight -= navigationBarHeight;
                        if (keyboardHeight > navigationBarHeight) {
                            bottom = keyboardHeight + navigationBarHeight;
                        }
                        mContentView.setPadding(mContentView.getPaddingLeft(),
                                mContentView.getPaddingTop(),
                                mContentView.getPaddingRight(),
                                bottom);
                    }
                }
            }
            /*int height = mDecorView.getContext().getResources().getDisplayMetrics().heightPixels; //获取屏幕密度，不包含导航栏
            int diff = height - r.bottom;
            if (diff > 0) {
                if (mContentView.getPaddingBottom() != diff) {
                    if (flag || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1())
                            || !ImmersionBar.with(mActivity).getBarParams().navigationBarEnable) {
                        mContentView.setPadding(0, 0, 0, diff);
                    } else {
                        mContentView.setPadding(0, 0, 0, diff + ImmersionBar.getNavigationBarHeight(mActivity));
                    }
                }
            } else {
                if (mContentView.getPaddingBottom() != 0) {
                    if (flag || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1())
                            || !ImmersionBar.with(mActivity).getBarParams().navigationBarEnable) {
                        mContentView.setPadding(0, 0, 0, 0);
                    } else {
                        mContentView.setPadding(0, 0, 0, ImmersionBar.getNavigationBarHeight(mActivity));
                    }
                }
            }*/
        }
    };
}
