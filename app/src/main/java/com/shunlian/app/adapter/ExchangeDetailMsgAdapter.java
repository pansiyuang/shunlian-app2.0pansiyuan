package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class ExchangeDetailMsgAdapter extends BaseRecyclerAdapter<RefundDetailEntity.RefundDetail.Msg> {


    public ExchangeDetailMsgAdapter(Context context, boolean isShowFooter,List<RefundDetailEntity.RefundDetail.Msg> msgs) {
        super(context, isShowFooter,msgs);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new MsgHolder(LayoutInflater.from(context).inflate(R.layout.item_exchange_detail_msg, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        MsgHolder mHolder = (MsgHolder) holder;
        RefundDetailEntity.RefundDetail.Msg msg=lists.get(position);
        mHolder.mtv_desc.setText(msg.description);
        mHolder.mtv_label.setText(msg.label);
        mHolder.mtv_title.setText(msg.title);
    }

    public class MsgHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_label)
        MyTextView mtv_label;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        public MsgHolder(View itemView) {
            super(itemView);
        }

    }
}
