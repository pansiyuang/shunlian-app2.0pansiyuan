package com.shunlian.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by zhanghe on 2018/8/15.
 */

public class GoodsDetailBannerAdapter extends PagerAdapter {

    private Context mContext;
    private boolean mHasVideo;
    private ArrayList<String> mPics;

    public GoodsDetailBannerAdapter(Context context, boolean hasVideo, ArrayList<String> pics){
        mContext = context;
        mHasVideo = hasVideo;
        mPics = pics;
        if (mPics == null){
            mPics = new ArrayList<>();
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

        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
