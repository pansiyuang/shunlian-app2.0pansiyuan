package com.shunlian.app.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public abstract class SimpleRecyclerAdapter <T> extends RecyclerView.Adapter<SimpleViewHolder> {

    private int mLayoutResId;
    private Context mContext;
    @Nullable
    private List<T> mData;
    private OnItemClickListener mListener;

    public SimpleRecyclerAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<T> data){
        mContext = context;
        this.mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(mLayoutResId, parent, false);
        return new SimpleViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        convert(holder, mData.get(position));
    }

    public abstract void convert(SimpleViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置条目点击
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){

        mListener = listener;
    }

    public OnItemClickListener getOnItemChildClickListener() {
        return mListener;
    }
}