package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.MyOrderEntity;
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
        GlideUtils.getInstance().loadImage(context,mHolder.miv_goods_pic,orderGoodsBean.thumb);
        mHolder.mtv_title.setText(orderGoodsBean.title);
        mHolder.mtv_attribute.setText(orderGoodsBean.sku_desc);
        mHolder.mtv_price.setText(getString(R.string.rmb)+orderGoodsBean.price);
        mHolder.mtv_market_price.setStrikethrough().setText(getString(R.string.rmb)+orderGoodsBean.market_price);
        mHolder.mtv_count.setText(String.format(getString(R.string.x),orderGoodsBean.qty));
    }

    public class OrderGoodsHolder extends BaseRecyclerViewHolder{

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


        public OrderGoodsHolder(View itemView) {
            super(itemView);
        }
    }
}