package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/11.
 */

public class CommentMsgAdapter extends BaseRecyclerAdapter<StoreMsgEntity.StoreMsg> {

    public CommentMsgAdapter(Context context, List<StoreMsgEntity.StoreMsg> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new CommentMsgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_msg, parent, false));
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
        CommentMsgViewHolder viewHolder = (CommentMsgViewHolder) holder;
        StoreMsgEntity.StoreMsg storeMsg = lists.get(position);
        GlideUtils.getInstance().loadCircleImage(context, viewHolder.miv_icon, storeMsg.url);
    }

    public class CommentMsgViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_date)
        TextView tv_date;

        public CommentMsgViewHolder(View itemView) {
            super(itemView);
        }
    }
}
