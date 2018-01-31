package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/25.
 */

public class FootAdapter extends BaseRecyclerAdapter<FootprintEntity.MarkData> {

    public static final int DATE_LAYOUT = 1003;

    public FootAdapter(Context context, boolean isShowFooter, List<FootprintEntity.MarkData> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new MarkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_foot, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MarkViewHolder) {
            MarkViewHolder markViewHolder = (MarkViewHolder) holder;
            FootprintEntity.MarkData markData = lists.get(position);
            GlideUtils.getInstance().loadImage(context, markViewHolder.miv_icon, markData.thumb);
            markViewHolder.tv_price.setText(getString(R.string.common_yuan) + markData.price);
        }
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_footmark));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
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
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    public class DateViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.tv_date)
        TextView tv_date;

        public DateViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class MarkViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.ll_del)
        LinearLayout ll_del;

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_price)
        TextView tv_price;

        public MarkViewHolder(View itemView) {
            super(itemView);
        }
    }
}
