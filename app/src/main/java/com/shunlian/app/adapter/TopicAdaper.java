package com.shunlian.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.TopicEntity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/10/18.
 */

public class TopicAdaper extends BaseRecyclerAdapter<TopicEntity.ItemBean> {

    public int item_id = -1;
    private String mID;

    public TopicAdaper(Context context, List<TopicEntity.ItemBean> lists, String id) {
        super(context, true, lists);
        mID = id;
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_topicv2, parent, false);
        return new TopicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (!isEmpty(payloads)){
            TopicHolder mHolder = (TopicHolder) holder;
            if (position == item_id){
                visible(mHolder.miv_select);
            }else {
                mHolder.miv_select.setVisibility(View.INVISIBLE);
            }
        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        TopicHolder mHolder = (TopicHolder) holder;
        if (position == 0) {
            visible(mHolder.mtvHotTopic);
        } else {
            gone(mHolder.mtvHotTopic);
        }
        String format = "%s人参与/%s条内容";
        TopicEntity.ItemBean itemBean = lists.get(position);
        mHolder.mtvTitle.setText(itemBean.title);
        mHolder.mtvDesc.setText(String.format(format, itemBean.refer_member_num, itemBean.refer_num));

        if (!isEmpty(mID) && mID.equals(itemBean.id)){
            visible(mHolder.miv_select);
        }else {
            mHolder.miv_select.setVisibility(View.INVISIBLE);
        }
    }

    public class TopicHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_hot_topic)
        MyTextView mtvHotTopic;

        @BindView(R.id.mtv_title)
        MyTextView mtvTitle;

        @BindView(R.id.mtv_desc)
        MyTextView mtvDesc;

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.llayout_content)
        RelativeLayout llayout_content;

        public TopicHolder(View itemView) {
            super(itemView);
            llayout_content.setOnClickListener(v -> {
                if (listener != null){
                    listener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }
}
