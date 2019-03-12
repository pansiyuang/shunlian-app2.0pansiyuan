package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.MergeOrderEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

public class MergeGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    public MergeGoodsAdapter.OnGoodsBuyOnclickListener mListener;

    public MergeGoodsAdapter(Context context, List<GoodsDeatilEntity.Goods> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new GoodsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_meger_recommmedn, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsViewHolder) {
            GoodsDeatilEntity.Goods goods = lists.get(position);
            GoodsViewHolder viewHolder = (GoodsViewHolder) holder;
            GlideUtils.getInstance().loadImage(context, ((GoodsViewHolder) holder).mivMegerIcon, goods.thumb);
            ((GoodsViewHolder) holder).tvMegerTitle.setText(goods.title);

            String price = getString(R.string.common_yuan) + goods.price;
            viewHolder.tvMegerPrice.setText(Common.changeTextSize(price, getString(R.string.common_yuan), 12));

            int i = TransformUtil.dip2px(context, 20);
            TransformUtil.expandViewTouchDelegate(viewHolder.mivMegerBuy, i, i, i, i);
            viewHolder.mivMegerBuy.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.OnItemBuy(goods);
                }
            });
            viewHolder.tvMegerEarn.setText(goods.self_buy_earn);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mivMegerIcon.getLayoutParams();
            params.width = (DeviceInfoUtil.getDeviceWidth(context) - TransformUtil.dip2px(context, 5)) / 2;
            params.height = params.width;
            viewHolder.mivMegerIcon.setLayoutParams(params);
        }
    }

    public class GoodsViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_meger_icon)
        MyImageView mivMegerIcon;

        @BindView(R.id.tv_meger_title)
        TextView tvMegerTitle;

        @BindView(R.id.tv_meger_price)
        TextView tvMegerPrice;

        @BindView(R.id.tv_meger_earn)
        TextView tvMegerEarn;

        @BindView(R.id.miv_meger_buy)
        MyImageView mivMegerBuy;

        public GoodsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public void setOnGoodsBuyOnclickListener(MergeGoodsAdapter.OnGoodsBuyOnclickListener listener) {
        this.mListener = listener;
    }

    public interface OnGoodsBuyOnclickListener {
        void OnItemBuy(GoodsDeatilEntity.Goods goods);
    }
}
