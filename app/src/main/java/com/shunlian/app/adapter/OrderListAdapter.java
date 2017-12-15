package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 */

public class OrderListAdapter extends BaseRecyclerAdapter<MyOrderEntity.Orders> {

    private final int pink_color;
    private final int new_gray;
    private final int strokeWidth;

    public OrderListAdapter(Context context, boolean isShowFooter, List<MyOrderEntity.Orders> lists) {
        super(context, isShowFooter, lists);
        pink_color = getColor(R.color.pink_color);
        new_gray = getColor(R.color.new_gray);
        strokeWidth = TransformUtil.dip2px(context, 0.5f);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderListHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        OrderListHolder mHolder = (OrderListHolder) holder;
        MyOrderEntity.Orders orders = lists.get(position);
        mHolder.mtv_storeName.setText(orders.store_name);
        mHolder.mtv_status.setText(orders.status_text);
        String format = getString(R.string.all_goods);
        mHolder.mtv_goods_count.setText(String.format(format,orders.qty));
        SpannableStringBuilder ssb = Common.dotAfterSmall(getString(R.string.rmb) + orders.total_amount, 11);
        mHolder.mtv_total_price.setText(ssb);
        String formatFreight = "（含运费￥%s）";
        mHolder.mtv_freight.setText(String.format(formatFreight,orders.shipping_fee));

        setShowStatus(mHolder,orders.status,orders.is_append);

        OrderGoodsAdapter adapter = new OrderGoodsAdapter(context,orders.order_goods);
        mHolder.recy_view.setAdapter(adapter);
    }

    private void setShowStatus(OrderListHolder mHolder, String status, String is_append) {
        //-1取消状态，0待支付，1待发货, 2待收货, 3待评价, 4已评价
        GradientDrawable t1Dackground;
        GradientDrawable t2Dackground;
        GradientDrawable t3Dackground;
        switch (status){
            case "-1":
                mHolder.mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mHolder.mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth,new_gray);
                mHolder.mtv_title1.setTextColor(new_gray);
                mHolder.mtv_title1.setText("联系商家");

                mHolder.mtv_title2.setVisibility(View.GONE);
                mHolder.mtv_title3.setVisibility(View.GONE);
                break;
            case "0":
                mHolder.mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mHolder.mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth,new_gray);
                mHolder.mtv_title1.setTextColor(new_gray);
                mHolder.mtv_title1.setText("联系商家");

                mHolder.mtv_title2.setVisibility(View.VISIBLE);
                t2Dackground = (GradientDrawable) mHolder.mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth,new_gray);
                mHolder.mtv_title2.setTextColor(new_gray);
                mHolder.mtv_title2.setText("取消订单");

                mHolder.mtv_title3.setVisibility(View.VISIBLE);
                t3Dackground = (GradientDrawable) mHolder.mtv_title3.getBackground();
                t3Dackground.setStroke(strokeWidth,pink_color);
                mHolder.mtv_title3.setTextColor(pink_color);
                mHolder.mtv_title3.setText("付款");
                break;
            case "1":
                mHolder.mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mHolder.mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth,new_gray);
                mHolder.mtv_title1.setTextColor(new_gray);
                mHolder.mtv_title1.setText("提醒发货");

                mHolder.mtv_title2.setVisibility(View.GONE);
                mHolder.mtv_title3.setVisibility(View.GONE);
                break;
            case "2":
                mHolder.mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mHolder.mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth,new_gray);
                mHolder.mtv_title1.setTextColor(new_gray);
                mHolder.mtv_title1.setText("延长收货");

                mHolder.mtv_title2.setVisibility(View.VISIBLE);
                t2Dackground = (GradientDrawable) mHolder.mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth,new_gray);
                mHolder.mtv_title2.setTextColor(new_gray);
                mHolder.mtv_title2.setText("查看物流");

                mHolder.mtv_title3.setVisibility(View.VISIBLE);
                t3Dackground = (GradientDrawable) mHolder.mtv_title3.getBackground();
                t3Dackground.setStroke(strokeWidth,pink_color);
                mHolder.mtv_title3.setTextColor(pink_color);
                mHolder.mtv_title3.setText("确认收货");
                break;
            case "3":
                mHolder.mtv_title1.setVisibility(View.GONE);

                mHolder.mtv_title2.setVisibility(View.VISIBLE);
                t2Dackground = (GradientDrawable) mHolder.mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth,new_gray);
                mHolder.mtv_title2.setTextColor(new_gray);
                mHolder.mtv_title2.setText("查看物流");

                mHolder.mtv_title3.setVisibility(View.VISIBLE);
                t3Dackground = (GradientDrawable) mHolder.mtv_title3.getBackground();
                t3Dackground.setStroke(strokeWidth,pink_color);
                mHolder.mtv_title3.setTextColor(pink_color);
                mHolder.mtv_title3.setText("评价");
                break;
            case "4":
                mHolder.mtv_title1.setVisibility(View.GONE);

                t2Dackground = (GradientDrawable) mHolder.mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth,new_gray);
                mHolder.mtv_title2.setTextColor(new_gray);
                mHolder.mtv_title2.setText("查看物流");

                if ("1".equals(is_append)){
                    mHolder.mtv_title3.setVisibility(View.VISIBLE);
                    t3Dackground = (GradientDrawable) mHolder.mtv_title3.getBackground();
                    t3Dackground.setStroke(strokeWidth,pink_color);
                    mHolder.mtv_title3.setTextColor(pink_color);
                    mHolder.mtv_title3.setText("追评");
                }else {
                    mHolder.mtv_title3.setVisibility(View.GONE);
                }
                break;
        }
    }


    public class OrderListHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_storeName)
        MyTextView mtv_storeName;

        @BindView(R.id.mtv_status)
        MyTextView mtv_status;

        @BindView(R.id.recy_view)
        RecyclerView recy_view;

        @BindView(R.id.mtv_goods_count)
        MyTextView mtv_goods_count;

        @BindView(R.id.mtv_total_price)
        MyTextView mtv_total_price;

        @BindView(R.id.mtv_freight)
        MyTextView mtv_freight;

        @BindView(R.id.mtv_title1)
        MyTextView mtv_title1;

        @BindView(R.id.mtv_title2)
        MyTextView mtv_title2;

        @BindView(R.id.mtv_title3)
        MyTextView mtv_title3;



        public OrderListHolder(View itemView) {
            super(itemView);

            LinearLayoutManager manager = new LinearLayoutManager(context);
            recy_view.setLayoutManager(manager);

            int space = TransformUtil.dip2px(context, 15);
            recy_view.addItemDecoration(new VerticalItemDecoration(space,0,0));
        }
    }
}
