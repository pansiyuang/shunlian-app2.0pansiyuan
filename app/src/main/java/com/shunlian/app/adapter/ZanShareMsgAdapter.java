package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.ZanShareEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class ZanShareMsgAdapter extends BaseRecyclerAdapter<ZanShareEntity.Msg> {

    public ZanShareMsgAdapter(Context context, List<ZanShareEntity.Msg> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ZanViewHolder(LayoutInflater.from(context).inflate(R.layout.item_zan_share, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ZanViewHolder) {
            ZanViewHolder zanViewHolder = (ZanViewHolder) holder;
            ZanShareEntity.Msg msg = lists.get(position);
            GlideUtils.getInstance().loadCircleImage(context, zanViewHolder.miv_icon, msg.avatar);
            zanViewHolder.tv_nickname.setText(msg.nickname);
            zanViewHolder.tv_date.setText(msg.create_time);
        }
    }

    public class ZanViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_nickname)
        TextView tv_nickname;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.miv_status)
        MyImageView miv_status;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.miv_goods_icon)
        MyImageView miv_goods_icon;

        @BindView(R.id.tv_goods_title)
        TextView tv_goods_title;

        public ZanViewHolder(View itemView) {
            super(itemView);
        }
    }
}
