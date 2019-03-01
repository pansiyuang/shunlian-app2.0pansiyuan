package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.MyDrawRecordEntity;

import java.util.List;

import butterknife.BindView;

public class GoldRecordAdapter extends BaseRecyclerAdapter<MyDrawRecordEntity.DrawRecord> {

    public GoldRecordAdapter(Context context, List<MyDrawRecordEntity.DrawRecord> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gold_egg_draw, parent, false);
        return new DrawRecordViewHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DrawRecordViewHolder) {
            DrawRecordViewHolder drawRecordViewHolder = (DrawRecordViewHolder) holder;
            MyDrawRecordEntity.DrawRecord drawRecord = lists.get(position);
            drawRecordViewHolder.tv_date.setText(drawRecord.create_time);
            drawRecordViewHolder.tv_name.setText(drawRecord.name);
            drawRecordViewHolder.tv_use_type.setText(drawRecord.use_rold);
        }
    }

    public class DrawRecordViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_use_type)
        TextView tv_use_type;

        public DrawRecordViewHolder(View itemView) {
            super(itemView);
        }
    }
}
