package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreSortAdapters extends BaseRecyclerAdapter<StoreCategoriesEntity.MData.Children> {
    private Context context;
    private List<StoreCategoriesEntity.MData.Children> datas;

    public StoreSortAdapters(Context context, boolean isShowFooter, List<StoreCategoriesEntity.MData.Children> datas) {
        super(context, isShowFooter, datas);
        this.context = context;
        this.datas = datas;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_sorts, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoHolder) {
            TwoHolder twoHolder = (TwoHolder) holder;
            StoreCategoriesEntity.MData.Children data = datas.get(position);
            twoHolder.mtv_name.setText(data.name);
        }
    }


    class TwoHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        private MyTextView mtv_name;

        TwoHolder(View itemView) {
            super(itemView);
            mtv_name = (MyTextView) itemView.findViewById(R.id.mtv_name);
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
