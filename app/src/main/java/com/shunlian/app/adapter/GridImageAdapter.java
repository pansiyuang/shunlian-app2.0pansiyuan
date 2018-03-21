package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/16.
 */

public class GridImageAdapter extends BaseRecyclerAdapter<String> {

    public GridImageAdapter(Context context, List<String> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ImageHolderView(LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageHolderView) {
            ImageHolderView imageHolderView = (ImageHolderView) holder;
            GlideUtils.getInstance().loadImage(context, imageHolderView.miv_pic, lists.get(position));
        }
    }

    public class ImageHolderView extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        public ImageHolderView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
