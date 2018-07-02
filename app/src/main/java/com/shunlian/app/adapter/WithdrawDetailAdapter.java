package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.BalanceDetailEntity;
import com.shunlian.app.bean.WithdrawListEntity;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class WithdrawDetailAdapter extends BaseRecyclerAdapter<WithdrawListEntity.Record> {

    public WithdrawDetailAdapter(Context context, boolean isShowFooter,
                                 List<WithdrawListEntity.Record> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_balance_detail, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        WithdrawListEntity.Record ad = lists.get(position);
        if (position%2==0){
            mHolder.mrlayout_root.setBackgroundColor(getColor(R.color.white));
        }else {
            mHolder.mrlayout_root.setBackgroundColor(getColor(R.color.value_FBFBFB));
        }
        if (ad.is_fail){
            mHolder.mtv_amount.setTextColor(getColor(R.color.pink_color));
            mHolder.mtv_title.setTextColor(getColor(R.color.pink_color));
            mHolder.mtv_title.setText(ad.title);
        }else {
            mHolder.mtv_title.setTextColor(getColor(R.color.new_text));
            mHolder.mtv_title.setText(ad.title);
            mHolder.mtv_amount.setTextColor(getColor(R.color.new_text));
        }
        mHolder.mtv_amount.setText(ad.amount);
        mHolder.mtv_time.setText(ad.create_time);

    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.mtv_amount)
        MyTextView mtv_amount;

        @BindView(R.id.mrlayout_root)
        MyRelativeLayout mrlayout_root;

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
