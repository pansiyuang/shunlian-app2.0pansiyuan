package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ZanShareEntity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/25.
 */

public class DownloadMsgAdapter extends BaseRecyclerAdapter<ZanShareEntity.Msg> {

    public DownloadMsgAdapter(Context context, List<ZanShareEntity.Msg> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new DownViewHolder(LayoutInflater.from(context).inflate(R.layout.item_zan_share, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DownViewHolder) {
            DownViewHolder downViewHolder = (DownViewHolder) holder;
            ZanShareEntity.Msg msg = lists.get(position);
            GlideUtils.getInstance().loadCircleAvar(context, downViewHolder.miv_icon, msg.avatar);
            GlideUtils.getInstance().loadImage(context, downViewHolder.miv_goods_icon, msg.blog.pic);
            downViewHolder.tv_nickname.setText(msg.nickname);
            downViewHolder.tv_date.setText(msg.create_time);

            downViewHolder.miv_status.setVisibility(View.GONE);
            downViewHolder.tv_content.setText("下载了这条心得");

            if (msg.blog.media == 2) {//视频
                downViewHolder.miv_video.setVisibility(View.VISIBLE);
            } else {
                downViewHolder.miv_video.setVisibility(View.GONE);
            }
            downViewHolder.tv_goods_title.setText(msg.blog.title);
            downViewHolder.ll_member.setOnClickListener(v -> MyPageActivity.startAct(context, msg.member_id));
            downViewHolder.miv_icon.setOnClickListener(v -> MyPageActivity.startAct(context, msg.member_id));
        }
    }

    public class DownViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_video)
        MyImageView miv_video;

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

        @BindView(R.id.ll_member)
        LinearLayout ll_member;

        public DownViewHolder(View itemView) {
            super(itemView);
        }
    }
}
