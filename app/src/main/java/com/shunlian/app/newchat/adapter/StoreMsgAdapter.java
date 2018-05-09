package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public StoreMsgAdapter(Context context, List<StoreMsgEntity.StoreMsg> lists) {
        super(context, true, lists);
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
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        VipViewholder vipViewholder = (VipViewholder) holder;
        StoreMsgEntity.StoreMsg storeMsg = lists.get(position);
        StoreMsgEntity.Content content = storeMsg.content;
        vipViewholder.tv_title.setText(content.title);
        GlideUtils.getInstance().loadImage(context, vipViewholder.miv_icon, storeMsg.url);
        vipViewholder.tv_name.setText(content.username);
        vipViewholder.tv_id_number.setText(content.sl_id);
        vipViewholder.tv_attention_date.setText(content.create_time);
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

        public VipViewholder(View itemView) {
            super(itemView);
        }
    }
}
