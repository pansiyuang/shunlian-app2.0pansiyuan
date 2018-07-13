package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/7/13.
 */

public class MoreCreditAdapter extends BaseRecyclerAdapter<String> {

    public MoreCreditAdapter(Context context, List<String> lists) {
        super(context, false, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_more_credit, parent, false);
        return new MoreCreditHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MoreCreditHolder){

        }
    }

    public class MoreCreditHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.rlayout_root)
        RelativeLayout rlayout_root;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_rel_price)
        MyTextView mtv_rel_price;

        public MoreCreditHolder(View itemView) {
            super(itemView);
            GradientDrawable rootGB = (GradientDrawable) rlayout_root.getBackground();
            rootGB.setColor(Color.WHITE);
            int w = TransformUtil.dip2px(context, 1);
            rootGB.setStroke(w,getColor(R.color.value_484848));
        }
    }
}
