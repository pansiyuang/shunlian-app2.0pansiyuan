package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.order.ExchangeDetailAct;
import com.shunlian.app.ui.plus.PlusGifDetailAct;
import com.shunlian.app.ui.plus.PlusLogisticsDetailAct;
import com.shunlian.app.ui.returns_order.SelectServiceActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 */

public class OrderGoodAdapter extends BaseRecyclerAdapter<OrderdetailEntity.Good> {

    public OrderGoodAdapter(Context context, List<OrderdetailEntity.Good> lists) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_good_order, parent, false);
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
        final OrderdetailEntity.Good orderGoodsBean = lists.get(position);
        GlideUtils.getInstance().loadOverrideImage(context,
                mHolder.miv_goods_pic,orderGoodsBean.thumb,160,160);
        mHolder.mtv_title.setText(orderGoodsBean.title);
        mHolder.mtv_attribute.setText(orderGoodsBean.sku_desc);
        mHolder.mtv_price.setText(getString(R.string.rmb)+orderGoodsBean.price);
        if (!isEmpty(orderGoodsBean.market_price)&&Float.parseFloat(orderGoodsBean.market_price)>0){
            mHolder.mtv_market_price.setVisibility(View.VISIBLE);
            mHolder.mtv_market_price.setStrikethrough().setText(getString(R.string.rmb)+orderGoodsBean.market_price);
        }else {
            mHolder.mtv_market_price.setVisibility(View.GONE);
        }

        mHolder.mtv_count.setText(String.format(getString(R.string.x),orderGoodsBean.qty));
        mHolder.mrlayout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(orderGoodsBean.product_id)){
                    PlusGifDetailAct.startAct(context,orderGoodsBean.product_id);
                }else {
                    GoodsDetailAct.startAct(context,orderGoodsBean.goods_id);
                }
            }
        });
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
        if (!TextUtils.isEmpty(orderGoodsBean.refund_button_desc)){
            mHolder.mtv_refund.setText(orderGoodsBean.refund_button_desc);
            mHolder.mtv_refund.setVisibility(View.VISIBLE);
        }else {
            mHolder.mtv_refund.setVisibility(View.GONE);
        }
        mHolder.mtv_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("apply".equals(orderGoodsBean.refund_button_type)){
                    SelectServiceActivity.startAct(context, orderGoodsBean.og_id);
                }else {
                    ExchangeDetailAct.startAct(context,orderGoodsBean.refund_id);
                }
            }
        });
        GradientDrawable copyBackground = (GradientDrawable) mHolder.mtv_refund.getBackground();
        copyBackground.setColor(getColor(R.color.white));
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mHolder.rv_goods.setLayoutManager(manager);
        mHolder.rv_goods.setAdapter(new OrderGiftAdapter(context, orderGoodsBean.gift));
        mHolder.rv_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });
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

        @BindView(R.id.rv_goods)
        RecyclerView rv_goods;

        @BindView(R.id.mrlayout_root)
        MyRelativeLayout mrlayout_root;

        @BindView(R.id.mtv_label)
        MyTextView mtv_label;

        @BindView(R.id.mtv_refund)
        MyTextView mtv_refund;

        public OrderGoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
