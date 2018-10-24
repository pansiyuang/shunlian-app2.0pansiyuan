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

import butterknife.BindDimen;
import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/15.
 */

public class AttentionAdapter extends BaseRecyclerAdapter<HotBlogsEntity.RecomandFocus> {

    public static final int HEAD_LAYOUT = 10001;
    private OnFocusListener onFocusListener;

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
        if (isEmpty(lists)) {
            emptyViewHolder.tv_attention_title.setVisibility(View.GONE);
        } else {
            emptyViewHolder.tv_attention_title.setVisibility(View.VISIBLE);
        }
    }

    public void handlerItem(RecyclerView.ViewHolder holder, int position) {
        AttentionViewholder attentionViewholder = (AttentionViewholder) holder;
        HotBlogsEntity.RecomandFocus recomandFocus = lists.get(position - 1);
        GlideUtils.getInstance().loadCircleImage(context, attentionViewholder.miv_icon, recomandFocus.avatar);
        attentionViewholder.tv_name.setText(recomandFocus.nickname);
        attentionViewholder.tv_content.setText(recomandFocus.signature);

        attentionViewholder.tv_attention.setOnClickListener(v -> {
            if (onFocusListener != null) {
                onFocusListener.onFocus(recomandFocus.focus_status, recomandFocus.member_id);
            }
        });

        if (recomandFocus.focus_status == 1) {//已经关注
            attentionViewholder.tv_attention.setBackgroundDrawable(null);
            attentionViewholder.tv_attention.setText("已关注");
            attentionViewholder.tv_attention.setTextColor(getColor(R.color.text_gray2));
        } else {
            attentionViewholder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
            attentionViewholder.tv_attention.setText("关注");
            attentionViewholder.tv_attention.setTextColor(getColor(R.color.pink_color));
        }
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

        @BindView(R.id.tv_attention_title)
        TextView tv_attention_title;

        public EmptyViewHolder(View itemView) {
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
