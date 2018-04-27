package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.newchat.entity.MessageListEntity;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SwipeMenuLayout;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/11.
 */

public class TopMessageAdapter extends BaseRecyclerAdapter<MessageListEntity.Msg> {
    private boolean isLoad;
    private MessageCountManager manager;
    private OnMessageClickListener messageClickListener;

    public TopMessageAdapter(Context context, List<MessageListEntity.Msg> lists) {
        super(context, false, lists);
        manager = MessageCountManager.getInstance(context);
        isLoad = manager.isLoad();
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        MessageListEntity.Msg msg = lists.get(position);
        viewHolder.swipe_layout.setSwipeEnable(false);
        switch (msg.type) {
            case "1": //平台消息
                GlideUtils.getInstance().loadLocalImageWithView(context, R.mipmap.img_kefu, viewHolder.miv_icon);
                viewHolder.tv_name.setText(getString(R.string.platform_message));
                viewHolder.tv_official.setVisibility(View.GONE);
                if (isLoad) {
                    String custom_msg = Common.formatBadgeNumber(manager.getCustom_msg());
                    if (isEmpty(custom_msg)) {
                        viewHolder.tv_count.setVisibility(View.GONE);
                    } else {
                        viewHolder.tv_count.setText(custom_msg);
                        viewHolder.tv_count.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case "2": //客服消息
                GlideUtils.getInstance().loadLocalImageWithView(context, R.mipmap.img_kefu, viewHolder.miv_icon);
                viewHolder.tv_name.setText(getString(R.string.custom_message));
                viewHolder.tv_official.setVisibility(View.GONE);
                if (isLoad) {
                    String custom_msg = Common.formatBadgeNumber(manager.getCustom_msg());
                    if (isEmpty(custom_msg)) {
                        viewHolder.tv_count.setVisibility(View.GONE);
                    } else {
                        viewHolder.tv_count.setText(custom_msg);
                        viewHolder.tv_count.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case "4": //系统消息
                GlideUtils.getInstance().loadLocalImageWithView(context, R.mipmap.img_xitong, viewHolder.miv_icon);
                viewHolder.tv_name.setText(getString(R.string.sys_notice));
                viewHolder.tv_official.setVisibility(View.VISIBLE);
                if (isLoad) {
                    String sys_notice_msg = Common.formatBadgeNumber(manager.getSys_notice_msg());
                    if (isEmpty(sys_notice_msg)) {
                        viewHolder.tv_count.setVisibility(View.GONE);
                    } else {
                        viewHolder.tv_count.setText(sys_notice_msg);
                        viewHolder.tv_count.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case "5": //头条消息
                GlideUtils.getInstance().loadLocalImageWithView(context, R.mipmap.img_toutiao, viewHolder.miv_icon);
                viewHolder.tv_name.setText(getString(R.string.topic));
                viewHolder.tv_official.setVisibility(View.VISIBLE);
                if (isLoad) {
                    String discover_topic_msg = Common.formatBadgeNumber(manager.getDiscover_topic_msg());
                    if (isEmpty(discover_topic_msg)) {
                        viewHolder.tv_count.setVisibility(View.GONE);
                    } else {
                        viewHolder.tv_count.setText(discover_topic_msg);
                        viewHolder.tv_count.setVisibility(View.VISIBLE);
                    }
                }
                break;
            default:
                break;
        }
        viewHolder.tv_content.setText(msg.title);
        viewHolder.tv_date.setText(msg.date);

        viewHolder.ll_item.setOnClickListener(v -> {
            switch (msg.type) {
                case "1":
                    if (messageClickListener != null) {
                        messageClickListener.OnAdminMsgClick();
                    }
                    break;
                case "2":
                    if (messageClickListener != null) {
                        messageClickListener.OnSellerMsgClick();
                    }
                    break;
                case "4":
                    if (messageClickListener != null) {
                        messageClickListener.OnSysMsgClick();
                    }
                    break;
                case "5":
                    if (messageClickListener != null) {
                        messageClickListener.OnTopMsgClick();
                    }
                    break;
            }
        });
    }

    public class ItemViewHolder extends BaseRecyclerViewHolder {
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

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnMessageClickListener(OnMessageClickListener listener) {
        this.messageClickListener = listener;
    }

    public interface OnMessageClickListener {
        void OnSysMsgClick();

        void OnTopMsgClick();

        void OnAdminMsgClick();

        void OnSellerMsgClick();
    }
}
