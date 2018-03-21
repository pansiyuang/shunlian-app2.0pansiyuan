package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/21.
 */

public class SinglePicAdapter extends BaseRecyclerAdapter<String> {

    public SinglePicAdapter(Context context, boolean isShowFooter, List<String> list) {
        super(context, isShowFooter, list);

    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new MViewHolder(LayoutInflater.from(context).inflate(R.layout.item_single_pic, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        MViewHolder viewHolder = (MViewHolder) holder;
        GlideUtils.getInstance().loadCornerImage(context,viewHolder.miv_pic,lists.get(position),4);
    }

    @Override
    public int getItemCount() {
        if (lists.size() > 9) {
            return 9;
        }
        return super.getItemCount();
    }


    public class MViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener{
        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        public MViewHolder(View itemView) {
            super(itemView);
//            int picWidth = (Common.getScreenWidth((Activity) context) - 18 - 40) / 3;
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth, picWidth);
//            miv_pic.setLayoutParams(params);
//
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
