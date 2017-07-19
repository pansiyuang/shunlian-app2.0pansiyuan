package com.shunlian.app.ui;

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;

import com.shunlian.app.App;
import com.shunlian.app.utils.sideslip.SlideBackHelper;
import com.shunlian.app.utils.sideslip.SlideConfig;
import com.shunlian.app.utils.sideslip.callbak.OnSlideListenerAdapter;
import com.shunlian.app.utils.sideslip.widget.SlideBackLayout;

/**
 * Created by Administrator on 2017/7/19.
 */

public abstract class SideslipBaseActivity extends BaseActivity {
    private SlideBackLayout mSlideBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openSideslipCallback();
    }
    /**
     * 初始化侧滑返回
     */
    private void openSideslipCallback() {
        mSlideBackLayout = SlideBackHelper.attach(
                // 当前Activity
                this, App.getActivityHelper(),
                // 参数的配置
                new SlideConfig.Builder()
                        // 屏幕是否旋转
                        .rotateScreen(false)
                        // 是否侧滑
                        .edgeOnly(true)
                        // 是否禁止侧滑
                        .lock(false)
                        // 侧滑的响应阈值，0~1，对应屏幕宽度*percent
                        .edgePercent(0.1f)
                        // 关闭页面的阈值，0~1，对应屏幕宽度*percent
                        .slideOutPercent(0.3f).create(),
                // 滑动的监听
                new OnSlideListenerAdapter() {
                    @Override
                    public void onSlide(@FloatRange(from = 0.0,
                            to = 1.0) float percent) {
                        super.onSlide(percent);
                    }
                });
    }

    /**
     * 打开侧滑
     */
    protected void openSideslip(){
        if (mSlideBackLayout != null){
            mSlideBackLayout.lock(true);
        }
    }

    /**
     * 关闭侧滑
     */
    protected void closeSideslip(){
        if (mSlideBackLayout != null){
            mSlideBackLayout.lock(false);
        }
    }
}
