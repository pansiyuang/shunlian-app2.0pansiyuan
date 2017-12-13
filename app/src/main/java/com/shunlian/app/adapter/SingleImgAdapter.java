package com.shunlian.app.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/12.
 */

public class SingleImgAdapter extends BaseRecyclerAdapter<String> {

    private static final int FOOTER = 2;

    public SingleImgAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER:
                View inflate = LayoutInflater.from(context).inflate(R.layout.item_single_img, parent, false);
                return new DelImagViewHolder(inflate);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount())
            return FOOTER;
        else
            return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case FOOTER:
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_single_img, parent, false);
        return new ImagViewHolder(inflate);
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImagViewHolder) {
            ImagViewHolder imagViewHolder = (ImagViewHolder) holder;
            imagViewHolder.miv_img_del.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImage(context, imagViewHolder.miv_img, lists.get(position));
        } else {
            DelImagViewHolder delImagViewHolder = (DelImagViewHolder) holder;
            delImagViewHolder.miv_img_del.setVisibility(View.GONE);
            delImagViewHolder.miv_img.setImageResource(R.mipmap.img_tupian);
        }
    }


    public class ImagViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.miv_img_del)
        MyImageView miv_img_del;

        public ImagViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class DelImagViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.miv_img_del)
        MyImageView miv_img_del;

        public DelImagViewHolder(View itemView) {
            super(itemView);
        }
    }
}
