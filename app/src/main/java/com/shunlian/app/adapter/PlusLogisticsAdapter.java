package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/28.
 */

public class PlusLogisticsAdapter extends BaseRecyclerAdapter<OrderLogisticsEntity.Trace> {

    public PlusLogisticsAdapter(Context context, List<OrderLogisticsEntity.Trace> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TraceViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_logistics, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((TraceViewHolder) holder).miv_logistics.getLayoutParams();
        OrderLogisticsEntity.Trace trace = lists.get(position);
        TraceViewHolder traceViewHolder = (TraceViewHolder) holder;
        traceViewHolder.tv_logistics_detail.setText(trace.AcceptStation);
        traceViewHolder.tv_logistics_time.setText(trace.AcceptTime);

        if (position == getItemCount() - 1) {
            params.width = TransformUtil.dip2px(context, 14);
            params.height = TransformUtil.dip2px(context, 14);
            traceViewHolder.miv_logistics.setLayoutParams(params);
            traceViewHolder.miv_logistics.setImageResource(R.mipmap.img_wuliu_active);
            traceViewHolder.view_logistics_line.setVisibility(View.GONE);
            traceViewHolder.tv_logistics_detail.setTextColor(getColor(R.color.value_048BFE));
        } else {
            params.width = TransformUtil.dip2px(context, 10);
            params.height = TransformUtil.dip2px(context, 10);
            traceViewHolder.miv_logistics.setLayoutParams(params);
            traceViewHolder.miv_logistics.setImageResource(R.mipmap.img_wuliu_before);
            traceViewHolder.view_logistics_line.setVisibility(View.VISIBLE);
            traceViewHolder.tv_logistics_detail.setTextColor(getColor(R.color.new_text));
        }

        if (position == getItemCount() - 1) {
            traceViewHolder.setVisibility(true);
            traceViewHolder.rl_logistics.setPadding(0, TransformUtil.dip2px(context, 15), 0, 0);
            traceViewHolder.view_logistics_line.setVisibility(View.VISIBLE);
        } else {
            traceViewHolder.setVisibility(false);
        }
        traceViewHolder.tv_logistics_more.setVisibility(View.GONE);
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

        @BindView(R.id.rl_logistics)
        RelativeLayout rl_logistics;

        @BindView(R.id.tv_logistics_more)
        TextView tv_logistics_more;


        public TraceViewHolder(View itemView) {
            super(itemView);
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = RelativeLayout.LayoutParams.WRAP_CONTENT;// 这里注意使用自己布局的根布局类型
                param.width = RelativeLayout.LayoutParams.MATCH_PARENT;// 这里注意使用自己布局的根布局类型
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }
}
