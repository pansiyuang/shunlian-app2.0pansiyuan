package com.shunlian.app.widget;

/**
 * Created by Administrator on 2018/7/23.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.DiscoverGoodsAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.download.DownLoadDialogProgress;
import com.shunlian.app.utils.download.DownloadUtils;
import com.shunlian.app.utils.download.JsDownloadListener;
import com.shunlian.app.widget.circle.CircleImageView;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZUserActionStandard;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;

public class GoodVideoPlayer extends JZVideoPlayer  {
    protected updateParseAttent parseAttent;
    protected static Timer DISMISS_CONTROL_VIEW_TIMER;

    public ImageView backButton;
    public ProgressBar bottomProgressBar, loadingProgressBar;
    public ImageView thumbImageView;
    public ImageView tinyBackImageView;
    public TextView videoCurrentTime;
    public TextView replayTextView;
    public PopupWindow clarityPopWindow;
    public TextView mRetryBtn;
    public LinearLayout mRetryLayout;
    private ImageView playControl;

    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected Dialog mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogIcon;
    protected Dialog mVolumeDialog;
    protected ProgressBar mDialogVolumeProgressBar;
    protected TextView mDialogVolumeTextView;
    protected ImageView mDialogVolumeImageView;
    protected Dialog mBrightnessDialog;
    protected ProgressBar mDialogBrightnessProgressBar;
    protected TextView mDialogBrightnessTextView;

    private LinearLayout line_good_info;
    private RelativeLayout relt_bottom_user;
    private View include_good;

    /**
     * 商品相关的信息，分享，点赞，下载，商品内容，用户
     * @param context
     */
    private LinearLayout line_dianzan,line_down,line_share;
    private TextView tv_dianzan,tv_down,tv_share;
    private ImageView image_dianzai_state;
    private CircleImageView image_user_head;
    private TextView tv_user_name;
    private TextView tv_user_attent;
    private ImageView miv_share;
    private ImageView img_goods_icon;
    private TextView tv_goods_name,tv_goods_price,tv_old_price;

    private DownLoadDialogProgress downLoadDialogProgress;
    private DownloadUtils downloadUtils;
    private BigImgEntity.Blog blog;
    public GoodVideoPlayer(Context context) {
        super(context);
        //LogUtil.zhLogW("=====SmallVideoPlayer==1========");
    }

    public GoodVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        //LogUtil.zhLogW("=====SmallVideoPlayer==2========");
    }

    @Override
    public void init(Context context) {
        super.init(context);
        miv_share= findViewById(cn.jzvd.R.id.miv_share);
        line_dianzan = findViewById(cn.jzvd.R.id.line_dianzan);
        line_down = findViewById(cn.jzvd.R.id.line_down);
        line_share = findViewById(cn.jzvd.R.id.line_share);
        tv_dianzan= findViewById(cn.jzvd.R.id.tv_dianzan);
        tv_down= findViewById(cn.jzvd.R.id.tv_down);
        tv_share= findViewById(cn.jzvd.R.id.tv_share);
        image_dianzai_state=findViewById(cn.jzvd.R.id.image_dianzai_state);
        image_user_head= findViewById(cn.jzvd.R.id.image_user_head);
        tv_user_name= findViewById(cn.jzvd.R.id.tv_user_name);
        tv_user_attent= findViewById(cn.jzvd.R.id.tv_user_attent);
        img_goods_icon= findViewById(cn.jzvd.R.id.img_goods_icon);
        tv_goods_name= findViewById(cn.jzvd.R.id.tv_goods_name);
        tv_goods_price= findViewById(cn.jzvd.R.id.tv_goods_price);
        tv_old_price= findViewById(cn.jzvd.R.id.tv_old_price);
        tv_user_attent.setOnClickListener(this);
        line_dianzan.setOnClickListener(this);
        line_down.setOnClickListener(this);
        line_share.setOnClickListener(this);
        miv_share.setOnClickListener(this);
        image_user_head.setOnClickListener(this);
        bottomProgressBar = findViewById(cn.jzvd.R.id.bottom_progress);
        backButton = findViewById(cn.jzvd.R.id.back);
        thumbImageView = findViewById(cn.jzvd.R.id.thumb);
        loadingProgressBar = findViewById(cn.jzvd.R.id.loading);
        tinyBackImageView = findViewById(cn.jzvd.R.id.back_tiny);
        videoCurrentTime = findViewById(cn.jzvd.R.id.video_current_time);
        replayTextView = findViewById(cn.jzvd.R.id.replay_text);
        findViewById(cn.jzvd.R.id.clarity).setVisibility(GONE);
        mRetryBtn = findViewById(cn.jzvd.R.id.retry_btn);
        mRetryLayout = findViewById(cn.jzvd.R.id.retry_layout);
        topContainer.setVisibility(VISIBLE);
        fullscreenButton.setVisibility(GONE);
        playControl = findViewById(R.id.iv_play_control);
        playControl.setVisibility(VISIBLE);
        line_good_info = findViewById(R.id.line_good_info);
        relt_bottom_user= findViewById(R.id.relt_bottom_user);
        include_good= findViewById(R.id.include_good);
        findViewById(R.id.iv_more).setVisibility(GONE);
        ImageView iv_download = findViewById(R.id.iv_download);
        iv_download.setVisibility(GONE);

        include_good.setOnClickListener(this);
        thumbImageView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        tinyBackImageView.setOnClickListener(this);
        mRetryBtn.setOnClickListener(this);
        playControl.setOnClickListener(this);
        startButton.setOnClickListener(this);
        backPlayButton.setOnClickListener(this);
        iv_download.setOnClickListener(this);
        downLoadDialogProgress = new DownLoadDialogProgress();
    }

    /**
     * 设置商品用户信息信息
     */
    public void setGoodUserInfo(BigImgEntity.Blog blog, updateParseAttent parseAttent){
           this.blog = blog;
           this.parseAttent = parseAttent;
             setParseStateView();
            tv_share.setText(blog.share_num+"");
            tv_down.setText(blog.down_num+"");
            tv_user_name.setText(blog.nickname+"");
             GlideUtils.getInstance().loadBitmapSync(getContext(), blog.avatar,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        image_user_head.setImageBitmap(resource);
                    }
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        image_user_head.setImageResource(R.mipmap.img_set_defaulthead);
                    }
                });
                 setAttentStateView();
           if(blog.related_goods!=null&&blog.related_goods.size()>0) {
               GlideUtils.getInstance().loadCornerImage(getContext(), img_goods_icon, blog.related_goods.get(0).thumb, 4);
               if(blog.related_goods.get(0).title!=null)
               tv_goods_name.setText(blog.related_goods.get(0).title);
               if(blog.related_goods.get(0).price!=null)
               tv_goods_price.setText("¥"+blog.related_goods.get(0).price);
               if(blog.related_goods.get(0).market_price!=null) {
                   tv_old_price.setText("¥" + blog.related_goods.get(0).market_price);
                   tv_old_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
               }else{
                   tv_old_price.setText("");
               }
           }
    }

    /**
     * 设置点赞显示
     */
    public void setParseStateView(){
        if(blog.is_praise==1) {//已点赞
            image_dianzai_state.setImageResource(R.mipmap.icon_dianzan_sel);
            tv_dianzan.setTextColor(getResources().getColor(R.color.pink_color));
        }else{
            image_dianzai_state.setImageResource(R.mipmap.icon_dianzan_good);
            tv_dianzan.setTextColor(getResources().getColor(R.color.white));
        }
        tv_dianzan.setText(blog.praise_num+"");
    }

    /**
     * 设置关注显示
     */
    public void setAttentStateView(){
        if(blog.is_focus==0) {
            tv_user_attent.setText(blog.is_focus == 0 ? "关注" : "已关注");
            tv_user_attent.setVisibility(VISIBLE);
        }else{
            tv_user_attent.setText(blog.is_focus == 0 ? "关注" : "已关注");
            tv_user_attent.setVisibility(VISIBLE);
        }
        if(blog.is_self==1){
            tv_user_attent.setVisibility(GONE);
        }

        tv_user_attent.setTextColor(blog.is_focus==0?getResources().getColor(R.color.deep_red):getResources().getColor(R.color.value_878B8A));
        tv_user_attent.setBackgroundResource(blog.is_focus==0?R.drawable.rounded_rectangle_stroke_22px:R.color.transparent);
    }

    /**
     * 设置下载成功回调
     */
    public void setDownLoadSuccess(){
        tv_down.setText(blog.down_num+"");
    }

    @Override
    public void startVideo() {
        super.startVideo();
        if (mAudioManager != null) {
            int initialVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            SharedPrefUtil.saveCacheSharedPrfLong("video_volume",initialVolume);
        }
        //LogUtil.zhLogW("=====startVideo==========");
        Object[] dataSource = JZMediaManager.getDataSource();
        if (dataSource != null && dataSource.length > 0){
            Object data = dataSource[0];
            if (data instanceof LinkedHashMap){
                LinkedHashMap map = (LinkedHashMap) data;
                if (map.get(URL_KEY_DEFAULT) instanceof String)
                currentUrl = (String) map.get(URL_KEY_DEFAULT);
            }
        }
    }

    public static void goOnPlayOnResume() {
        if (JZVideoPlayerManager.getCurrentJzvd() != null) {
            JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState == JZVideoPlayer.CURRENT_STATE_PAUSE) {
                jzvd.onStatePlaying();
                JZMediaManager.start();
            }
        }
    }

    public static void goOnPlayOnPause() {
        try {
            if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
                if (jzvd.currentState == JZVideoPlayer.CURRENT_STATE_AUTO_COMPLETE ||
                        jzvd.currentState == JZVideoPlayer.CURRENT_STATE_NORMAL ||
                        jzvd.currentState == JZVideoPlayer.CURRENT_STATE_ERROR) {
                    //releaseAllVideos();
                } else {
                    jzvd.onStatePause();
                    JZMediaManager.pause();
                }
            }
        }catch (Exception e){

        }
    }

    public static void onChildViewAttachedToWindow(View view, int jzvdId) {
        if (JZVideoPlayerManager.getCurrentJzvd() != null &&
                JZVideoPlayerManager.getCurrentJzvd().currentScreen == JZVideoPlayer.SCREEN_WINDOW_TINY) {
            GoodVideoPlayer videoPlayer = view.findViewById(jzvdId);
            if (videoPlayer != null && JZUtils.getCurrentFromDataSource(
                    videoPlayer.dataSourceObjects,
                    videoPlayer.currentUrlMapIndex).equals
                    (JZMediaManager.getCurrentDataSource())) {
                GoodVideoPlayer.backPress();
            }
        }
    }

    public static void onChildViewDetachedFromWindow(View view) {
        if (JZVideoPlayerManager.getCurrentJzvd() != null &&
                JZVideoPlayerManager.getCurrentJzvd().currentScreen !=
                        JZVideoPlayer.SCREEN_WINDOW_TINY) {
            GoodVideoPlayer
                    videoPlayer = (GoodVideoPlayer) JZVideoPlayerManager.getCurrentJzvd();
            if (view instanceof ViewGroup && ((ViewGroup) view).indexOfChild(videoPlayer) != -1) {
                if (videoPlayer.currentState == JZVideoPlayer.CURRENT_STATE_PAUSE) {
                    JZVideoPlayer.releaseAllVideos();
                } else {
                    videoPlayer.startWindowTiny();
                }
            }
        }
    }

    public void setUp(Object[] dataSourceObjects, int defaultUrlMapIndex, int screen, Object... objects) {
        super.setUp(dataSourceObjects, defaultUrlMapIndex, screen, objects);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            fullscreenButton.setImageResource(cn.jzvd.R.drawable.jz_shrink);
            backButton.setVisibility(View.VISIBLE);
            tinyBackImageView.setVisibility(View.INVISIBLE);
            changeStartButtonSize((int) getResources().getDimension(cn.jzvd.R.dimen.jz_start_button_w_h_fullscreen));
        } else if (currentScreen == SCREEN_WINDOW_NORMAL
                || currentScreen == SCREEN_WINDOW_LIST) {
            fullscreenButton.setImageResource(cn.jzvd.R.drawable.jz_enlarge);
            backButton.setVisibility(View.VISIBLE);
            tinyBackImageView.setVisibility(View.INVISIBLE);
            changeStartButtonSize((int) getResources().getDimension(cn.jzvd.R.dimen.jz_start_button_w_h_normal));
        } else if (currentScreen == SCREEN_WINDOW_TINY) {
            tinyBackImageView.setVisibility(View.VISIBLE);
            setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                    View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
        }

        if (tmp_test_back) {
            tmp_test_back = false;
            JZVideoPlayerManager.setFirstFloor(this);
            backPress();
        }
        //LogUtil.zhLogW("=====setUp==========");
    }

    public void changeStartButtonSize(int size) {
        ViewGroup.LayoutParams lp = startButton.getLayoutParams();
        lp.height = size;
        lp.width = size;
        lp = loadingProgressBar.getLayoutParams();
        lp.height = size;
        lp.width = size;
    }

    @Override
    public int getLayoutId() {
        return cn.jzvd.R.layout.jz_layout_standard;
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
        //LogUtil.zhLogW("=====onStateNormal==========");
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
        //LogUtil.zhLogW("=====onStatePreparing==========");
    }

    @Override
    public void onStatePreparingChangingUrl(int urlMapIndex, long seekToInAdvance) {
        super.onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);
        loadingProgressBar.setVisibility(VISIBLE);
        startButton.setVisibility(INVISIBLE);
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingClear();
        //LogUtil.zhLogW("=====onStatePlaying==========");
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
        //LogUtil.zhLogW("=====onStatePause==========");
    }

    @Override
    public void onStateError() {
        super.onStateError();
        changeUiToError();
        //LogUtil.zhLogW("=====onStateError==========");
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        changeUiToComplete();
        cancelDismissControlViewTimer();
        bottomProgressBar.setProgress(100);
        //LogUtil.zhLogW("=====onStateAutoComplete==========");
    }


    public void startWindowFullscreen() {
        //LogUtil.zhLogW("=====startWindowFullscreen==========");
        //Log.i(TAG, "startWindowFullscreen " + " [" + this.hashCode() + "] ");
        hideSupportActionBar(getContext());

        ViewGroup vp = (JZUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(cn.jzvd.R.id.jz_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(JZMediaManager.textureView);
        try {
            Constructor<GoodVideoPlayer> constructor = (Constructor<GoodVideoPlayer>) getClass().getConstructor(Context.class);
            GoodVideoPlayer jzVideoPlayer = constructor.newInstance(getContext());
            jzVideoPlayer.setId(cn.jzvd.R.id.jz_fullscreen_id);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(jzVideoPlayer, lp);
            jzVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
            jzVideoPlayer.setUp(dataSourceObjects, currentUrlMapIndex, jzVideoPlayer.SCREEN_WINDOW_FULLSCREEN, objects);
            jzVideoPlayer.setState(currentState);
            jzVideoPlayer.addTextureView();
            JZVideoPlayerManager.setSecondFloor(jzVideoPlayer);
            //JZUtils.setRequestedOrientation(getContext(), FULLSCREEN_ORIENTATION);

            onStateNormal();
            jzVideoPlayer.progressBar.setSecondaryProgress(progressBar.getSecondaryProgress());
            jzVideoPlayer.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startWindowTiny() {
        //Log.i(TAG, "startWindowTiny " + " [" + this.hashCode() + "] ");
        onEvent(JZUserAction.ON_ENTER_TINYSCREEN);
        if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR || currentState == CURRENT_STATE_AUTO_COMPLETE)
            return;
        ViewGroup vp = (JZUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(cn.jzvd.R.id.jz_tiny_id);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(JZMediaManager.textureView);

        try {
            Constructor<GoodVideoPlayer> constructor = (Constructor<GoodVideoPlayer>) getClass().getConstructor(Context.class);
            GoodVideoPlayer jzVideoPlayer = constructor.newInstance(getContext());
            jzVideoPlayer.setId(cn.jzvd.R.id.jz_tiny_id);
            int w = TransformUtil.dip2px(getContext(), 125);
            int h = TransformUtil.dip2px(getContext(), 70);
            LayoutParams lp = new LayoutParams(w, h);
            lp.gravity = Gravity.RIGHT | Gravity.TOP;
            int statusBarHeight = ImmersionBar.getStatusBarHeight((Activity) getContext());
            int topmargin = TransformUtil.dip2px(getContext(), 54);
            int rightmargin = TransformUtil.dip2px(getContext(), 10);
            lp.topMargin = topmargin+statusBarHeight;
            lp.rightMargin = rightmargin;
            vp.addView(jzVideoPlayer, lp);
            jzVideoPlayer.setUp(dataSourceObjects, currentUrlMapIndex, GoodVideoPlayer.SCREEN_WINDOW_TINY, objects);
            jzVideoPlayer.setState(currentState);
            jzVideoPlayer.addTextureView();
            JZVideoPlayerManager.setSecondFloor(jzVideoPlayer);
            onStateNormal();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == cn.jzvd.R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    if (mChangePosition) {
                        long duration = getDuration();
                        int progress = (int) (mSeekTimePosition * 100 / (duration == 0 ? 1 : duration));
                        bottomProgressBar.setProgress(progress);
                    }
                    if (!mChangePosition && !mChangeVolume) {
                        onEvent(JZUserActionStandard.ON_CLICK_BLANK);
                        onClickUiToggle();
                    }
                    break;
            }
        } else if (id == cn.jzvd.R.id.bottom_seek_progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    break;
            }
        }
        return super.onTouch(v, event);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == cn.jzvd.R.id.thumb) {
            if (dataSourceObjects == null || JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex) == null) {
                Toast.makeText(getContext(), getResources().getString(cn.jzvd.R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentState == CURRENT_STATE_NORMAL) {
                /*if (!JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("file") &&
                        !JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("/") &&
                        !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }*/
                startVideo();
                onEvent(JZUserActionStandard.ON_CLICK_START_THUMB);//开始的事件应该在播放之后，此处特殊
            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onClickUiToggle();
            }
        } else if (i == cn.jzvd.R.id.surface_container) {
            startDismissControlViewTimer();
        } else if (i == cn.jzvd.R.id.back) {
            topContainer.setVisibility(VISIBLE);
            backPress(); //暂时去掉该功能
            parseAttent.destoryVideo();
        } else if (i == cn.jzvd.R.id.back_tiny) {
            backPress();
            goOnPlayOnPause();
        } else if (i == cn.jzvd.R.id.retry_btn) {
            if (dataSourceObjects == null || JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex) == null) {
                Toast.makeText(getContext(), getResources().getString(cn.jzvd.R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            /*if (!JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("file") && !
                    JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("/") &&
                    !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                showWifiDialog();
                return;
            }*/
            initTextureView();//和开始播放的代码重复
            addTextureView();
            JZMediaManager.setDataSource(dataSourceObjects);
            JZMediaManager.setCurrentDataSource(JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
            onStatePreparing();
            onEvent(JZUserAction.ON_CLICK_START_ERROR);
        }else if (i == R.id.iv_play_control || i == R.id.start){
            playerControl();
        }else if (i == cn.jzvd.R.id.fullscreen) {
            //Log.i(TAG, "onClick fullscreen [" + this.hashCode() + "] ");
            if (currentState == CURRENT_STATE_AUTO_COMPLETE) return;
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //quit fullscreen
                backPress();
            } else {
                //Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                onEvent(JZUserAction.ON_ENTER_FULLSCREEN);
                startWindowFullscreen();
            }
        }else if (i == cn.jzvd.R.id.iv_close){
            onAutoCompletion();
        }else if (i == cn.jzvd.R.id.iv_voice){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
        }else if (i == cn.jzvd.R.id.iv_download){
            downFileStart();
        }else if(i==R.id.line_dianzan){
            if(blog.is_praise==0) {
                parseAttent.updateParse(blog.is_praise == 1);
            }
        }else if(i==R.id.line_share){
        }else if(i==R.id.line_down){
            downFileStart();
        }else if(i==R.id.tv_user_attent){
            parseAttent.updateAttent(blog.is_focus == 1, blog.nickname);
        }else if(i==R.id.include_good){
            if (blog.related_goods.size() == 1) {
                parseAttent.startGoodInfo();
            } else {
                parseAttent.startListGoodInfo();
            }
        }else if(i==R.id.miv_share){
            parseAttent.shareBolg();
        }else if(i==R.id.image_user_head){
            MyPageActivity.startAct(getContext(), blog.member_id);
        }
    }


    @Override
    public void showWifiDialog() {
       /* super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(cn.jzvd.R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(cn.jzvd.R.string.tips_not_wifi_confirm), (dialog, which) -> {
            dialog.dismiss();
            onEvent(JZUserActionStandard.ON_CLICK_START_WIFIDIALOG);
            startVideo();
            WIFI_TIP_DIALOG_SHOWED = true;
        });
        builder.setNegativeButton(getResources().getString(cn.jzvd.R.string.tips_not_wifi_cancel), (dialog, which) -> {
            dialog.dismiss();
            clearFloatScreen();
        });
        builder.setOnCancelListener(dialog -> dialog.dismiss());
        builder.create().show();*/
    }

    @Override
    protected void playerControl() {
        //Log.i(TAG, "onClick start [" + this.hashCode() + "] ");
        if (dataSourceObjects == null || JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex) == null) {
            Toast.makeText(getContext(), getResources().getString(cn.jzvd.R.string.no_url), Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentState == CURRENT_STATE_NORMAL) {
            /*if (!JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("file") && !
                    JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("/") &&
                    !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                showWifiDialog();
                return;
            }*/
            startVideo();
            onEvent(JZUserAction.ON_CLICK_START_ICON);//开始的事件应该在播放之后，此处特殊
        } else if (currentState == CURRENT_STATE_PLAYING) {
            onEvent(JZUserAction.ON_CLICK_PAUSE);
            //Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
            JZMediaManager.pause();
            onStatePause();
        } else if (currentState == CURRENT_STATE_PAUSE) {
            onEvent(JZUserAction.ON_CLICK_RESUME);
            JZMediaManager.start();
            onStatePlaying();
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            onEvent(JZUserAction.ON_CLICK_START_AUTO_COMPLETE);
            startVideo();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        if (currentState == CURRENT_STATE_PLAYING) {
            dissmissControlView();
        } else {
            startDismissControlViewTimer();
        }
    }

    public void onClickUiToggle() {
        if (currentState == CURRENT_STATE_PREPARING) {
            changeUiToPreparing();
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        }
    }

    public void onCLickUiToggleToClear() {
        if (currentState == CURRENT_STATE_PREPARING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPreparing();
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            }
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToComplete();
            }
        }
    }

    @Override
    public void setProgressAndText(int progress, long position, long duration) {
        super.setProgressAndText(progress, position, duration);
        if (progress != 0) bottomProgressBar.setProgress(progress);
    }

    @Override
    public void setBufferProgress(int bufferProgress) {
        super.setBufferProgress(bufferProgress);
        if (bufferProgress != 0) bottomProgressBar.setSecondaryProgress(bufferProgress);
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
        bottomProgressBar.setProgress(0);
        bottomProgressBar.setSecondaryProgress(0);
    }

    public void changeUiToNormal() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.GONE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }
    }

    public void changeUiToPreparing() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.GONE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.GONE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.GONE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPauseShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.GONE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }
    }

    public void changeUiToPauseClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.GONE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToComplete() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.GONE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToError() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(View.GONE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro,
                                        int thumbImg, int bottomPro, int retryLayout) {
        topContainer.setVisibility(VISIBLE);
        bottomContainer.setVisibility(bottomCon);
        startButton.setVisibility(startBtn);
        loadingProgressBar.setVisibility(loadingPro);
        thumbImageView.setVisibility(thumbImg);
        bottomProgressBar.setVisibility(bottomPro);
        mRetryLayout.setVisibility(retryLayout);

        if(bottomCon==View.VISIBLE) {
           line_good_info.setVisibility(View.GONE);
           relt_bottom_user.setVisibility(View.GONE);
           include_good.setVisibility(View.GONE);
        }else{
            line_good_info.setVisibility(View.VISIBLE);
            relt_bottom_user.setVisibility(View.VISIBLE);
            if(blog==null||blog.related_goods==null||blog.related_goods.size()==0){
                include_good.setVisibility(View.GONE);
            }else {
                include_good.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(cn.jzvd.R.drawable.jz_click_pause_selector);
            playControl.setImageResource(R.drawable.img_xiangqing_zanting);
            replayTextView.setVisibility(INVISIBLE);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setVisibility(INVISIBLE);
            replayTextView.setVisibility(INVISIBLE);
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(cn.jzvd.R.drawable.icon_faxian_chongbo);
            replayTextView.setVisibility(VISIBLE);
        } else {
            startButton.setImageResource(cn.jzvd.R.drawable.jz_click_play_selector);
            playControl.setImageResource(R.drawable.img_xiangqing_bofang);
            replayTextView.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(cn.jzvd.R.layout.jz_dialog_progress, null);
            mDialogProgressBar = localView.findViewById(cn.jzvd.R.id.duration_progressbar);
            mDialogSeekTime = localView.findViewById(cn.jzvd.R.id.tv_current);
            mDialogTotalTime = localView.findViewById(cn.jzvd.R.id.tv_duration);
            mDialogIcon = localView.findViewById(cn.jzvd.R.id.duration_image_tip);
            mProgressDialog = createDialogWithView(localView);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        mDialogSeekTime.setText(seekTime);
        mDialogTotalTime.setText(" / " + totalTime);
        mDialogProgressBar.setProgress(totalTimeDuration <= 0 ? 0 : (int) (seekTimePosition * 100 / totalTimeDuration));
        if (deltaX > 0) {
            mDialogIcon.setBackgroundResource(cn.jzvd.R.drawable.jz_forward_icon);
        } else {
            mDialogIcon.setBackgroundResource(cn.jzvd.R.drawable.jz_backward_icon);
        }
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        if (mVolumeDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(cn.jzvd.R.layout.jz_dialog_volume, null);
            mDialogVolumeImageView = localView.findViewById(cn.jzvd.R.id.volume_image_tip);
            mDialogVolumeTextView = localView.findViewById(cn.jzvd.R.id.tv_volume);
            mDialogVolumeProgressBar = localView.findViewById(cn.jzvd.R.id.volume_progressbar);
            mVolumeDialog = createDialogWithView(localView);
        }
        if (!mVolumeDialog.isShowing()) {
            mVolumeDialog.show();
        }
        if (volumePercent <= 0) {
            mDialogVolumeImageView.setBackgroundResource(cn.jzvd.R.drawable.jz_close_volume);
        } else {
            mDialogVolumeImageView.setBackgroundResource(cn.jzvd.R.drawable.jz_add_volume);
        }
        if (volumePercent > 100) {
            volumePercent = 100;
        } else if (volumePercent < 0) {
            volumePercent = 0;
        }
        mDialogVolumeTextView.setText(volumePercent + "%");
        mDialogVolumeProgressBar.setProgress(volumePercent);
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (mVolumeDialog != null) {
            mVolumeDialog.dismiss();
        }
    }

    @Override
    public void showBrightnessDialog(int brightnessPercent) {
        super.showBrightnessDialog(brightnessPercent);
        if (mBrightnessDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(cn.jzvd.R.layout.jz_dialog_brightness, null);
            mDialogBrightnessTextView = localView.findViewById(cn.jzvd.R.id.tv_brightness);
            mDialogBrightnessProgressBar = localView.findViewById(cn.jzvd.R.id.brightness_progressbar);
            mBrightnessDialog = createDialogWithView(localView);
        }
        if (!mBrightnessDialog.isShowing()) {
            mBrightnessDialog.show();
        }
        if (brightnessPercent > 100) {
            brightnessPercent = 100;
        } else if (brightnessPercent < 0) {
            brightnessPercent = 0;
        }
        mDialogBrightnessTextView.setText(brightnessPercent + "%");
        mDialogBrightnessProgressBar.setProgress(brightnessPercent);
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
        if (mBrightnessDialog != null) {
            mBrightnessDialog.dismiss();
        }
    }

    public Dialog createDialogWithView(View localView) {
        Dialog dialog = new Dialog(getContext(), cn.jzvd.R.style.jz_style_dialog_progress);
        dialog.setContentView(localView);
        Window window = dialog.getWindow();
        window.addFlags(Window.FEATURE_ACTION_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.gravity = Gravity.CENTER;
        window.setAttributes(localLayoutParams);
        return dialog;
    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(mDismissControlViewTimerTask, 2500);
    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
        }

    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        cancelDismissControlViewTimer();
        if (mAudioManager != null){
            int video_volume = (int) SharedPrefUtil.getCacheSharedPrfLong("video_volume", 0);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,video_volume,0);
        }
        //LogUtil.zhLogW("=====onAutoCompletion==========");
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        cancelDismissControlViewTimer();
        if (clarityPopWindow != null) {
            clarityPopWindow.dismiss();
        }
        //LogUtil.zhLogW("=====onCompletion==========");
    }

    public void dissmissControlView() {
        if (currentState != CURRENT_STATE_NORMAL
                && currentState != CURRENT_STATE_ERROR
                && currentState != CURRENT_STATE_AUTO_COMPLETE) {
            post(() -> {
                bottomContainer.setVisibility(View.INVISIBLE);
                line_good_info.setVisibility(View.VISIBLE);
                relt_bottom_user.setVisibility(View.VISIBLE);
                if(blog==null||blog.related_goods==null||blog.related_goods.size()==0){
                    include_good.setVisibility(View.GONE);
                }else {
                    include_good.setVisibility(View.VISIBLE);
                }
                topContainer.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                if (clarityPopWindow != null) {
                    clarityPopWindow.dismiss();
                }
                if (currentScreen != SCREEN_WINDOW_TINY) {
                    bottomProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void release() {
        super.release();
        if (JZMediaManager.surface != null) JZMediaManager.surface.release();
        if (JZMediaManager.savedSurfaceTexture != null)
            JZMediaManager.savedSurfaceTexture.release();
        JZMediaManager.textureView = null;
        JZMediaManager.savedSurfaceTexture = null;
        //LogUtil.zhLogW("=====release==========");
    }

    public class DismissControlViewTimerTask extends TimerTask {

        @Override
        public void run() {
            dissmissControlView();
        }
    }

    public String currentUrl;

    private void downFileStart(){
        downloadUtils = new DownloadUtils(new JsDownloadListener() {
            @Override
            public void onStartDownload() {
            }
            @Override
            public void onProgress(int progress) {
                downLoadDialogProgress.showProgress(progress);
            }
            @Override
            public void onFinishDownload(String filePath,boolean isCancel) {
                if(!isCancel)
                downLoadDialogProgress.downLoadSuccess();
            }
            @Override
            public void onFail(String errorInfo) {
                downLoadDialogProgress.dissMissDialog();
            }
            @Override
            public void onFinishEnd() {
                parseAttent.downVideo();
            }
        });
        boolean checkState = downloadUtils.checkDownLoadFileExists(currentUrl);
        if (checkState) {
            Common.staticToast("已下载过该视频,请勿重复下载!");
            return;
        }
        downLoadDialogProgress.showDownLoadDialogProgress(getContext(), new DownLoadDialogProgress.downStateListen() {
            @Override
            public void cancelDownLoad() {
                downloadUtils.setCancel(true);
                parseAttent.downVideo();
            }

            @Override
            public void fileDownLoad() {
                downloadUtils.download(currentUrl,downloadUtils.fileName);
            }
        }, !NetworkUtils.isWifiConnected(getContext()));
    }


    public void saveVideoFile(String fileDir) {
        //strDir视频路径
        Uri localUri = Uri.parse("file://" + fileDir);
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
        getContext().sendBroadcast(localIntent);
        EventBus.getDefault().post("");
        Message obtain = Message.obtain();
        obtain.obj = "下载成功";
        obtain.what = 100;
        mHandler.sendMessage(obtain);
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100){
//                Common.staticToast((String) msg.obj);
                if(((String) msg.obj).equals("下载成功")){
                   parseAttent.downVideo();
                }
            }
        }
    };

    public interface updateParseAttent{
        /**点赞取消点赞*/
        void updateParse(boolean isParse);
        /**关注取消关注*/
        void updateAttent(boolean isAttent,String nickName);
        /**下载视频成功回调*/
        void downVideo();
        /**分享内容成功回调*/
        void shareBolg();
        /**进入商品详情页面*/
        void startGoodInfo();

        void startListGoodInfo();

        void destoryVideo();
    }

}
