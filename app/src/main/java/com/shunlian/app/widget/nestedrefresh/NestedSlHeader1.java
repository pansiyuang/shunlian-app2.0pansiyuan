package com.shunlian.app.widget.nestedrefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.nestedrefresh.base.BaseHeader;
import com.shunlian.app.widget.refresh.turkey.FirstSetpView;
import com.shunlian.app.widget.refresh.turkey.SecondStepView;
import com.shunlian.app.yjfk.ComplaintActivity;


/**
 * 火鸡下拉刷新头布局
 * Created by zhouweilong on 2016/10/24.
 */

public class NestedSlHeader1 extends BaseHeader {
    private static final int DONE = 0;
    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;
    private static final int REFRESH_THE_SECOND_FLOOR = 4;

    private AnimationDrawable secondAnimation;
    private int headerViewHeight;
    private SecondStepView secondStepView;
    private FirstSetpView firstSetpView;
    //    private  TextView tv_pull_to_refresh;
    private MyImageView miv_release;
    private View headerView;
    public boolean isgoSecond=false;
    private boolean isDone = false, isRelease = false;
//    private int i;
    private Context context1;
    public NestedSlHeader1(Context context) {
        super(context);
        context1=context;
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
        Log.d("onDrag: ", String.valueOf(y));
        Log.d("onDrag: ", String.valueOf(offset));
        if (y > 0 && !isRelease) {
            if (y > offset) {
                if (y > (2.5*offset)) {
                    changeHeaderByState(REFRESH_THE_SECOND_FLOOR);
                }else {
                    changeHeaderByState(RELEASE_TO_REFRESH);
                }
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
                Log.e("changeHeaderByState: ", "DONE,DONE,DONE,DONE");
                headerView.setPadding(0, -headerViewHeight, 0, 0);
//                firstSetpView.setVisibility(VISIBLE);
                secondAnimation.stop();
//                secondStepView.setVisibility(INVISIBLE);
//                miv_release.setVisibility(INVISIBLE);
                break;
            case RELEASE_TO_REFRESH:
                Log.e("changeHeaderByState: ", "RELEASE_TO_REFRESH,RELEASE_TO_REFRESH,RELEASE_TO_REFRESH,RELEASE_TO_REFRESH");
                firstSetpView.setVisibility(INVISIBLE);
                secondStepView.setVisibility(INVISIBLE);
                miv_release.setVisibility(VISIBLE);
                secondAnimation.stop();

                break;
            case PULL_TO_REFRESH:
                Log.e("changeHeaderByState: ", "PULL_TO_REFRESH,PULL_TO_REFRESH,PULL_TO_REFRESH,PULL_TO_REFRESH");
                firstSetpView.setVisibility(VISIBLE);
                secondAnimation.stop();
                secondStepView.setVisibility(INVISIBLE);
                miv_release.setVisibility(INVISIBLE);
                break;
            case REFRESHING:
                Log.e("changeHeaderByState: ", "REFRESHING,REFRESHING,REFRESHING,REFRESHING");
                firstSetpView.setVisibility(INVISIBLE);
                secondStepView.setVisibility(VISIBLE);
                miv_release.setVisibility(INVISIBLE);
                secondAnimation.stop();
                secondAnimation.start();
                break;
            case REFRESH_THE_SECOND_FLOOR:
                Log.e("changeHeaderByState: ", "REFRESH_THE_SECOND_FLOOR,REFRESH_THE_SECOND_FLOOR");
                isgoSecond=true;
                firstSetpView.setVisibility(INVISIBLE);
                secondStepView.setVisibility(INVISIBLE);
                miv_release.setVisibility(VISIBLE);
                ComplaintActivity.startAct(getContext());
            default:
              break;
                }
        }
//        i=0;
    }
