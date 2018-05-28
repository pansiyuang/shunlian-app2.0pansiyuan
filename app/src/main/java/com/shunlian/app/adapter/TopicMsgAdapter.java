package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/11.
 */

public class TopicMsgAdapter extends BaseRecyclerAdapter<StoreMsgEntity.StoreMsg> {

    public TopicMsgAdapter(Context context, List<StoreMsgEntity.StoreMsg> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TopicMsgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic_msg, parent, false));
    }


    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        TopicMsgViewHolder topicMsgViewHolder = (TopicMsgViewHolder) holder;
        StoreMsgEntity.StoreMsg storeMsg = lists.get(position);
        StoreMsgEntity.Body body = storeMsg.body;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) topicMsgViewHolder.tv_date.getLayoutParams();
        if (storeMsg.is_read == 1) { //已读
            topicMsgViewHolder.miv_point.setVisibility(View.GONE);
            layoutParams.setMargins(0, TransformUtil.dip2px(context, 6), 0, 0);
        } else {
            topicMsgViewHolder.miv_point.setVisibility(View.VISIBLE);
            layoutParams.setMargins(TransformUtil.dip2px(context, 12), TransformUtil.dip2px(context, 6), 0, 0);
        }
        topicMsgViewHolder.tv_date.setLayoutParams(layoutParams);
        topicMsgViewHolder.tv_title.setText(body.title);
        GlideUtils.getInstance().loadImage(context, topicMsgViewHolder.miv_img, body.thumb);
        topicMsgViewHolder.tv_date.setText(storeMsg.create_time);
        topicMsgViewHolder.tv_content.setText(body.content);

        if ("1".equals(body.expire)) {//已过期
            topicMsgViewHolder.tv_title.setTextColor(getColor(R.color.new_gray));
            topicMsgViewHolder.tv_content.setTextColor(getColor(R.color.new_gray));
            topicMsgViewHolder.tv_bg.setVisibility(View.VISIBLE);
        } else {//未过期
            topicMsgViewHolder.tv_title.setTextColor(getColor(R.color.new_text));
            topicMsgViewHolder.tv_content.setTextColor(getColor(R.color.new_text));
            topicMsgViewHolder.tv_bg.setVisibility(View.GONE);
        }
    }

    public class TopicMsgViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_point)
        MyImageView miv_point;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_bg)
        TextView tv_bg;

        public TopicMsgViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> listener.onItemClick(v, getAdapterPosition()));
        }
    }
}
