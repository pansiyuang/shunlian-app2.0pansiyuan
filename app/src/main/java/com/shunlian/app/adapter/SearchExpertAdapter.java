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
import com.shunlian.app.ui.discover_new.search.SearchExpertFrag;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/24.
 */

public class SearchExpertAdapter extends BaseRecyclerAdapter<ExpertEntity.Expert> {
    private SearchExpertFrag mFrag;

    public SearchExpertAdapter(Context context, List<ExpertEntity.Expert> lists, SearchExpertFrag frag) {
        super(context, true, lists);
        this.mFrag = frag;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ExpertViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_expert, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExpertViewHolder) {
            ExpertEntity.Expert expert = lists.get(position);
            ExpertViewHolder expertViewHolder = (ExpertViewHolder) holder;
            GlideUtils.getInstance().loadCircleAvar(context, expertViewHolder.miv_icon, expert.avatar);
            expertViewHolder.tv_name.setText(expert.nickname);
            expertViewHolder.tv_hot.setText(expert.hot + "热度");

            if (expert.is_focus == 1) {
                expertViewHolder.tv_attention.setBackgroundDrawable(null);
                expertViewHolder.tv_attention.setText("已关注");
                expertViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
            } else {
                expertViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
                expertViewHolder.tv_attention.setText("关注");
                expertViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
            }

            if (expert.add_v == 1) {
                expertViewHolder.miv_v.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(context, expertViewHolder.miv_v, expert.v_icon);
            } else {
                expertViewHolder.miv_v.setVisibility(View.GONE);
            }

            if (expert.expert == 1) {
                expertViewHolder.miv_expert.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(context, expertViewHolder.miv_expert, expert.expert_icon);
            } else {
                expertViewHolder.miv_expert.setVisibility(View.GONE);
            }

            expertViewHolder.miv_icon.setOnClickListener(v -> MyPageActivity.startAct(context, expert.member_id));

            expertViewHolder.tv_attention.setOnClickListener((View v) -> {
                mFrag.toFocus(expert.is_focus, expert.member_id);
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

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_hot)
        TextView tv_hot;

        @BindView(R.id.tv_attention)
        TextView tv_attention;

        public ExpertViewHolder(View itemView) {
            super(itemView);
        }
    }
}
