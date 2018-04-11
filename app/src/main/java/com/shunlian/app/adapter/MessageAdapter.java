package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.newchat.adapter.TopMessageAdapter;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.newchat.ui.ChatActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SwipeMenuLayout;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/8.
 */

public class MessageAdapter extends BaseRecyclerAdapter<ChatMemberEntity.ChatMember> implements BaseRecyclerAdapter.OnItemClickListener {
    public static final int ITEM_TOP = 100005;
    private List<MessageListEntity.Msg> msgList;

    public MessageAdapter(Context context, List<MessageListEntity.Msg> msgs, List<ChatMemberEntity.ChatMember> memberList) {
        super(context, false, memberList);
        this.msgList = msgs;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TOP:
                return new TopViewHolder(LayoutInflater.from(context).inflate(R.layout.frag_list, parent, false));
            default:
                return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TOP;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (!isEmpty(msgList)) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case ITEM_TOP:
                handlerTop(holder);
                break;
            default:
                handlerItem(holder, position);
                break;
        }
    }

    public void handlerTop(RecyclerView.ViewHolder holder) {
        if (!isEmpty(msgList)) {
            TopViewHolder topViewHolder = (TopViewHolder) holder;
            TopMessageAdapter messageAdapter = new TopMessageAdapter(context, msgList);
            messageAdapter.setOnItemClickListener(this);
            topViewHolder.recycler_list.setAdapter(new TopMessageAdapter(context, msgList));
        }
    }

    public void handlerItem(RecyclerView.ViewHolder holder, final int position) {
        ChatMemberEntity.ChatMember chatMember;
        if (isEmpty(msgList)) {
            chatMember = lists.get(position);
        } else {
            chatMember = lists.get(position - 1);
        }
        MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
        messageViewHolder.tv_name.setText(chatMember.nickname);
//        messageViewHolder.tv_content.setText(chatMember.);
        messageViewHolder.tv_date.setText(chatMember.update_time);
        GlideUtils.getInstance().loadCornerImage(context, messageViewHolder.miv_icon, chatMember.headurl, 5);
        if (chatMember.unread_count > 0) {
            messageViewHolder.tv_count.setVisibility(View.VISIBLE);
            messageViewHolder.tv_count.setText(Common.formatBadgeNumber(chatMember.unread_count));
        } else {
            messageViewHolder.tv_count.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        MessageListEntity.Msg msg = msgList.get(position);
        switch (msg.type) {
            case "4":
                break;
            case "5":
                break;
            case "10":
                break;
        }
    }

    public class TopViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.recycler_list)
        RecyclerView recycler_list;

        public TopViewHolder(View itemView) {
            super(itemView);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recycler_list.setLayoutManager(manager);
        }
    }

    public class MessageViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.swipe_layout)
        SwipeMenuLayout swipe_layout;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_count)
        TextView tv_count;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_del)
        TextView tv_del;

        @BindView(R.id.tv_official)
        TextView tv_official;

        @BindView(R.id.ll_item)
        LinearLayout ll_item;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ll_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
