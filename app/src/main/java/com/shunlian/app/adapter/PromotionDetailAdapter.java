package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.utils.HorItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/30.
 */

public class PromotionDetailAdapter extends BaseRecyclerAdapter<String> {


    public PromotionDetailAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        PromotionDetailHolder viewHolder = new PromotionDetailHolder(LayoutInflater.from(context).inflate(R.layout.item_dialog_combo, parent, false));
        return viewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromotionDetailHolder){
            PromotionDetailHolder mHolder = (PromotionDetailHolder) holder;
            LinearLayoutManager manager = new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL,false);
            mHolder.recycler_combo.setLayoutManager(manager);

            SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter<String>(context,R.layout.item_price_pic, DataUtil.getListString(10,"d")) {

                @Override
                public void convert(SimpleViewHolder holder, String s, int position) {

                }
            };
            mHolder.recycler_combo.setAdapter(simpleRecyclerAdapter);
        }
    }

    public class PromotionDetailHolder extends BaseRecyclerViewHolder{
        @BindView(R.id.tv_combo_price)
        TextView tv_combo_price;

        @BindView(R.id.tv_market_price)
        TextView tv_market_price;

        @BindView(R.id.recycler_combo)
        RecyclerView recycler_combo;

        @BindView(R.id.ll_combo)
        LinearLayout ll_combo;

        @BindView(R.id.ll_discounted)
        LinearLayout ll_discounted;

        @BindView(R.id.mtv_discount)
        MyTextView mtv_discount;

        @BindView(R.id.tv_market_detail)
        MyTextView tv_market_detail;

        @BindView(R.id.mtv_already_discounted)
        MyTextView mtv_already_discounted;
        public PromotionDetailHolder(View itemView) {
            super(itemView);
            ll_combo.setVisibility(View.GONE);
            ll_discounted.setVisibility(View.VISIBLE);
            mtv_already_discounted.setVisibility(View.VISIBLE);
            int px = TransformUtil.dip2px(context, 10);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycler_combo.getLayoutParams();
            layoutParams.leftMargin = TransformUtil.dip2px(context,20);
            layoutParams.topMargin = px;
            recycler_combo.setLayoutParams(layoutParams);

            recycler_combo.addItemDecoration(new HorItemDecoration(px,0,0));
        }
    }
}
