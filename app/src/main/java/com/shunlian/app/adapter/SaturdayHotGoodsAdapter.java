package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.TurnTableEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

public class SaturdayHotGoodsAdapter extends BaseRecyclerAdapter<TurnTableEntity.HotGoods> {

    public SaturdayHotGoodsAdapter(Context context, List<TurnTableEntity.HotGoods> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new GoodsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hot_goods, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsViewHolder) {
            GoodsViewHolder viewHolder = (GoodsViewHolder) holder;
            TurnTableEntity.HotGoods hotGoods = lists.get(position);
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, hotGoods.thumb);
            viewHolder.tv_goods_name.setText(hotGoods.title);
            viewHolder.tv_goods_price.setText(getString(R.string.common_yuan) + hotGoods.price);
            viewHolder.tv_goods_earn.setText(getString(R.string.common_yuan) + hotGoods.self_buy_earn);
        }
    }

    public class GoodsViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_goods_name)
        TextView tv_goods_name;

        @BindView(R.id.tv_goods_price)
        TextView tv_goods_price;

        @BindView(R.id.tv_goods_earn)
        TextView tv_goods_earn;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            int picWidth = (Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 55)) / 2;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(picWidth, picWidth);
            miv_icon.setLayoutParams(params);
        }
    }
}
