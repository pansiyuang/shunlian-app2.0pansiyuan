package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.ProbablyLikeEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ProbablyLikeAdapter extends BaseRecyclerAdapter<ProbablyLikeEntity.MayBuyList> {
    private boolean isPlus = false;

    public ProbablyLikeAdapter(Context context, List<ProbablyLikeEntity.MayBuyList> lists, boolean isPlus) {
        super(context, false, lists);
        this.isPlus = isPlus;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ProbablyLikeHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_store_baby, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ProbablyLikeEntity.MayBuyList mayBuyList = lists.get(position);
        ProbablyLikeHolder mHolder = (ProbablyLikeHolder) holder;
        GlideUtils.getInstance().loadImage(context, mHolder.miv_onel, mayBuyList.thumb);
        mHolder.mtv_descl.setText(mayBuyList.title);
        mHolder.mtv_pricel.setText(mayBuyList.price);
        gone(mHolder.mrlayout_plus,mHolder.mtv_pricer);
        if (!isPlus) {
            mHolder.mtv_pricer.setStrikethrough()//市场价
                    .setText(getString(R.string.rmb) + mayBuyList.market_price);
            visible(mHolder.mtv_pricer);
        } else if (!isEmpty(mayBuyList.self_buy_earn)) {
            mHolder.mtv_plus_prefPrice.setText(getString(R.string.common_yuan) + mayBuyList.self_buy_earn);
            visible(mHolder.mrlayout_plus);
        }

    }

    public class ProbablyLikeHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_onel)
        MyImageView miv_onel;

        @BindView(R.id.mtv_descl)
        MyTextView mtv_descl;

        @BindView(R.id.mtv_pricel)
        MyTextView mtv_pricel;

        @BindView(R.id.mtv_pricer)
        MyTextView mtv_pricer;

        @BindView(R.id.mrlayout_plus)
        MyRelativeLayout mrlayout_plus;

        @BindView(R.id.mtv_plus_prefPrice)
        MyTextView mtv_plus_prefPrice;


        public ProbablyLikeHolder(View itemView) {
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
