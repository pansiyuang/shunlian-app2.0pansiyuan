package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.newchat.entity.ReplysetEntity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/18.
 */

public class FastAdapter extends BaseRecyclerAdapter<ReplysetEntity.Replyset> {

    public FastAdapter(Context context, List<ReplysetEntity.Replyset> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new FastHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_fast, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        FastHolder fastHolder = (FastHolder) holder;
        String str = lists.get(position).item;
        fastHolder.tv_fast_content.setText(str);
    }

    public class FastHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_fast_content)
        TextView tv_fast_content;

        public FastHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }
    }
}
