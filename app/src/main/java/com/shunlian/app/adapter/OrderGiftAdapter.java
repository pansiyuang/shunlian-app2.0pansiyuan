package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 */

public class OrderGiftAdapter extends BaseRecyclerAdapter<OrderdetailEntity.Good.Gift> {

    public OrderGiftAdapter(Context context, List<OrderdetailEntity.Good.Gift> lists) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_gift_order, parent, false);
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
        OrderdetailEntity.Good.Gift orderGoodsBean = lists.get(position);
        GlideUtils.getInstance().loadImage(context,mHolder.miv_gift,orderGoodsBean.thumb);
        mHolder.mtv_gift.setText(orderGoodsBean.title);
    }

    public class OrderGoodsHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.miv_gift)
        MyImageView miv_gift;

        @BindView(R.id.mtv_gift)
        MyTextView mtv_gift;

        public OrderGoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
