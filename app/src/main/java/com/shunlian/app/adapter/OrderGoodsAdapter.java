package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 */

public class OrderGoodsAdapter extends BaseRecyclerAdapter<MyOrderEntity.OrderGoodsBean> {

    public OrderGoodsAdapter(Context context, List<MyOrderEntity.OrderGoodsBean> lists) {
        super(context, false, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_order, parent, false);
        return new OrderGoodsHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        OrderGoodsHolder mHolder = (OrderGoodsHolder) holder;
        MyOrderEntity.OrderGoodsBean orderGoodsBean = lists.get(position);
        GlideUtils.getInstance().loadOverrideImage(context,
                mHolder.miv_goods_pic,orderGoodsBean.thumb,160,160);
        mHolder.mtv_attribute.setText(orderGoodsBean.sku_desc);
        mHolder.mtv_price.setText(getString(R.string.rmb)+orderGoodsBean.price);
        mHolder.mtv_market_price.setStrikethrough().setText(getString(R.string.rmb)+orderGoodsBean.market_price);
        mHolder.mtv_count.setText(String.format(getString(R.string.x),orderGoodsBean.qty));
        String offered = orderGoodsBean.offered;
        if (isEmpty(offered)){
            mHolder.mtv_label.setVisibility(View.GONE);
            mHolder.mtv_title.setText(orderGoodsBean.title);
        }else {
            mHolder.mtv_label.setVisibility(View.VISIBLE);
            GradientDrawable background = (GradientDrawable) mHolder.mtv_label.getBackground();
            background.setColor(getColor(R.color.pink_color));
            mHolder.mtv_label.setText(offered);
            mHolder.mtv_title.setText(Common.getPlaceholder(offered.length())
                    .concat(orderGoodsBean.title));
        }

        String is_refund = orderGoodsBean.is_refund;
        if (isEmpty(is_refund)){
            mHolder.mtv_refund_status.setVisibility(View.GONE);
        }else {
            mHolder.mtv_refund_status.setVisibility(View.VISIBLE);
            mHolder.mtv_refund_status.setText(is_refund);
        }

    }

    public class OrderGoodsHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_goods_pic)
        MyImageView miv_goods_pic;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_attribute)
        MyTextView mtv_attribute;

        @BindView(R.id.mtv_market_price)
        MyTextView mtv_market_price;

        @BindView(R.id.mtv_count)
        MyTextView mtv_count;

        @BindView(R.id.mtv_label)
        MyTextView mtv_label;

        @BindView(R.id.mtv_refund_status)
        MyTextView mtv_refund_status;

        public OrderGoodsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
