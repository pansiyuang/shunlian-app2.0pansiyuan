package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.RecommendEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/15.
 */

public class RecommendAdapter extends BaseRecyclerAdapter {

    public RecommendAdapter(Context context, List lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new RecommendHolderView(LayoutInflater.from(context).inflate(R.layout.item_first_more, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        RecommendEntity.Goods goods = (RecommendEntity.Goods) lists.get(position);
        RecommendHolderView recommendHolderView = (RecommendHolderView) holder;
        GlideUtils.getInstance().loadImage(context, recommendHolderView.miv_photo, goods.thumb);
        recommendHolderView.mtv_price.setText(getString(R.string.common_yuan) + goods.price);
        recommendHolderView.mtv_title.setText(goods.title);
        if (!isEmpty(goods.self_buy_earn)) {
            recommendHolderView.mtv_earn.setVisibility(View.VISIBLE);
            recommendHolderView.mtv_earn.setEarnMoney(goods.self_buy_earn, 15);
        } else {
            recommendHolderView.mtv_earn.setVisibility(View.GONE);
        }
    }

    public class RecommendHolderView extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mllayout_tag)
        MyLinearLayout mllayout_tag;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_earn)
        MyTextView mtv_earn;

        public RecommendHolderView(View itemView) {
            super(itemView);
            mllayout_tag.setVisibility(View.GONE);
            itemView.setOnClickListener(view -> listener.onItemClick(view, getAdapterPosition()));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TransformUtil.dip2px(context, 185));
            miv_photo.setLayoutParams(layoutParams);
        }
    }
}
