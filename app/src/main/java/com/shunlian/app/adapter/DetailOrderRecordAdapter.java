package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.DetailOrderRecordEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/17.
 */

public class DetailOrderRecordAdapter extends BaseRecyclerAdapter<DetailOrderRecordEntity.Item> {

    private List<BaseRecyclerAdapter> adapterList = new ArrayList<>();
    public DetailOrderRecordAdapter(Context context, List<DetailOrderRecordEntity.Item> lists) {
        super(context, true, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_order_record, parent, false);
        return new DetailOrderRecordHolder(view);
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_order));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        DetailOrderRecordHolder mHolder = (DetailOrderRecordHolder) holder;
        DetailOrderRecordEntity.Item item = lists.get(position);
        if ("1".equals(item.anonymous)) {//匿名购买
            gone(mHolder.view_line,
                    mHolder.recy_view,
                    mHolder.mtv_buy,mHolder.mtv_order_state);
            visible(mHolder.mtv_forecast_earnings,mHolder.view_line1,mHolder.mtv_order_time);

//            String offered = "天天测试";
            String offered = item.big_label;
            if (isEmpty(offered)){
                gone(mHolder.mtv_label);
            }else {
                visible(mHolder.mtv_label);
                mHolder.mtv_label.setText(offered);
            }
            mHolder.mtv_order.setText("匿名购买");
            mHolder.mtv_forecast_earnings.setText("预估收益："+item.estimate_profit);
        }else {
            visible(mHolder.view_line,mHolder.view_line1,
                    mHolder.recy_view,mHolder.mtv_order_time,
                    mHolder.mtv_buy,mHolder.mtv_order_state);
            gone(mHolder.mtv_forecast_earnings,mHolder.mtv_label);

            mHolder.mtv_order.setText("订单号：" + item.order_sn);
            mHolder.mtv_order_state.setText(item.status_desc);
            //mHolder.mtv_order_time.setText("下单日期：" + item.order_time);
            GoodsItemAdapter goodsItemAdapter = new GoodsItemAdapter(context, item.order_goods);
            if (adapterList != null)adapterList.add(goodsItemAdapter);
            mHolder.recy_view.setAdapter(goodsItemAdapter);


            GradientDrawable buyDrawable = (GradientDrawable) mHolder.mtv_buy.getBackground();
            buyDrawable.setStroke(0, getColor(R.color.white));
            if ("1".equals(item.deal_type)) {
                mHolder.mtv_buy.setText("买");
                buyDrawable.setColor(Color.parseColor("#FB9A00"));
            } else {
                mHolder.mtv_buy.setText("卖");
                buyDrawable.setColor(getColor(R.color.pink_color));
            }
        }
        mHolder.mtv_order_time.setText("下单日期：" + item.order_time);
    }

    @Override
    public void unbind() {
        super.unbind();
        for (BaseRecyclerAdapter adapter : adapterList) {
            if (adapter != null && adapter.lists != null){
                adapter.unbind();
                adapter.lists.clear();
                adapter = null;
            }
        }
        adapterList.clear();
        adapterList = null;
    }

    public class DetailOrderRecordHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_order)
        MyTextView mtv_order;

        @BindView(R.id.mtv_label)
        MyTextView mtv_label;

        @BindView(R.id.mtv_order_state)
        MyTextView mtv_order_state;

        @BindView(R.id.mtv_order_time)
        MyTextView mtv_order_time;

        @BindView(R.id.recy_view)
        RecyclerView recy_view;

        @BindView(R.id.mtv_buy)
        MyTextView mtv_buy;

        @BindView(R.id.mtv_forecast_earnings)
        MyTextView mtv_forecast_earnings;

        @BindView(R.id.view_line)
        View view_line;

        @BindView(R.id.view_line1)
        View view_line1;

        public DetailOrderRecordHolder(View itemView) {
            super(itemView);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recy_view.setLayoutManager(manager);
            recy_view.setNestedScrollingEnabled(false);
        }
    }


    public class GoodsItemAdapter extends BaseRecyclerAdapter<DetailOrderRecordEntity.OrderGoods>{


        public GoodsItemAdapter(Context context,List<DetailOrderRecordEntity.OrderGoods> lists) {
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
            View view = mInflater.inflate(R.layout.item_order_goods, parent, false);
            return new GoodsItemHolder(view);
        }

        /**
         * 处理列表
         *
         * @param holder
         * @param position
         */
        @Override
        public void handleList(RecyclerView.ViewHolder holder, int position) {
            GoodsItemHolder mHolder = (GoodsItemHolder) holder;
            DetailOrderRecordEntity.OrderGoods item = lists.get(position);
            GlideUtils.getInstance().loadImage(context,mHolder.miv_goods_pic,item.thumb);
            mHolder.mtv_order_source.setText(item.child_type);
            mHolder.mtv_goods_attr.setText(item.sku);
            mHolder.mtv_goods_count.setText("x"+item.qty);
            mHolder.mtv_goods_price.setText(getString(R.string.rmb)+item.price);
            mHolder.mtv_assertionProfit.setText(item.estimate_profit);
            String offered = item.big_label;
//            String offered = "测试";
            if (isEmpty(offered)){
                gone(mHolder.mtv_label);
                mHolder.mtv_goods_title.setText(item.title);
            }else {
                visible(mHolder.mtv_label);
                mHolder.mtv_label.setText(offered);
                mHolder.mtv_goods_title.setText(Common.getPlaceholder(offered.length())
                        .concat(item.title));
            }
        }


        public class GoodsItemHolder extends BaseRecyclerViewHolder
                implements View.OnClickListener {

            @BindView(R.id.mtv_order_source)
            MyTextView mtv_order_source;

            @BindView(R.id.mtv_label)
            MyTextView mtv_label;

            @BindView(R.id.mtv_goods_title)
            MyTextView mtv_goods_title;

            @BindView(R.id.mtv_goods_attr)
            MyTextView mtv_goods_attr;

            @BindView(R.id.mtv_goods_price)
            MyTextView mtv_goods_price;

            @BindView(R.id.mtv_goods_count)
            MyTextView mtv_goods_count;

            @BindView(R.id.mtv_assertionProfit)
            MyTextView mtv_assertionProfit;

            @BindView(R.id.miv_goods_pic)
            MyImageView miv_goods_pic;

            public GoodsItemHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
            }

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                DetailOrderRecordEntity.OrderGoods orderGoods = lists.get(getAdapterPosition());
                GoodsDetailAct.startAct(context,orderGoods.goods_id);
            }
        }
    }


}

