package com.shunlian.app.utils.player;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by zhanghe on 2018/10/26.
 */

public class SmallMediaPlayer extends BaseActivity {

    @BindView(R.id.sv_player)
    SmallVideoPlayerV2 svPlayer;


    public static void startAct(Context context,String url){
        context.startActivity(new Intent(context,SmallMediaPlayer.class)
                .putExtra("url",url));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.small_media_player;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        String url = getIntent().getStringExtra("url");
        if (isEmpty(url)){
            Common.staticToast("url错误");
            svPlayer.postDelayed(()->finish(),500);
            return;
        }
        svPlayer.setUp(url, JZVideoPlayer.SCREEN_WINDOW_LIST);
        svPlayer.startVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SmallVideoPlayerV2.goOnPlayOnPause();
    }

    @Override
    protected void onDestroy() {
        if (svPlayer != null){
            svPlayer.onAutoCompletion();
            svPlayer.release();
        }
        super.onDestroy();
    }
}
