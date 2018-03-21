package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ChangeTopicAdapter extends BaseRecyclerAdapter<ArticleEntity.Topic> {
    private List<ArticleEntity.Topic> mTopics;

    public ChangeTopicAdapter(Context context, List<ArticleEntity.Topic> lists) {
        super(context, false, lists);
        this.mTopics = lists;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TopicHolderView(LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        TopicHolderView topicHolderView = (TopicHolderView) holder;
        ArticleEntity.Topic topic = mTopics.get(position);
        GlideUtils.getInstance().loadImage(context, topicHolderView.miv_icon, topic.thumb);
        topicHolderView.tv_title.setText(topic.subject);
        topicHolderView.tv_attention.setText(topic.join_in_num + "人关注");
    }

    public void setData(List<ArticleEntity.Topic> mData) {
        if (!isEmpty(mData)) {
            mTopics = mData;
            notifyDataSetChanged();
        }
    }

    public class TopicHolderView extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_attention)
        TextView tv_attention;

        public TopicHolderView(View itemView) {
            super(itemView);
        }
    }
}
