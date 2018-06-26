package com.shunlian.app.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.PlusOrderEntity;
import com.shunlian.app.ui.confirm_order.OrderLogisticsActivity;
import com.shunlian.app.ui.plus.PlusLogisticsDetailAct;
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
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_plus_order, parent, false));
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_order));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
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
        orderViewHolder.tv_count.setText("x" + plusOrder.qty);
        orderViewHolder.tv_total_count.setText("共计" + plusOrder.qty + "件商品");

        if ("0".equals(plusOrder.shipping_fee)) {
            orderViewHolder.tv_total_price.setText("合计:" + price + "  (包邮)");
        } else {
            orderViewHolder.tv_total_price.setText("合计:" + price);
        }
        switch (plusOrder.status) {
            case 1: //待发货
                orderViewHolder.miv_status.setImageDrawable(getDrawable(R.mipmap.img_plus_yifukuan));
                orderViewHolder.miv_status.setVisibility(View.VISIBLE);
                orderViewHolder.rl_logistics.setVisibility(View.GONE);
                break;
            case 2: //已发货
                orderViewHolder.miv_status.setImageDrawable(getDrawable(R.mipmap.img_plus_yifahuo));
                orderViewHolder.miv_status.setVisibility(View.VISIBLE);
                orderViewHolder.rl_logistics.setVisibility(View.VISIBLE);
                break;
            default:
                orderViewHolder.miv_status.setVisibility(View.GONE);
                orderViewHolder.rl_logistics.setVisibility(View.GONE);
                break;
        }
        orderViewHolder.tv_logistics.setOnClickListener(v -> {
            //查看物流信息
            OrderLogisticsActivity.startAct(context, plusOrder.product_order_id,"4");
        });
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

        @BindView(R.id.rl_logistics)
        RelativeLayout rl_logistics;

        @BindView(R.id.tv_logistics)
        TextView tv_logistics;

        public OrderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
