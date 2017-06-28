package com.shunlian.app.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.listener.IRefreshListener;
import com.shunlian.app.utils.TransformUtil;


//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

/**
 * Created by zhang on 2017/6/24 14 : 54.
 */

public class RefreshLinearLayout extends LinearLayout {

    private float mInitialDownY = -1;
    private boolean mIsBeingDragged;


    private int refreshHeadHeight;
    private int currentRefreshHeadHeight;
    private int currentPosition;
    private CustomAnimation customAnimation;
    private IRefreshListener listener;
    /*
     *滑动速率
     */
    private final float SLIP_RATE = 2.0f;

    /*
     *刷分为三个状态
       * 下拉刷新
       * 松手刷新
       * 刷新中
       *
     */
    /*
    下拉刷新
     */
    private final int PULL_REFRESH_STATE = 0;
    /*
    松手刷新
     */
    private final int UP_REFRESH_STATE = 1;
    /*
    刷新中
     */
    private final int REFRESHING_STATE = 2;
    /*
    当前刷新状态
     */
    private int currentRefreshState = PULL_REFRESH_STATE;
    private final int INVALID_POINTER = -1;
    private int firstItemPosition;
    private View mTarget;
    private int mActivePointerId = INVALID_POINTER;
    private int mTouchSlop;
    private float mInitialMotionY;

    public RefreshLinearLayout(Context context) {
        super(context);
        init();
    }


    public RefreshLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        refreshHeadHeight = TransformUtil.dip2px(getContext(), 50);
        currentPosition = -refreshHeadHeight;
        currentRefreshHeadHeight = -refreshHeadHeight;
        setPadding(0, -refreshHeadHeight, 0, 0);
    }

    public void setRefreshHeader(CustomAnimation doubleCircleView) {
        if (doubleCircleView == null) {
            throw new IllegalArgumentException("DoubleCircleView can not be empty");
        }
        this.customAnimation = doubleCircleView;
        View childAt = getChildAt(0);
        if (childAt == doubleCircleView) {
            removeView(childAt);
        }
        addView(doubleCircleView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, refreshHeadHeight));

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();
        int actionMasked = MotionEventCompat.getActionMasked(ev);
        int pointerIndex;

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(RefreshLinearLayout.class.getName(), "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                startDragging(y);
                float moveY = mInitialDownY - y;
                if (moveY > 0) {//手指向上滑动
                    return false;
                } else {//手指向下滑动
                    if (mTarget instanceof RecyclerView) {
                        return dispatchRecycler(mTarget);
                    } else {
                        mIsBeingDragged = false;
                    }
                }
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mInitialDownY = -1;
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        int pointerIndex = -1;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                startDragging(y);

                if (mIsBeingDragged) {
                    float moveY = (mInitialMotionY - y) / SLIP_RATE;
                    if (moveY < 0) {//手指向下滑动
                        if (currentRefreshState != REFRESHING_STATE) {
                            currentRefreshHeadHeight = (int) (currentPosition - moveY + 0.5f);

                            if (currentRefreshHeadHeight >= 0) {
                                currentRefreshState = UP_REFRESH_STATE;
                            } else {
                                currentRefreshState = PULL_REFRESH_STATE;
                            }
                            setRefreshViewHeight(currentRefreshHeadHeight);
                        }

                    } else {
                        return false;
                    }
                    if (currentRefreshHeadHeight <= 0) {
                        customAnimation.pullDownHeight(refreshHeadHeight + currentRefreshHeadHeight);
                    }
                }
                break;

            case MotionEventCompat.ACTION_POINTER_DOWN:
                pointerIndex = MotionEventCompat.getActionIndex(ev);
                if (pointerIndex < 0) {
                    return false;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                if (mIsBeingDragged) {
                    if (currentRefreshState == UP_REFRESH_STATE) {
                        showRefreshView();
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onRefreshComplete();
                            }
                        }, 3000);
                    } else if (currentRefreshState == PULL_REFRESH_STATE) {
                        hideRefreshView();
                    }
                }

                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEvent.ACTION_CANCEL:
                return false;

        }

        return true;
    }

    /**
     * 设置刷新监听
     *
     * @param listener
     */
    public void setRefreshListener(IRefreshListener listener) {

        this.listener = listener;
    }

    /**
     * 设置刷新view的高度
     *
     * @param height
     */
    private void setRefreshViewHeight(int height) {
        if (height < -refreshHeadHeight) {
            height = -refreshHeadHeight;
        }
        setPadding(0, height, 0, 0);
    }


    private void hideRefreshView() {
        currentRefreshHeadHeight = -refreshHeadHeight;
        currentPosition = currentRefreshHeadHeight;
        setRefreshViewHeight(currentRefreshHeadHeight);
    }

    private void showRefreshView() {
        currentRefreshHeadHeight = 0;
        currentRefreshState = REFRESHING_STATE;
        customAnimation.startAnimation();
        currentPosition = currentRefreshHeadHeight;
        setRefreshViewHeight(currentRefreshHeadHeight);
        if (listener != null) {
            listener.onComplete();
        }
    }


    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(customAnimation)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    private void startDragging(float y) {
        final float yDiff = y - mInitialDownY;
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mIsBeingDragged = true;
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private boolean dispatchRecycler(View childAt) {
        RecyclerView recyclerView = (RecyclerView) childAt;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) layoutManager;
            firstItemPosition = mLayoutManager.findFirstVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager mLayoutManager = (GridLayoutManager) layoutManager;
            firstItemPosition = mLayoutManager.findFirstVisibleItemPosition();
        }
        if (firstItemPosition == 0) {
            return true;
        }

        return false;
    }

    /**
     * 完成刷新
     */
    public void onRefreshComplete() {
        currentRefreshState = PULL_REFRESH_STATE;
        customAnimation.stopAnimation();
        hideRefreshView();
    }

    /**
     * true 显示刷新中，否则隐藏刷新
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing){
        if (refreshing){
            showRefreshView();
        }else {
            onRefreshComplete();
        }
    }
}
