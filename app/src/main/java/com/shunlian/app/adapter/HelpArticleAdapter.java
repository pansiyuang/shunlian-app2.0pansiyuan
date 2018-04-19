package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.HelpcenterIndexEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/27.
 */

public class HelpArticleAdapter extends BaseRecyclerAdapter<HelpcenterIndexEntity.ArticleCategory> {

    public HelpArticleAdapter(Context context, boolean isShowFooter, List<HelpcenterIndexEntity.ArticleCategory> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new HolderView(LayoutInflater.from(context).inflate(R.layout.item_help_article, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderView) {
            HolderView viewHolder = (HolderView) holder;
            HelpcenterIndexEntity.ArticleCategory category = lists.get(position);

            GlideUtils.getInstance().loadCornerImage(context, viewHolder.miv_photo, category.image,4);
        }
    }

    public class HolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
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
