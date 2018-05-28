package com.shunlian.app.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.PlusOrderEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/28.
 */

public class PlusOrderAdapter extends BaseRecyclerAdapter<PlusOrderEntity.PlusOrder> {

    public PlusOrderAdapter(Context context, List<PlusOrderEntity.PlusOrder> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_plus_order, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        PlusOrderEntity.PlusOrder plusOrder = lists.get(position);
        OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
        GlideUtils.getInstance().loadImage(context, orderViewHolder.miv_icon, plusOrder.thumb);
        orderViewHolder.tv_title.setText(plusOrder.title);
        String price = getString(R.string.common_yuan) + plusOrder.price;
        orderViewHolder.tv_price.setText(price);
        Common.changeTextSize(price, getString(R.string.common_yuan), 19);
        orderViewHolder.tv_param.setText(plusOrder.sku_desc);
        orderViewHolder.tv_count.setText(String.valueOf(plusOrder.qty));
        orderViewHolder.tv_total_count.setText("共计" + plusOrder.qty + "件商品");
        try {
            double shippingFee = Double.valueOf(plusOrder.shipping_fee);
            if (shippingFee > 0) {
                orderViewHolder.tv_total_price.setText("合计:" + price + "(包邮)");
            } else {
                orderViewHolder.tv_total_price.setText("合计:" + price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (plusOrder.status) {
            case 1: //已发货
                orderViewHolder.miv_status.setImageDrawable(getDrawable(R.mipmap.img_plus_yifahuo));
                orderViewHolder.miv_status.setVisibility(View.VISIBLE);
                break;
            case 2: //待收货
                orderViewHolder.miv_status.setImageDrawable(getDrawable(R.mipmap.img_plus_yifukuan));
                orderViewHolder.miv_status.setVisibility(View.VISIBLE);
                break;
            default:
                orderViewHolder.miv_status.setVisibility(View.GONE);
                break;
        }
    }

    public class OrderViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_param)
        TextView tv_param;

        @BindView(R.id.tv_count)
        TextView tv_count;

        @BindView(R.id.miv_status)
        MyImageView miv_status;

        @BindView(R.id.tv_total_count)
        TextView tv_total_count;

        @BindView(R.id.tv_total_price)
        TextView tv_total_price;

        public OrderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
