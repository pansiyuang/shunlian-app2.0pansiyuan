package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class AttentionMsgAdapter extends BaseRecyclerAdapter<HotBlogsEntity.MemberInfo> {
    private OnAdapterCallBack mCallBack;

    public AttentionMsgAdapter(Context context, List<HotBlogsEntity.MemberInfo> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new AttentionMsgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_fans, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AttentionMsgViewHolder) {
            AttentionMsgViewHolder attentionMsgViewHolder = (AttentionMsgViewHolder) holder;
            HotBlogsEntity.MemberInfo memberInfo = lists.get(position);
            GlideUtils.getInstance().loadCircleAvar(context, attentionMsgViewHolder.miv_icon, memberInfo.avatar);
            attentionMsgViewHolder.tv_name.setText(memberInfo.nickname);

            if (memberInfo.is_fans == 1) {//已经关注
                attentionMsgViewHolder.tv_attention.setBackgroundDrawable(null);
                attentionMsgViewHolder.tv_attention.setText("已关注");
                attentionMsgViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
            } else {
                attentionMsgViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
                attentionMsgViewHolder.tv_attention.setText("关注");
                attentionMsgViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
            }

            if (memberInfo.add_v == 1) {
                attentionMsgViewHolder.miv_v.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(context, attentionMsgViewHolder.miv_v, memberInfo.v_icon);
            } else {
                attentionMsgViewHolder.miv_v.setVisibility(View.GONE);
            }

            if (memberInfo.expert == 1) {
                attentionMsgViewHolder.miv_expert.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(context, attentionMsgViewHolder.miv_expert, memberInfo.expert_icon);
            } else {
                attentionMsgViewHolder.miv_expert.setVisibility(View.GONE);
            }

            attentionMsgViewHolder.tv_attention.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.toFocusUser(memberInfo.is_fans, memberInfo.member_id);
                }
            });
            attentionMsgViewHolder.miv_icon.setOnClickListener(v -> MyPageActivity.startAct(context, memberInfo.member_id));
            attentionMsgViewHolder.tv_name.setOnClickListener(v -> MyPageActivity.startAct(context, memberInfo.member_id));
        }
    }

    public class AttentionMsgViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_v)
        MyImageView miv_v;

        @BindView(R.id.miv_expert)
        MyImageView miv_expert;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_attention)
        TextView tv_attention;

        public AttentionMsgViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setAdapterCallBack(OnAdapterCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnAdapterCallBack {
        void toFocusUser(int isFocus, String memberId);
    }
}
