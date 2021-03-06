package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.MemberEntity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class MemberListAdapter extends BaseRecyclerAdapter<MemberEntity.Member> {
    private OnAdapterCallBack mCallBack;

    public MemberListAdapter(Context context, List<MemberEntity.Member> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new FansViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_fans, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FansViewHolder) {
            FansViewHolder memberViewHolder = (FansViewHolder) holder;
            MemberEntity.Member member = lists.get(position);
            GlideUtils.getInstance().loadCircleAvar(context, memberViewHolder.miv_icon, member.avatar);
            memberViewHolder.tv_name.setText(member.nickname);

            if (member.focus_status == 1) {//已经关注
                memberViewHolder.tv_attention.setBackgroundDrawable(null);
                memberViewHolder.tv_attention.setText("已关注");
                memberViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
            } else {
                memberViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
                memberViewHolder.tv_attention.setText("关注");
                memberViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
            }

            if (member.add_v == 1) {
                memberViewHolder.miv_v.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(context, memberViewHolder.miv_v, member.v_icon);
            } else {
                memberViewHolder.miv_v.setVisibility(View.GONE);
            }

            if (member.expert == 1) {
                memberViewHolder.miv_expert.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(context, memberViewHolder.miv_expert, member.expert_icon);
            } else {
                memberViewHolder.miv_expert.setVisibility(View.GONE);
            }

            memberViewHolder.miv_icon.setOnClickListener(v -> MyPageActivity.startAct(context, member.member_id));
            memberViewHolder.tv_name.setOnClickListener(v -> MyPageActivity.startAct(context, member.member_id));

            memberViewHolder.tv_attention.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.toFocusUser(member.focus_status, member.member_id,member.nickname);
                }
            });
        }
    }

    public class FansViewHolder extends BaseRecyclerViewHolder {

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

        public FansViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setAdapterCallBack(OnAdapterCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnAdapterCallBack {
        void toFocusUser(int isFocus, String memberId,String nickName);
    }
}
