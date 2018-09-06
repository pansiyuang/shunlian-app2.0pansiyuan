package com.shunlian.app.adapter;

import android.content.Context;
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

public class EggDetailsAdapter extends BaseRecyclerAdapter<EggDetailEntity.Out.In> {


    public EggDetailsAdapter(Context context, boolean isShowFooter, List<EggDetailEntity.Out.In> lists) {
        super(context, isShowFooter, lists);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_egg_details, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        EggDetailEntity.Out.In ad = lists.get(position);
        mHolder.ntv_time.setText(ad.hour);
        mHolder.ntv_amount.setText(ad.change_num);
        mHolder.ntv_desc.setText(ad.handle);
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.ntv_time)
        NewTextView ntv_time;

        @BindView(R.id.ntv_desc)
        NewTextView ntv_desc;

        @BindView(R.id.ntv_amount)
        NewTextView ntv_amount;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }

    }
}
