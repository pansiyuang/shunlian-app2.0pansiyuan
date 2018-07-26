package com.shunlian.app.newchat.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/18.
 */

public class PanelAdapter extends BaseRecyclerAdapter<String> {

    public PanelAdapter(Context context, List<String> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new PanelHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_panel, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        String str = lists.get(position);
        PanelHolder panelHolder = (PanelHolder) holder;
        panelHolder.tv_item.setText(str);
        switch (str) {
            case "相册":
                panelHolder.miv_icon.setImageResource(R.mipmap.img_chat_sendphoto);
                break;
            case "拍照":
                panelHolder.miv_icon.setImageResource(R.mipmap.img_chat_paizhao);
                break;
            case "商品":
                panelHolder.miv_icon.setImageResource(R.mipmap.img_chat_sendgoods);
                break;
            case "邀请评价":
                panelHolder.miv_icon.setImageResource(R.mipmap.img_chat_sendpingjia);
                break;
            case "订单":
                panelHolder.miv_icon.setImageResource(R.mipmap.img_chat_sendorder);
                break;
            case "快捷回复":
                panelHolder.miv_icon.setImageResource(R.mipmap.img_chat_kuaijie);
                break;
        }
    }

    public class PanelHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_item)
        TextView tv_item;

        public PanelHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }
    }
}
