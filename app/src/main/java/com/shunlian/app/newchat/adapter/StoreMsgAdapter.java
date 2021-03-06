package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/9.
 */

public class StoreMsgAdapter extends BaseRecyclerAdapter<StoreMsgEntity.StoreMsg> {
    private boolean isVip;
    private OnDelMessageListener mListener;

    public StoreMsgAdapter(Context context, List<StoreMsgEntity.StoreMsg> lists, boolean b) {
        super(context, true, lists);
        isVip = b;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new VipViewholder(LayoutInflater.from(context).inflate(R.layout.item_store_msg, parent, false));
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        VipViewholder vipViewholder = (VipViewholder) holder;
        StoreMsgEntity.StoreMsg storeMsg = lists.get(position);
        StoreMsgEntity.Body body = storeMsg.body;
        vipViewholder.tv_title.setText(body.title);
        GlideUtils.getInstance().loadImage(context, vipViewholder.miv_icon, body.avatar);
        vipViewholder.tv_name.setText(body.nickname);
        vipViewholder.tv_attention_date.setText(storeMsg.create_time);
        if (isVip) {
            vipViewholder.tv_id_number.setText("会员ID:" + body.member_id);
            vipViewholder.tv_attention_date.setText("关注时间:" + storeMsg.create_time);
        } else {
            vipViewholder.tv_id_number.setText(storeMsg.create_time);
            vipViewholder.tv_attention_date.setText("预计收益:" + getString(R.string.common_yuan) + body.money);
        }
        vipViewholder.tv_anonymous_price.setText("预计收益:" + getString(R.string.common_yuan) + body.money);

        if (body.anonymous == 1) {//匿名购买
            vipViewholder.ll_anonymous.setVisibility(View.VISIBLE);
            vipViewholder.rl_memberInfo.setVisibility(View.GONE);
        } else {
            vipViewholder.ll_anonymous.setVisibility(View.GONE);
            vipViewholder.rl_memberInfo.setVisibility(View.VISIBLE);
        }

        vipViewholder.tv_msg_del.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onDel(position);
            }
        });
    }

    public class VipViewholder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_id_number)
        TextView tv_id_number;

        @BindView(R.id.tv_attention_date)
        TextView tv_attention_date;

        @BindView(R.id.tv_msg_del)
        TextView tv_msg_del;

        @BindView(R.id.rl_memberInfo)
        RelativeLayout rl_memberInfo;

        @BindView(R.id.ll_anonymous)
        LinearLayout ll_anonymous;

        @BindView(R.id.tv_anonymous_price)
        TextView tv_anonymous_price;

        @BindView(R.id.rlayout_content)
        RelativeLayout rlayout_content;

        public VipViewholder(View itemView) {
            super(itemView);
            rlayout_content.setOnClickListener(v -> {
                if (listener != null){
                    listener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }

    public void setOnDelMessageListener(OnDelMessageListener l) {
        this.mListener = l;
    }

    public interface OnDelMessageListener {
        void onDel(int position);
    }
}
