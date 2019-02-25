package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.SaturdayDrawRecordEntity;

import java.util.List;

import butterknife.BindView;

public class SaturdayDrawRecordAdapter extends BaseRecyclerAdapter<SaturdayDrawRecordEntity.SaturdayDrawRecord> {

    public SaturdayDrawRecordAdapter(Context context, List<SaturdayDrawRecordEntity.SaturdayDrawRecord> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new DrawRecordViewHolder(LayoutInflater.from(context).inflate(R.layout.item_draw_record, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DrawRecordViewHolder) {
            SaturdayDrawRecordEntity.SaturdayDrawRecord saturdayDrawRecord = lists.get(position);
            DrawRecordViewHolder viewHolder = (DrawRecordViewHolder) holder;
            viewHolder.tv_draw_date.setText(saturdayDrawRecord.create_time);
            viewHolder.tv_draw_name.setText(saturdayDrawRecord.trophy_name);
            viewHolder.tv_draw_express.setText(saturdayDrawRecord.express_sn);
        }
    }

    public class DrawRecordViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_draw_date)
        TextView tv_draw_date;

        @BindView(R.id.tv_draw_name)
        TextView tv_draw_name;

        @BindView(R.id.tv_draw_express)
        TextView tv_draw_express;

        public DrawRecordViewHolder(View itemView) {
            super(itemView);
        }
    }
}
