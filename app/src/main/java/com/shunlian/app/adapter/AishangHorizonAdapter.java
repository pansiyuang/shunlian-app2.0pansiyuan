package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class AishangHorizonAdapter extends BaseRecyclerAdapter<CoreNewEntity.MData> {

    public AishangHorizonAdapter(Context context, boolean isShowFooter,
                                 List<CoreNewEntity.MData> lists) {
        super(context, isShowFooter, lists);

    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_first_goods, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        CoreNewEntity.MData data = lists.get(position);
        SpannableStringBuilder priceBuilder = Common.changeTextSize(getString(R.string.common_yuan) + data.price,
                getString(R.string.common_yuan), 11);
        mHolder.mtv_price.setText(priceBuilder);
        mHolder.mtv_title.setText(data.title);
        mHolder.mtv_title.setVisibility(View.VISIBLE);
        mHolder.mtv_earn_price.setEarnMoney(data.self_buy_earn,11);

        GlideUtils.getInstance().loadImage(context, mHolder.miv_photo, data.thumb);
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_earn_price)
        MyTextView mtv_earn_price;

        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        public ActivityMoreHolder(View itemView) {
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
