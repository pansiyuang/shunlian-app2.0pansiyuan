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

    private OnFocusListener onFocusListener;

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

            if (recomandFocus.focus_status == 1) {//已经关注
                memberViewHolder.tv_attention.setBackgroundDrawable(null);
                memberViewHolder.tv_attention.setText("已关注");
                memberViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
            } else {
                memberViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_56px));
                memberViewHolder.tv_attention.setText("关注");
                memberViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
            }

            memberViewHolder.tv_attention.setOnClickListener(v -> {
                if (onFocusListener != null) {
                    onFocusListener.onFocus(recomandFocus.focus_status, recomandFocus.member_id);
                }
            });
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

    public void setOnFocusListener(OnFocusListener listener) {
        this.onFocusListener = listener;
    }

    public interface OnFocusListener {

        void onFocus(int isFocus, String memberId);
    }
}
