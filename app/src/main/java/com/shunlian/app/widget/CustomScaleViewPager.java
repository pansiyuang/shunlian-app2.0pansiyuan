package com.shunlian.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.shunlian.app.adapter.GoodsDetailBannerAdapter;

import java.util.ArrayList;

/**
 * 禁止左右滑动的ViewPager
 */
public class CustomScaleViewPager extends ViewPager {

    private boolean mHasVideo;
    private String mVideoPath;
    private ArrayList<String> mPics;
    private String mType;

    public CustomScaleViewPager(Context context) {
        this(context,null);
    }

    public CustomScaleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 是否有视频
     * @param hasVideo
     */
    public void setHasVideo(boolean hasVideo) {
        mHasVideo = hasVideo;
    }

    /**
     * 视频路径
     * @param path
     */
    public void setVideoPath(String path) {
        mVideoPath = path;
    }

    /**
     * 图片banner图
     * @param pics
     * @param type 类型 1是优品  2是团购
     */
    public void setBanner(ArrayList<String> pics, String type) {
        mPics = pics;
        mType = type;
        if (mHasVideo && !TextUtils.isEmpty(mVideoPath))
            mPics.add(0,mVideoPath);
        GoodsDetailBannerAdapter adapter = new GoodsDetailBannerAdapter(getContext(),mHasVideo,mPics);
        setAdapter(adapter);
    }



}