package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class FootMarkAdapter extends BaseRecyclerAdapter<OrderLogisticsEntity.FootMark> {

    public FootMarkAdapter(Context context, boolean isShowFooter, List<OrderLogisticsEntity.FootMark> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new FootMarkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_store_baby, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        OrderLogisticsEntity.FootMark footMark = lists.get(position);
        FootMarkViewHolder footMarkViewHolder = (FootMarkViewHolder) holder;
        GlideUtils.getInstance().loadImage(context, footMarkViewHolder.miv_onel, footMark.thumb);
        footMarkViewHolder.mtv_descl.setText(footMark.title);
        footMarkViewHolder.mtv_pricel.setText(footMark.price);
        footMarkViewHolder.mtv_pricer.setVisibility(View.GONE);
    }

    public class FootMarkViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_onel)
        MyImageView miv_onel;

        @BindView(R.id.mtv_descl)
        MyTextView mtv_descl;

        @BindView(R.id.mtv_pricel)
        MyTextView mtv_pricel;

        @BindView(R.id.mtv_pricer)
        MyTextView mtv_pricer;


        public FootMarkViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
