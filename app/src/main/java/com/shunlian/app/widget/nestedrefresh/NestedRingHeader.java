package com.shunlian.app.widget.nestedrefresh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.nestedrefresh.base.BaseHeader;
import com.shunlian.app.widget.refresh.ring.RingProgressBar;


/**
 * Created by zhouweilong on 2016/10/25.
 */

public class NestedRingHeader extends BaseHeader {

    private RotateAnimation refreshingAnimation;
    private TextView ring_refresh_status;
    private RingProgressBar progress_bar;
    private View headerView;
    private boolean isRelease=false;

    public NestedRingHeader(Context context) {
        super(context);
        headerView = LayoutInflater.from(context).inflate(R.layout.ring_refresh_header, this, true);
        progress_bar = (RingProgressBar) headerView.findViewById(R.id.progress_bar);
        ring_refresh_status = (TextView) headerView.findViewById(R.id.ring_refresh_status);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        progress_bar.setProgress(90);
    }


    @Override
    public void onRefresh() {
        ring_refresh_status.setText("正在刷新");
        progress_bar.startAnimation(refreshingAnimation);
    }

    @Override
    public void onDrag(int y, int offset) {
        if (y > 0&&!isRelease) {
            if (y >offset) {
                if(y>2.5*offset){
                    ring_refresh_status.setText("上二楼");
                }else{
                ring_refresh_status.setText("松开刷新");
                progress_bar.setIsShowIcon(false);
                }
            } else {
                int progress = (int) ((Math.abs(y) / (1.0f * offset)) * 100);
                ring_refresh_status.setText("下拉刷新");
                progress_bar.setProgress(progress > 90 ? 90 : progress);
                progress_bar.setIsShowIcon(true);
            }
        }
    }

    @Override
    public void onRelease() {
        isRelease=true;
    }

    @Override
    public void onComplete() {
        ring_refresh_status.setText("刷新完成");
        progress_bar.clearAnimation();
        progress_bar.setIsShowIcon(true);
        progress_bar.setProgress(100);
    }

    @Override
    public void onReset() {
        isRelease=false;
        ring_refresh_status.setText("下拉刷新");
        progress_bar.clearAnimation();
    }
}
