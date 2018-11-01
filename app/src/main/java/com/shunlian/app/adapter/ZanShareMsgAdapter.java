package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.ZanShareEntity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
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
            GlideUtils.getInstance().loadCircleAvar(context, zanViewHolder.miv_icon, msg.avatar);
            GlideUtils.getInstance().loadImage(context, zanViewHolder.miv_goods_icon, msg.blog.pic);
            zanViewHolder.tv_nickname.setText(msg.nickname);
            zanViewHolder.tv_date.setText(msg.create_time);
            if (msg.type == 119) {//点赞
                zanViewHolder.miv_status.setImageResource(R.mipmap.icon_faxian_dainzan_hong);
                zanViewHolder.tv_content.setText("点赞了这条心得");
            } else if (msg.type == 120) {//分享
                zanViewHolder.miv_status.setImageResource(R.mipmap.icon_home_share);
                zanViewHolder.tv_content.setText("分享了这条心得");
            }
            if (msg.blog.media == 2) {//视频
                zanViewHolder.miv_video.setVisibility(View.VISIBLE);
            } else {
                zanViewHolder.miv_video.setVisibility(View.GONE);
            }
            zanViewHolder.tv_goods_title.setText(msg.blog.title);
            zanViewHolder.miv_icon.setOnClickListener(v -> MyPageActivity.startAct(context,msg.member_id));
            zanViewHolder.ll_member.setOnClickListener(v -> MyPageActivity.startAct(context,msg.member_id));
        }
    }

    public class ZanViewHolder extends BaseRecyclerViewHolder {

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

        public ZanViewHolder(View itemView) {
            super(itemView);
        }
    }
}
