package com.shunlian.app.utils.player;

import android.content.Context;
import android.util.AttributeSet;

import com.shunlian.app.widget.SmallVideoPlayer;

/**
 * Created by zhanghe on 2018/10/26.
 */

public class SmallVideoPlayerV2 extends SmallVideoPlayer {


    public SmallVideoPlayerV2(Context context) {
        super(context);
    }

    public SmallVideoPlayerV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        iv_download.setVisibility(GONE);
        closeVolumeControl.setVisibility(GONE);
        closeVideo.setVisibility(GONE);
        voiceControl.setVisibility(GONE);
    }
}
