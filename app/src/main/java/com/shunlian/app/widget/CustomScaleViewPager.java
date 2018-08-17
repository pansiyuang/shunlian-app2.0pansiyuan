package com.shunlian.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.shunlian.app.adapter.GoodsDetailBannerAdapter;

import java.util.ArrayList;

/**
 * 禁止左右滑动的ViewPager
 */
public class CustomScaleViewPager extends ViewPager {

    private String mVideoPath;
    private ArrayList<String> mPics = new ArrayList<>();
    private GoodsDetailBannerAdapter adapter;

    public CustomScaleViewPager(Context context) {
        this(context,null);
    }

    public CustomScaleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 图片banner图
     * @param pics
     */
    public void setBanner(String path, ArrayList<String> pics) {
        mPics.clear();
        mPics.addAll(pics);
        mVideoPath = path;

        if (adapter == null) {
            adapter = new GoodsDetailBannerAdapter(getContext(), mVideoPath, mPics);
            setAdapter(adapter);
        }
    }

    public void pausePlay(){
        if (adapter != null) adapter.pausePlay();
    }

}