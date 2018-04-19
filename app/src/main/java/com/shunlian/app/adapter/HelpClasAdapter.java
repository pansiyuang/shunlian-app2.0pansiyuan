package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.HelpClassEntity;
import com.shunlian.app.bean.HelpcenterIndexEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/27.
 */

public class HelpClasAdapter extends BaseRecyclerAdapter<HelpClassEntity.Article> {

    public HelpClasAdapter(Context context, boolean isShowFooter, List<HelpClassEntity.Article> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new HolderView(LayoutInflater.from(context).inflate(R.layout.item_help_class, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderView) {
            HolderView viewHolder = (HolderView) holder;
            HelpClassEntity.Article category = lists.get(position);
            viewHolder.mtv_desc.setText(category.desc);
            viewHolder.mtv_title.setText(category.title);
            viewHolder.mtv_author.setText(category.author);
            GlideUtils.getInstance().loadImage(context,viewHolder.miv_photo,category.image);
        }
    }

    public class HolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_author)
        MyTextView mtv_author;

        @BindView(R.id.miv_photo)
        MyImageView miv_photo;


        public HolderView(View itemView) {
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
