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
 * Created by Administrator on 2018/2/28.
 */

public class BrandlistAdapter extends BaseRecyclerAdapter<GetListFilterEntity.Brand.Item> {

    public BrandlistAdapter(Context context, List<GetListFilterEntity.Brand.Item> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_logistics, parent, false);
        return new BrandListHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BrandListHolder){
            BrandListHolder mHolder = (BrandListHolder) holder;
            GetListFilterEntity.Brand.Item item = lists.get(position);

            mHolder.mtv_name.setText(item.brand_name);
            if (position != 0){
                GetListFilterEntity.Brand.Item item1 = lists.get(position - 1);
                if (!item.first_letter.equals(item1.first_letter)){
                    mHolder.view_line.setVisibility(View.VISIBLE);
                    mHolder.mtv_initials.setVisibility(View.VISIBLE);
                    mHolder.mtv_initials.setText(item.first_letter);
                }else {
                    mHolder.view_line.setVisibility(View.GONE);
                    mHolder.mtv_initials.setVisibility(View.GONE);
                }
            }else {
                mHolder.view_line.setVisibility(View.GONE);
                mHolder.mtv_initials.setVisibility(View.VISIBLE);
                mHolder.mtv_initials.setText(item.first_letter);
            }
        }
    }

    public class BrandListHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_initials)
        MyTextView mtv_initials;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.view_line)
        View view_line;

        public BrandListHolder(View itemView) {
            super(itemView);
            mtv_name.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
