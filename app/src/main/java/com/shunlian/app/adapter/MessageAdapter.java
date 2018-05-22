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
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SwipeMenuLayout;
import com.shunlian.app.utils.TimeUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.newchat.util.TimeUtil.getNewChatTime;
import static com.shunlian.app.newchat.util.TimeUtil.getTime;

/**
 * Created by Administrator on 2018/4/8.
 */

public class MessageAdapter extends BaseRecyclerAdapter<ChatMemberEntity.ChatMember> implements TopMessageAdapter.OnMessageClickListener {
    public static final int ITEM_TOP = 100005;
    private List<MessageListEntity.Msg> msgList;
    private OnStatusClickListener mListener;
    private boolean canDelItem;

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

    public void setDelMode(boolean isCanDel) {
        canDelItem = isCanDel;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isEmpty(msgList)) {
            if (position == 0) {
                return ITEM_TOP;
            }
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
            messageAdapter.setOnMessageClickListener(this);
            topViewHolder.recycler_list.setAdapter(messageAdapter);
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
        try {
            long time = Long.valueOf(chatMember.update_time);
            if (time > 0) {
                messageViewHolder.tv_date.setText(getTime(time, "YYYY-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            if (!isEmpty(chatMember.update_time)) {
                messageViewHolder.tv_date.setText(chatMember.update_time);
            }
            e.printStackTrace();
        }
        GlideUtils.getInstance().loadCornerImage(context, messageViewHolder.miv_icon, chatMember.headurl, 5);
        if (chatMember.unread_count > 0) {
            messageViewHolder.tv_count.setVisibility(View.VISIBLE);
            messageViewHolder.tv_count.setText(Common.formatBadgeNumber(chatMember.unread_count));
        } else {
            messageViewHolder.tv_count.setVisibility(View.GONE);
        }
        messageViewHolder.tv_del.setOnClickListener((View v) -> {
            if (mListener != null) {
                mListener.OnMessageDel(chatMember.m_user_id);
            }
        });
        messageViewHolder.swipe_layout.setSwipeEnable(canDelItem);
    }


    @Override
    public void OnSysMsgClick() {
        if (mListener != null) {
            mListener.OnSysClick();
        }
    }

    @Override
    public void OnTopMsgClick() {
        if (mListener != null) {
            mListener.OnTopicClick();
        }
    }

    @Override
    public void OnAdminMsgClick() {
        if (mListener != null) {
            mListener.OnAdminClick();
        }
    }

    @Override
    public void OnSellerMsgClick() {
        if (mListener != null) {
            mListener.OnSellerClick();
        }
    }

    @Override
    public void OnOrderMsgClick() {
        if (mListener != null) {
            mListener.OnOrderClick();
        }
    }

    @Override
    public void OnStoreMsgClick() {
        if (mListener != null) {
            mListener.OnStoreMessageClick();
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

    public void setOnStatusClickListener(OnStatusClickListener listener) {
        this.mListener = listener;
    }

    public interface OnStatusClickListener {

        void OnSysClick();

        void OnTopicClick();

        void OnSellerClick();

        void OnAdminClick();

        void OnMessageDel(String userId);

        void OnStoreMessageClick();

        void OnOrderClick();
    }
}
