package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ExpertEntity;
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
            GlideUtils.getInstance().loadCircleImage(context, expertViewHolder.miv_icon, expert.avatar);
            expertViewHolder.tv_nickname.setText(expert.nickname);
            expertViewHolder.tv_hot.setText(expert.hot_val + "热度");
            if (expert.focus_status == 1) {
                expertViewHolder.tv_attention.setBackgroundDrawable(null);
                expertViewHolder.tv_attention.setText("已关注");
                expertViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
            } else {
                expertViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
                expertViewHolder.tv_attention.setText("关注");
                expertViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
            }

            expertViewHolder.tv_attention.setOnClickListener((View v) -> {
                mFrag.toFocus(expert.focus_status, expert.member_id);
            });
        }
    }

    public class ExpertViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

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
