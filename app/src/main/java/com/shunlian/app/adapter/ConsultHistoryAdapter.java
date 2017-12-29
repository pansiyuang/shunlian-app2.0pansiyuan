package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ConsultHistoryAdapter extends BaseRecyclerAdapter<String> {

    public ConsultHistoryAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consult_history, parent, false);
        ConsultHistoryHolder holder = new ConsultHistoryHolder(view);
        return holder;
    }

    /**
     * 处理列表
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConsultHistoryHolder){
            ConsultHistoryHolder mHolder = (ConsultHistoryHolder) holder;
            GradientDrawable background = (GradientDrawable) mHolder.mtv_detail.getBackground();
            background.setColor(getColor(R.color.value_f3));

            if (position == 0){
                mHolder.view_logistics_line1.setVisibility(View.INVISIBLE);
                mHolder.miv_logistics.setImageResource(R.mipmap.img_wuliu_active);
            }else {
                mHolder.view_logistics_line1.setVisibility(View.VISIBLE);
                mHolder.miv_logistics.setImageResource(R.mipmap.img_wuliu_before);
            }
        }
    }

    public class ConsultHistoryHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_detail)
        MyTextView mtv_detail;

        @BindView(R.id.view_logistics_line1)
        View view_logistics_line1;

        @BindView(R.id.miv_logistics)
        MyImageView miv_logistics;

        @BindView(R.id.civ_head)
        CircleImageView civ_head;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.mtv_label)
        MyTextView mtv_label;

        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        public ConsultHistoryHolder(View itemView) {
            super(itemView);
        }
    }
}
