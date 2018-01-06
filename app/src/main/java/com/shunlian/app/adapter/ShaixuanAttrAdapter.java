package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
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

public class ShaixuanAttrAdapter extends BaseRecyclerAdapter<GetListFilterEntity.Attr> {

    public ShaixuanAttrAdapter(Context context, boolean isShowFooter, List<GetListFilterEntity.Attr> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shaixuan_attr, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        GetListFilterEntity.Attr attr = lists.get(position);
        viewHolder.mtv_name.setText(attr.name);
        viewHolder.rv_attr.setLayoutManager(new GridLayoutManager(context,3));
        viewHolder.rv_attr.setAdapter(new ShaixuanAttrsAdapter(context, false,attr.val_list));
    }


    public class ViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.rv_attr)
        RecyclerView rv_attr;

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
