package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.utils.EmojisUtils;

/**
 * Created by Administrator on 2018/4/11.
 */

public class EmojisVPAdapter extends PagerAdapter {

    private Context mContext;
    private final LayoutInflater inflater;

    public EmojisVPAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        if (EmojisUtils.emojisMap().size() % 21 != 0){
            return EmojisUtils.emojisMap().size() / 21 + 1;
        }
        return EmojisUtils.emojisMap().size() / 21;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView recycler_view = (RecyclerView) inflater.inflate(R.layout.recycler_view,
                null, false);
        container.addView(recycler_view);
        GridLayoutManager manager = new GridLayoutManager(mContext,7);
        recycler_view.setLayoutManager(manager);
        recycler_view.setAdapter(new EmojiAdapter(mContext,position));
        return recycler_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
