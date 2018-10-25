package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/18.
 */

public class DiscoverActivityAdapter extends BaseRecyclerAdapter<DiscoverActivityEntity.Activity> {

    public DiscoverActivityAdapter(Context context, List<DiscoverActivityEntity.Activity> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ActivityViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ActivityViewHolder) {
            ActivityViewHolder activityViewHolder = (ActivityViewHolder) holder;
            DiscoverActivityEntity.Activity activity = lists.get(position);
            GlideUtils.getInstance().loadImage(context, activityViewHolder.miv_icon, activity.thumb);
            activityViewHolder.tv_topic.setText(activity.title);
            if (isEmpty(activity.refer_member_num)) {
                activityViewHolder.tv_join.setVisibility(View.GONE);
            } else {
                int memberCount = Integer.valueOf(activity.refer_member_num);
                if (memberCount != 0) {
                    activityViewHolder.tv_join.setText(memberCount + "人在参与");
                    activityViewHolder.tv_join.setVisibility(View.VISIBLE);
                } else {
                    activityViewHolder.tv_join.setVisibility(View.GONE);
                }
            }
        }
    }

    public class ActivityViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_topic)
        TextView tv_topic;

        @BindView(R.id.recycler_list)
        RecyclerView recycler_list;

        @BindView(R.id.tv_join)
        TextView tv_join;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            if (listener != null) {
                listener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
