package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/18.
 */

public class AttentionMemberAdapter extends BaseRecyclerAdapter<HotBlogsEntity.RecomandFocus> {

    public AttentionMemberAdapter(Context context, List<HotBlogsEntity.RecomandFocus> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new MemberViewHolder(LayoutInflater.from(context).inflate(R.layout.item_attention_member, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MemberViewHolder) {
            MemberViewHolder memberViewHolder = (MemberViewHolder) holder;
            HotBlogsEntity.RecomandFocus recomandFocus = lists.get(position);
            GlideUtils.getInstance().loadCircleImage(context, memberViewHolder.miv_icon, recomandFocus.avatar);
            memberViewHolder.tv_nickname.setText(recomandFocus.nickname);
            memberViewHolder.tv_attention_count.setText(recomandFocus.follow_num + "人关注");
            memberViewHolder.tv_blogs_count.setText(recomandFocus.blog_num + "篇内容");
        }
    }

    public class MemberViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_nickname)
        TextView tv_nickname;

        @BindView(R.id.tv_attention_count)
        TextView tv_attention_count;

        @BindView(R.id.tv_blogs_count)
        TextView tv_blogs_count;

        @BindView(R.id.tv_attention)
        TextView tv_attention;

        public MemberViewHolder(View itemView) {
            super(itemView);
        }
    }
}
