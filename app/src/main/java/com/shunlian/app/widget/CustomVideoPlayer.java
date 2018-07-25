package com.shunlian.app.widget;

/**
 * Created by Administrator on 2018/7/23.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.shunlian.app.eventbus_bean.VideoPlayEvent;
import com.shunlian.app.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import cn.jzvd.JZVideoPlayerStandard;

public class CustomVideoPlayer extends JZVideoPlayerStandard {

    public CustomVideoPlayer(Context context) {
        super(context);
    }

    public CustomVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case cn.jzvd.R.id.fullscreen:
                if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                    //click quit fullscreen
                } else {
                    //click goto fullscreen
                }
                break;
            case cn.jzvd.R.id.back:
                EventBus.getDefault().post(new VideoPlayEvent(VideoPlayEvent.FinishAction));
                break;
            case cn.jzvd.R.id.iv_more:
                EventBus.getDefault().post(new VideoPlayEvent(VideoPlayEvent.MoreAction));
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return cn.jzvd.R.layout.jz_layout_standard;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();
        startWindowFullscreen();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        EventBus.getDefault().post(new VideoPlayEvent(VideoPlayEvent.CompleteAction));
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
    }

    @Override
    public void startWindowTiny() {
        super.startWindowTiny();
    }
}
