package com.shunlian.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.ui.goods_detail.GoodsDeatilFrag;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.SmallVideoPlayer;
import com.shunlian.app.widget.VideoBannerWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by zhanghe on 2018/8/15.
 */

public class GoodsDetailBannerAdapter extends PagerAdapter {

    private Context mContext;
    private String mVideoPath;
    private ArrayList<String> mPics;
    private final LayoutInflater mInflater;
    private boolean isOpenWindowTiny;//是否打开小窗口
    private SmallVideoPlayer videoPlayer;
    private LinearLayout ll_wifi_state;
    private final int deviceWidth;



    public GoodsDetailBannerAdapter(Context context, String videoPath, ArrayList<String> pics){
        mContext = context;
        mVideoPath = videoPath;
        mPics = pics;
        if (mPics == null){
            mPics = new ArrayList<>();
        }
        mInflater = LayoutInflater.from(mContext);
        EventBus.getDefault().register(this);

        deviceWidth = DeviceInfoUtil.getDeviceWidth(mContext);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scrollPosition(DefMessageEvent event){
        if (JZMediaManager.isPlaying()) {
            isOpenWindowTiny = false;
        }else {
            isOpenWindowTiny = true;
        }
        if (event.itemPosition >= 1 && !isOpenWindowTiny && VideoBannerWrapper.mCurrentPosition == 0){
            isOpenWindowTiny = true;
            if (videoPlayer != null)
                videoPlayer.startWindowTiny();
        }

        if (event.isPause){
            SmallVideoPlayer.goOnPlayOnPause();
            SmallVideoPlayer.backPress();
        }

        if (event.isrelease){
            destroy();
        }
    }

    @Override
    public int getCount() {
        return mPics.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;
        if (!TextUtils.isEmpty(mVideoPath) && position == 0){
            String pic = mPics.get(position);
            view = mInflater.inflate(R.layout.item_video, container, false);
            videoPlayer = view.findViewById(R.id.customVideoPlayer);
            GlideUtils.getInstance().loadOverrideImage(mContext,videoPlayer.thumbImageView,pic,deviceWidth,deviceWidth);
            videoPlayer.setUp(mVideoPath, JZVideoPlayer.SCREEN_WINDOW_LIST);
            view.setOnClickListener(v -> videoPlayer.startVideo());

            ll_wifi_state = view.findViewById(R.id.ll_wifi_state);
            if (GoodsDeatilFrag.isShowNetTip){
                ll_wifi_state.setVisibility(View.VISIBLE);
            }else {
                ll_wifi_state.setVisibility(View.GONE);
            }

            //继续播放
            MyImageView continueToPlay = view.findViewById(R.id.miv_continue_to_play);
            continueToPlay.setOnClickListener(v -> {
                ll_wifi_state.setVisibility(View.GONE);
                GoodsDeatilFrag.isShowNetTip = false;
                if (JZMediaManager.textureView == null){
                    videoPlayer.startVideo();
                }else {
                    startPlay();
                }
            });
        }else {
            String pic = mPics.get(position);
            view = mInflater.inflate(R.layout.item_detail, container, false);
            MyImageView miv_pic = view.findViewById(R.id.miv_pic);
            GlideUtils.getInstance().loadOverrideImage(mContext,miv_pic,pic,deviceWidth,deviceWidth);
            view.setOnClickListener(v -> {
                BigImgEntity entity = new BigImgEntity();
                entity.itemList = mPics;
                entity.index = position;
                LookBigImgAct.startAct(mContext, entity);
            });
        }
        container.addView(view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = deviceWidth;
        layoutParams.height = deviceWidth;
        view.setLayoutParams(layoutParams);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     * 暂停播放
     */
    public void pausePlay(){
        SmallVideoPlayer.goOnPlayOnPause();
    }

    /**
     * 暂停播放
     */
    public void startPlay(){
        if (JZMediaManager.textureView == null && videoPlayer != null){
            videoPlayer.startVideo();
        }else {
            SmallVideoPlayer.goOnPlayOnResume();
        }
    }


    public void destroy(){
        EventBus.getDefault().unregister(this);
        isOpenWindowTiny = false;
        if (videoPlayer != null){
            videoPlayer.onAutoCompletion();
            videoPlayer.release();
        }

        if (mPics != null){
            mPics.clear();
            mPics = null;
        }

        videoPlayer = null;
        ll_wifi_state = null;
        GoodsDeatilFrag.isShowNetTip = false;
    }
}
