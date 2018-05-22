package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/13.
 */

public class ChatOrderAdapter extends BaseRecyclerAdapter<MyOrderEntity.Orders> {
    private List<MyOrderEntity.Orders> mOrders;

    public ChatOrderAdapter(Context context, List<MyOrderEntity.Orders> lists) {
        super(context, true, lists);
        this.mOrders = lists;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_order, parent, false));
    }

    public void setData(List<MyOrderEntity.Orders> ordersList) {
        this.mOrders = ordersList;
        notifyDataSetChanged();
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
        OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
        MyOrderEntity.Orders order = mOrders.get(position);
        List<MyOrderEntity.OrderGoodsBean> orderGoodsBeans = order.order_goods;
        if (!isEmpty(orderGoodsBeans)) {
            MyOrderEntity.OrderGoodsBean goodsBean = orderGoodsBeans.get(0);
            GlideUtils.getInstance().loadImage(context, orderViewHolder.miv_icon, goodsBean.thumb);
            orderViewHolder.tv_order_title.setText(goodsBean.title);
            orderViewHolder.tv_order_param.setText(goodsBean.sku_desc);
            orderViewHolder.tv_order_price.setText(getString(R.string.common_yuan) + " " + goodsBean.price);
            orderViewHolder.tv_order_status.setText(order.status_text);
            orderViewHolder.tv_order_count.setText("x" + goodsBean.qty);
        }
    }

    public class OrderViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_order_title)
        TextView tv_order_title;

        @BindView(R.id.tv_order_param)
        TextView tv_order_param;

        @BindView(R.id.tv_order_status)
        TextView tv_order_status;

        @BindView(R.id.tv_order_price)
        TextView tv_order_price;

        @BindView(R.id.tv_order_count)
        TextView tv_order_count;

        @BindView(R.id.tv_order_send)
        TextView tv_order_send;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tv_order_send.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
