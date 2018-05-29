package com.shunlian.app.widget.nestedrefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.nestedrefresh.base.BaseHeader;
import com.shunlian.app.widget.refresh.turkey.FirstSetpView;
import com.shunlian.app.widget.refresh.turkey.SecondStepView;


/**
 * 火鸡下拉刷新头布局
 * Created by zhouweilong on 2016/10/24.
 */

public class NestedSlHeader extends BaseHeader {
    private static final int DONE = 0;
    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;

    private AnimationDrawable secondAnimation;
    private int headerViewHeight;
    private SecondStepView secondStepView;
    private FirstSetpView firstSetpView;
    //    private  TextView tv_pull_to_refresh;
    private MyImageView miv_release;
    private View headerView;
    private boolean isDone = false, isRelease = false;

    public NestedSlHeader(Context context) {
        super(context);
        headerView = LayoutInflater.from(context).inflate(R.layout.sl_refresh_view, this, true);
        miv_release = (MyImageView) headerView.findViewById(R.id.miv_release);
        firstSetpView = (FirstSetpView) headerView.findViewById(R.id.first_step_view);
        secondStepView = (SecondStepView) headerView.findViewById(R.id.second_step_view);
        secondStepView.setBackgroundResource(R.drawable.turkey_animation);
        secondAnimation = (AnimationDrawable) secondStepView.getBackground();
        measureView(headerView);
        changeHeaderByState(DONE);
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public void onRefresh() {
        changeHeaderByState(REFRESHING);
//        ivSuccess.setVisibility(GONE);
//        ivArrow.clearAnimation();
//        ivArrow.setVisibility(GONE);
//        progressBar.setVisibility(VISIBLE);
//        tvRefresh.setText("REFRESHING");
    }

    @Override
    public void onDrag(int y, int offset) {
        if (y > 0 && !isRelease) {
            if (y > offset) {
                changeHeaderByState(RELEASE_TO_REFRESH);
            } else {
                firstSetpView.setCurrentProgress(Math.abs(y) / (1.0f * getHeight()));
                firstSetpView.postInvalidate();
                changeHeaderByState(PULL_TO_REFRESH);
            }
        }
//        if (y > 0) {
////            ivArrow.setVisibility(VISIBLE);
////            progressBar.setVisibility(GONE);
////            ivSuccess.setVisibility(GONE);
//            if (y > offset) {
////                tvRefresh.setText("RELEASE TO REFRESH");
////                if (!rotated) {
////                    ivArrow.clearAnimation();
////                    ivArrow.startAnimation(rotateUp);
////                    rotated = true;
////                }
//                changeHeaderByState(RELEASE_TO_REFRESH);
//            } else {
////                if (rotated) {
////                    ivArrow.clearAnimation();
////                    ivArrow.startAnimation(rotateDown);
////                    rotated = false;
////                }
////
////                tvRefresh.setText("SWIPE TO REFRESH");
//                firstSetpView.setCurrentProgress(Math.abs(y) / (1.0f * getHeight()));
//                firstSetpView.postInvalidate();
//                if (!isDone)
//                changeHeaderByState(PULL_TO_REFRESH);
//            }
//        }else {
//            isDone=false;
//        }


    }

    @Override
    public void onRelease() {
        isRelease = true;
    }

    @Override
    public void onComplete() {
        changeHeaderByState(DONE);
//        rotated = false;
//        ivSuccess.setVisibility(VISIBLE);
//        ivArrow.clearAnimation();
//        ivArrow.setVisibility(GONE);
//        progressBar.setVisibility(GONE);
//        tvRefresh.setText("COMPLETE");
    }

    @Override
    public void onReset() {
        isRelease = false;
//        rotated = false;
//        ivSuccess.setVisibility(GONE);
//        ivArrow.clearAnimation();
//        ivArrow.setVisibility(GONE);
//        progressBar.setVisibility(GONE);
        changeHeaderByState(DONE);
    }


    private void changeHeaderByState(int state) {
        switch (state) {
            case DONE:
                headerView.setPadding(0, -headerViewHeight, 0, 0);
//                firstSetpView.setVisibility(View.VISIBLE);
                secondAnimation.stop();
//                secondStepView.setVisibility(View.GONE);
//                miv_release.setVisibility(GONE);
                break;
            case RELEASE_TO_REFRESH:
                firstSetpView.setVisibility(View.GONE);
                secondStepView.setVisibility(View.GONE);
                miv_release.setVisibility(VISIBLE);
                secondAnimation.stop();
                break;
            case PULL_TO_REFRESH:
                firstSetpView.setVisibility(View.VISIBLE);
                secondAnimation.stop();
                secondStepView.setVisibility(View.GONE);
                miv_release.setVisibility(GONE);
                break;
            case REFRESHING:
                firstSetpView.setVisibility(View.GONE);
                secondStepView.setVisibility(View.VISIBLE);
                miv_release.setVisibility(GONE);
                secondAnimation.stop();
                secondAnimation.start();
                break;
            default:
                break;
        }
    }
}
