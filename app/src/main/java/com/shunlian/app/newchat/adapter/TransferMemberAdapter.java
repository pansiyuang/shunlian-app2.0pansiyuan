package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/24.
 */

public class TransferMemberAdapter extends BaseRecyclerAdapter<ChatMemberEntity.ChatMember> {

    public TransferMemberAdapter(Context context, List<ChatMemberEntity.ChatMember> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TransferViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_switch_other, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        TransferViewHolder transferViewHolder = (TransferViewHolder) holder;
        ChatMemberEntity.ChatMember chatMember = lists.get(position);
        GlideUtils.getInstance().loadCornerImage(context, transferViewHolder.miv_icon, chatMember.headurl, 3);
        transferViewHolder.tv_name.setText(chatMember.nickname);
        transferViewHolder.tv_status.setText(chatMember.status_msg);

        if ("休息中".equals(chatMember.status_msg)) {
            transferViewHolder.miv_status.setImageResource(R.mipmap.img_chat_jiedaizhong);
        } else if ("接待中".equals(chatMember.status_msg)) {
            transferViewHolder.miv_status.setImageResource(R.mipmap.img_chat_xiuxizhong);
        }

        if (chatMember.isSelect) {
            transferViewHolder.miv_select.setBackgroundDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_h));
        } else {
            transferViewHolder.miv_select.setBackgroundDrawable(getDrawable(R.drawable.oval_stroke_pink));
        }
    }

    public class TransferViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.miv_status)
        MyImageView miv_status;

        @BindView(R.id.tv_status)
        TextView tv_status;

        public TransferViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
