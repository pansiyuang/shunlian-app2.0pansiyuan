package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.TurnTableEntity;
import com.shunlian.app.utils.LogUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/9/3.
 */

public class TurnTableAdapter extends BaseRecyclerAdapter<TurnTableEntity.MyPrize> {


    public TurnTableAdapter(Context context, List<TurnTableEntity.MyPrize> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new PrizeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_turntable, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PrizeViewHolder) {
            PrizeViewHolder prizeViewHolder = (PrizeViewHolder) holder;
            TurnTableEntity.MyPrize myPrize = lists.get(position);
            prizeViewHolder.tv_date.setText(myPrize.create_time);
            prizeViewHolder.tv_logistics_number.setText(myPrize.express_sn);
            prizeViewHolder.tv_turntable_name.setText(myPrize.trophy_name);
        }
    }

    public class PrizeViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_turntable_name)
        TextView tv_turntable_name;

        @BindView(R.id.tv_logistics_number)
        TextView tv_logistics_number;

        public PrizeViewHolder(View itemView) {
            super(itemView);
            itemView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                if (isFirst && mListener != null) {
                    int height = itemView.getHeight();
                    mListener.getItemHeight(height);
                    isFirst = false;
                }
            });
        }
    }

    boolean isFirst = true;
    OnItemGetHeightListener mListener;

    public void setOnItemGetHeightListener(OnItemGetHeightListener listener) {
        this.mListener = listener;
    }

    public interface OnItemGetHeightListener {
        void getItemHeight(int height);
    }
}
