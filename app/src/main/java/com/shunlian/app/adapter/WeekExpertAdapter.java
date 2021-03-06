package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ExpertEntity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.discover_new.WeekExpertRankFrag;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/19.
 */

public class WeekExpertAdapter extends BaseRecyclerAdapter<ExpertEntity.Expert> {
    private WeekExpertRankFrag weekExpertRankFrag;

    public WeekExpertAdapter(Context context, List<ExpertEntity.Expert> lists, WeekExpertRankFrag frag) {
        super(context, false, lists);
        this.weekExpertRankFrag = frag;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ExpertViewHolder(LayoutInflater.from(context).inflate(R.layout.item_week_rank, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExpertViewHolder) {
            ExpertEntity.Expert expert = lists.get(position);
            ExpertViewHolder expertViewHolder = (ExpertViewHolder) holder;
            GlideUtils.getInstance().loadCircleAvar(context, expertViewHolder.miv_icon, expert.avatar);
            expertViewHolder.tv_nickname.setText(expert.nickname);
            expertViewHolder.tv_hot.setText(expert.hot_val + "热度");
            switch (position) {
                case 0:
                    expertViewHolder.miv_rank.setImageResource(R.mipmap.img_paihangbang_one);
                    expertViewHolder.miv_rank.setVisibility(View.VISIBLE);
                    expertViewHolder.tv_rank.setVisibility(View.GONE);
                    break;
                case 1:
                    expertViewHolder.miv_rank.setImageResource(R.mipmap.img_paihangbang_two);
                    expertViewHolder.miv_rank.setVisibility(View.VISIBLE);
                    expertViewHolder.tv_rank.setVisibility(View.GONE);
                    break;
                case 2:
                    expertViewHolder.miv_rank.setImageResource(R.mipmap.img_paihangbang_three);
                    expertViewHolder.miv_rank.setVisibility(View.VISIBLE);
                    expertViewHolder.tv_rank.setVisibility(View.GONE);
                    break;
                default:
                    expertViewHolder.tv_rank.setText(String.valueOf(position + 1));
                    expertViewHolder.miv_rank.setVisibility(View.GONE);
                    expertViewHolder.tv_rank.setVisibility(View.VISIBLE);
                    break;
            }

            if (expert.add_v == 0) {
                expertViewHolder.miv_v.setVisibility(View.GONE);
            } else {
                expertViewHolder.miv_v.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(context, expertViewHolder.miv_v, expert.v_icon);
            }

            if (expert.expert == 0) {
                expertViewHolder.miv_expert.setVisibility(View.GONE);
            } else {
                expertViewHolder.miv_expert.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(context, expertViewHolder.miv_expert, expert.expert_icon);
            }

            if (expert.focus_status == 1) {
                expertViewHolder.tv_attention.setBackgroundDrawable(null);
                expertViewHolder.tv_attention.setText("已关注");
                expertViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
                expertViewHolder.tv_attention.setVisibility(View.GONE);
            } else {
                expertViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
                expertViewHolder.tv_attention.setText("关注");
                expertViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
                expertViewHolder.tv_attention.setVisibility(View.VISIBLE);
            }
            expertViewHolder.miv_icon.setOnClickListener(v -> MyPageActivity.startAct(context, expert.member_id));
            expertViewHolder.tv_nickname.setOnClickListener(v -> MyPageActivity.startAct(context, expert.member_id));

            expertViewHolder.tv_attention.setOnClickListener(v -> {
                weekExpertRankFrag.toFocus(expert.focus_status, expert.member_id);
            });
        }
    }


    public class ExpertViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_v)
        MyImageView miv_v;

        @BindView(R.id.miv_expert)
        MyImageView miv_expert;

        @BindView(R.id.miv_rank)
        MyImageView miv_rank;

        @BindView(R.id.tv_rank)
        TextView tv_rank;

        @BindView(R.id.tv_nickname)
        TextView tv_nickname;

        @BindView(R.id.tv_hot)
        TextView tv_hot;

        @BindView(R.id.tv_attention)
        TextView tv_attention;

        public ExpertViewHolder(View itemView) {
            super(itemView);
        }
    }
}
