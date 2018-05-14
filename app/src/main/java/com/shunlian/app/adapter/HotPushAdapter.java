package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.HotRdEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class HotPushAdapter extends BaseRecyclerAdapter<HotRdEntity.MData> {

    private LayoutInflater mInflater;

    public HotPushAdapter(Context context, List<HotRdEntity.MData> lists) {
        super(context, true, lists);
        mInflater = LayoutInflater.from(context);
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
        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_goods));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new SingleViewHolder(mInflater.inflate(R.layout.item_hot_push, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SingleViewHolder) {
            SingleViewHolder viewHolder = (SingleViewHolder) holder;
            HotRdEntity.MData goods = lists.get(position);
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_photo, goods.thumb);
            viewHolder.mtv_title.setText(goods.description);
            SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.common_yuan) + goods.price, getString(R.string.common_yuan), 12);
            viewHolder.mtv_price.setText(spannableStringBuilder);
            viewHolder.mtv_marketPrice.setText(goods.market_price);
            viewHolder.mtv_marketPrice.setStrikethrough();
        }
    }


    public class SingleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_marketPrice)
        MyTextView mtv_marketPrice;

        public SingleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
