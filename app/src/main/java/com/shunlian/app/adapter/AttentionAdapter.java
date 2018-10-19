package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/15.
 */

public class AttentionAdapter extends BaseRecyclerAdapter<HotBlogsEntity.RecomandFocus> {

    public static final int HEAD_LAYOUT = 10001;

    public AttentionAdapter(Context context, List<HotBlogsEntity.RecomandFocus> lists) {
        super(context, false, lists);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD_LAYOUT:
                return new EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.empty_attention, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new AttentionViewholder(LayoutInflater.from(context).inflate(R.layout.item_attention, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_LAYOUT;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEAD_LAYOUT:
                handleEmpty(holder);
                break;
            default:
                handlerItem(holder, position);
                break;
        }
    }

    public void handleEmpty(RecyclerView.ViewHolder holder) {
        EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
    }

    public void handlerItem(RecyclerView.ViewHolder holder, int position) {
        AttentionViewholder attentionViewholder = (AttentionViewholder) holder;
        HotBlogsEntity.RecomandFocus recomandFocus = lists.get(position - 1);
        GlideUtils.getInstance().loadCircleImage(context, attentionViewholder.miv_icon, recomandFocus.avatar);
        attentionViewholder.tv_name.setText(recomandFocus.nickname);
        attentionViewholder.tv_content.setText(recomandFocus.signature);
    }

    public class AttentionViewholder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_attention)
        TextView tv_attention;

        public AttentionViewholder(View itemView) {
            super(itemView);
        }
    }

    public class EmptyViewHolder extends BaseRecyclerViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
