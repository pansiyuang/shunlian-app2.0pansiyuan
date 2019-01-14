package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.EggDetailEntity;
import com.shunlian.app.bean.NewEggDetailEntity;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class NewEggDetailAdapter extends BaseRecyclerAdapter<NewEggDetailEntity.In> {


    public NewEggDetailAdapter(Context context, boolean isShowFooter, List<NewEggDetailEntity.In> lists) {
        super(context, isShowFooter, lists);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_egg_detail_new, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        NewEggDetailEntity.In ad = lists.get(position);
        mHolder.ntv_date.setText(ad.hour);
        mHolder.ntv_desc.setText(ad.handle);
        mHolder.ntv_number.setText(ad.change_num);
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.ntv_desc)
        NewTextView ntv_desc;

        @BindView(R.id.ntv_date)
        NewTextView ntv_date;

        @BindView(R.id.ntv_number)
        NewTextView ntv_number;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }

    }
}
