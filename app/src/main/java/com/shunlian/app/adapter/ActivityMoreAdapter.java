package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class ActivityMoreAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.ActivityDetail> {


    private int fullCut;
    private int fullDiscount;
    private int buyGift;

    public ActivityMoreAdapter(Context context, boolean isShowFooter,
                               List<GoodsDeatilEntity.ActivityDetail> lists,
                               int fullCut, int fullDiscount, int buyGift) {
        super(context, isShowFooter, lists);
        this.fullCut = fullCut;
        this.fullDiscount = fullDiscount;
        this.buyGift = buyGift;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_activity, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        GoodsDeatilEntity.ActivityDetail ad = lists.get(position);
        if (position < fullCut){
            if (position == 0){
                mHolder.mtv_title.setVisibility(View.VISIBLE);
                mHolder.mtv_title.setText(String.format(getString(R.string.full_cut),"",""));
            }else {
                mHolder.mtv_title.setVisibility(View.INVISIBLE);
            }
            mHolder.mtv_content.setText(String.format(getString(R.string.full_cut),ad.money_type_condition,ad.money_type_discount));
        }else if (position < fullCut + fullDiscount){
            if (position == fullCut){
                mHolder.mtv_title.setVisibility(View.VISIBLE);
                mHolder.mtv_title.setText(String.format(getString(R.string.full_discount)));
            }else {
                mHolder.mtv_title.setVisibility(View.INVISIBLE);
            }
            mHolder.mtv_content.setText(String.format(getString(R.string.full_discount_),ad.qty_type_condition,ad.qty_type_discount));
        }else {
            if (position == fullCut + fullDiscount){
                mHolder.mtv_title.setVisibility(View.VISIBLE);
                mHolder.mtv_title.setText(String.format(getString(R.string.buy_gift)));
            }else {
                mHolder.mtv_title.setVisibility(View.INVISIBLE);
            }
            mHolder.mtv_content.setText(ad.promotion_title);
        }
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        public ActivityMoreHolder(View itemView) {
            super(itemView);
            GradientDrawable background = (GradientDrawable) mtv_title.getBackground();
            background.setColor(getColor(R.color.value_FEF0F3));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
