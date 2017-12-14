package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 */

public class TraceAdapter extends BaseRecyclerAdapter<OrderLogisticsEntity.Trace> {

    public static final int HEAD_VIEW = 1;

    public TraceAdapter(Context context, boolean isShowFooter, List<OrderLogisticsEntity.Trace> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TraceViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_logistics, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        OrderLogisticsEntity.Trace trace = lists.get(position);
        TraceViewHolder traceViewHolder = (TraceViewHolder) holder;
        traceViewHolder.tv_logistics_detail.setText(trace.AcceptStation);
        traceViewHolder.tv_logistics_time.setText(trace.AcceptTime);
    }

    public void handleHead(RecyclerView.ViewHolder holder) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_VIEW) {
            return new HeadViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_order_title, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case HEAD_VIEW:
                handleHead(holder);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_VIEW;
        }
        return super.getItemViewType(position);
    }

    public class HeadViewHolder extends BaseRecyclerViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class TraceViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_logistics)
        MyImageView miv_logistics;

        @BindView(R.id.view_logistics_line)
        View view_logistics_line;

        @BindView(R.id.tv_logistics_detail)
        TextView tv_logistics_detail;

        @BindView(R.id.tv_logistics_time)
        TextView tv_logistics_time;

        public TraceViewHolder(View itemView) {
            super(itemView);
        }
    }
}
