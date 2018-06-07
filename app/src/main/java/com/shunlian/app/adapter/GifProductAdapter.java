package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GifProductEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/25.
 */

public class GifProductAdapter extends BaseRecyclerAdapter<GifProductEntity.Product> {

    public GifProductAdapter(Context context, List<GifProductEntity.Product> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_gif, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ProductViewHolder viewHolder = (ProductViewHolder) holder;
        GifProductEntity.Product product = lists.get(position);
        GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, product.thumb);
        viewHolder.tv_product_title.setText(product.title);
        viewHolder.tv_price.setText(getString(R.string.common_yuan) + product.price);
        viewHolder.tv_market_price.setText(getString(R.string.common_yuan) + product.market_price);
        viewHolder.tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if (product.sell_out == 1) {  //是否售罄，1售罄  0未售罄
            viewHolder.miv_sold_out.setVisibility(View.VISIBLE);
            viewHolder.tv_buy.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_solid_e5_8px));
            viewHolder.tv_buy.setTextColor(getColor(R.color.value_A0A0A0));
            viewHolder.tv_buy.setText(getString(R.string.seller_out));
            viewHolder.tv_price.setTextColor(getColor(R.color.value_A0A0A0));
        } else {
            viewHolder.miv_sold_out.setVisibility(View.GONE);
            viewHolder.tv_buy.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_solid_pink_8px));
            viewHolder.tv_buy.setTextColor(getColor(R.color.white));
            viewHolder.tv_price.setTextColor(getColor(R.color.pink_color));
            viewHolder.tv_buy.setText(getString(R.string.to_buy));
        }
    }

    public class ProductViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_sold_out)
        MyImageView miv_sold_out;

        @BindView(R.id.tv_product_title)
        TextView tv_product_title;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_market_price)
        TextView tv_market_price;

        @BindView(R.id.tv_buy)
        TextView tv_buy;

        public ProductViewHolder(View itemView) {
            super(itemView);
        }
    }
}
