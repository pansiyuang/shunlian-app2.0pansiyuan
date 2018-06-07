package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
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
    public List<OrderLogisticsEntity.FootMark> footMarkList;

    public FootMarkAdapter(Context context, boolean isShowFooter, List<OrderLogisticsEntity.FootMark> lists) {
        super(context, isShowFooter, lists);
        this.footMarkList = lists;
    }

    public void setData(List<OrderLogisticsEntity.FootMark> footMarks) {
        this.footMarkList = footMarks;
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new FootMarkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_store_baby, parent, false));
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        OrderLogisticsEntity.FootMark footMark = footMarkList.get(position);
        FootMarkViewHolder footMarkViewHolder = (FootMarkViewHolder) holder;
        GlideUtils.getInstance().loadImage(context, footMarkViewHolder.miv_onel, footMark.thumb);
        footMarkViewHolder.mtv_descl.setText(footMark.title);
        footMarkViewHolder.mtv_pricel.setText(footMark.price);
        footMarkViewHolder.mtv_pricer.setVisibility(View.GONE);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position + 1 == getItemCount()) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
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
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
