package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ShaixuanAttrsAdapter extends BaseRecyclerAdapter<String> {

    public ShaixuanAttrsAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pingpai, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String attrs = lists.get(position);
        viewHolder.mtv_name.setText(attrs);
    }


    public class ViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        public ViewHolder(View itemView) {
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
