package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.EggDetailEntity;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class EggDetailAdapter extends BaseRecyclerAdapter<EggDetailEntity.Out> {


    public EggDetailAdapter(Context context, boolean isShowFooter, List<EggDetailEntity.Out> lists) {
        super(context, isShowFooter, lists);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_egg_detail, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        EggDetailEntity.Out ad = lists.get(position);
        mHolder.ntv_date.setText(ad.date);
        mHolder.ntv_amount.setText(ad.total);
        mHolder.rv_detail.setLayoutManager(new LinearLayoutManager(context));
        mHolder.rv_detail.setAdapter(new EggDetailsAdapter(context,false,ad.list));
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.ntv_date)
        NewTextView ntv_date;

        @BindView(R.id.ntv_amount)
        NewTextView ntv_amount;

        @BindView(R.id.rv_detail)
        RecyclerView rv_detail;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }

    }
}
