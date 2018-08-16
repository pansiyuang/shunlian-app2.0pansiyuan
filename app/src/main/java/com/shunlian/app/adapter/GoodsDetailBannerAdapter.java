package com.shunlian.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shunlian.app.R;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.SmallVideoPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.jzvd.JZVideoPlayer;

/**
 * Created by zhanghe on 2018/8/15.
 */

public class GoodsDetailBannerAdapter extends PagerAdapter {

    private Context mContext;
    private boolean mHasVideo;
    private ArrayList<String> mPics;
    private final LayoutInflater mInflater;
    private boolean isOpenWindowTiny;//是否打开小窗口
    private SmallVideoPlayer videoPlayer;

    public GoodsDetailBannerAdapter(Context context, boolean hasVideo, ArrayList<String> pics){
        mContext = context;
        mHasVideo = hasVideo;
        mPics = pics;
        if (mPics == null){
            mPics = new ArrayList<>();
        }
        mInflater = LayoutInflater.from(mContext);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scrollPosition(DefMessageEvent event){
        if (event.itemPosition >= 1 && !isOpenWindowTiny){
            isOpenWindowTiny = true;
            if (videoPlayer != null)
                videoPlayer.startWindowTiny();
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
        if (mHasVideo && position == 0){
            String videoPath = mPics.get(position);
            view = mInflater.inflate(R.layout.item_video, container, false);
            videoPlayer = view.findViewById(R.id.customVideoPlayer);
            GlideUtils.getInstance().loadImage(mContext, videoPlayer.thumbImageView, mPics.get(position+1));
            videoPlayer.setUp(videoPath, JZVideoPlayer.SCREEN_WINDOW_NORMAL);
            view.setOnClickListener(v -> videoPlayer.startVideo());

            ImageView iv_close = view.findViewById(R.id.iv_close);
            ImageView iv_voice = view.findViewById(R.id.iv_voice);
            iv_close.setOnClickListener(v -> videoPlayer.onAutoCompletion());

            iv_voice.setOnClickListener(v -> {
                if (videoPlayer != null){
                    int ringerMode = videoPlayer.getRingerMode();
                    if (ringerMode != 0){
                        videoPlayer.setRingerMode(0);
                        iv_voice.setImageResource(R.drawable.img_xiangqing_yinliang_h);
                    }else {
                        videoPlayer.setRingerMode(1);
                        iv_voice.setImageResource(R.drawable.img_xiangqing_yinliang_n);
                    }
                }
            });
        }else {
            String pic = mPics.get(position);
            view = mInflater.inflate(R.layout.item_detail, container, false);
            MyImageView miv_pic = view.findViewById(R.id.miv_pic);
            GlideUtils.getInstance().loadImage(mContext,miv_pic,pic);
        }
        container.addView(view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        int i = TransformUtil.dip2px(mContext, 360);
        layoutParams.height = i;
        view.setLayoutParams(layoutParams);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    public void destroy(){
        EventBus.getDefault().unregister(this);
        isOpenWindowTiny = false;
        if (videoPlayer != null){
            videoPlayer.onAutoCompletion();
        }
    }
}
