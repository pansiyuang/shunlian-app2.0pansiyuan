package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CreditLogEntity;

import java.util.List;

import butterknife.BindView;

public class CreditlogAdapter extends BaseRecyclerAdapter<CreditLogEntity.CreditLog> {

    public CreditlogAdapter(Context context, List<CreditLogEntity.CreditLog> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new CreditLogViewHolder(LayoutInflater.from(context).inflate(R.layout.item_score_record, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CreditLogViewHolder) {
            CreditLogViewHolder mHolder = (CreditLogViewHolder) holder;
            CreditLogEntity.CreditLog creditLog = lists.get(position);
            mHolder.tv_use_type.setText(creditLog.use_road);
            mHolder.tv_date.setText(creditLog.create_time);
            mHolder.tv_count.setText(creditLog.num);
        }
    }

    public class CreditLogViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_use_type)
        TextView tv_use_type;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_count)
        TextView tv_count;

        public CreditLogViewHolder(View itemView) {
            super(itemView);
        }
    }
}
