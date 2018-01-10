package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.ui.confirm_order.OrderLogisticsActivity;
import com.shunlian.app.ui.confirm_order.SearchOrderResultActivity;
import com.shunlian.app.ui.my_comment.CreatCommentActivity;
import com.shunlian.app.ui.order.AllFrag;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.widget.DiscountListDialog;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pay.PayListActivity;

/**
 * Created by Administrator on 2017/12/14.
 */

public class OrderListAdapter extends BaseRecyclerAdapter<MyOrderEntity.Orders> {

    private int pink_color;
    private int new_gray;
    private int strokeWidth;
    private AllFrag mAllFrag;
    private SearchOrderResultActivity mSearchOrderAct;
    private RefreshOrderListener mOrderListener;

    public OrderListAdapter(Context context, boolean isShowFooter, List<MyOrderEntity.Orders> lists, AllFrag allFrag) {
        super(context, isShowFooter, lists);
        mAllFrag = allFrag;
        init(context);
    }

    public OrderListAdapter(Context context, boolean isShowFooter, List<MyOrderEntity.Orders> lists,
                            SearchOrderResultActivity searchOrderResultActivity) {
        super(context, isShowFooter, lists);
        mSearchOrderAct = searchOrderResultActivity;
        init(context);
    }

    private void init(Context context) {
        pink_color = getColor(R.color.pink_color);
        new_gray = getColor(R.color.new_gray);
        if (context == null) {
            context = Common.getApplicationContext();
        }
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
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        OrderListHolder mHolder = (OrderListHolder) holder;
        MyOrderEntity.Orders orders = lists.get(position);
        mHolder.mtv_storeName.setText(orders.store_name);
        mHolder.mtv_status.setText(orders.status_text);
        String format = getString(R.string.all_goods);
        mHolder.mtv_goods_count.setText(String.format(format, orders.qty));
        SpannableStringBuilder ssb = Common.dotAfterSmall(getString(R.string.rmb) + orders.total_amount, 11);
        mHolder.mtv_total_price.setText(ssb);
        String formatFreight = getString(R.string.freight);
        mHolder.mtv_freight.setText(String.format(formatFreight, orders.shipping_fee));

        setShowStatus(mHolder, orders.status, orders.is_append,orders.is_postpone);

        OrderGoodsAdapter adapter = new OrderGoodsAdapter(context, orders.order_goods);
        mHolder.recy_view.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int posi) {
                if (listener != null) {
                    listener.onItemClick(view, position);
                }
            }
        });
    }

    private void setShowStatus(OrderListHolder mHolder, String status, String is_append, String is_postpone) {
        //-1取消状态，0待支付，1待发货, 2待收货, 3待评价, 4已评价
        GradientDrawable t1Dackground;
        GradientDrawable t2Dackground;
        GradientDrawable t3Dackground;
        switch (status) {
            case "-1":
                mHolder.mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mHolder.mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth, new_gray);
                mHolder.mtv_title1.setTextColor(new_gray);
                mHolder.mtv_title1.setText(getString(R.string.contact_seller));

                mHolder.mtv_title2.setVisibility(View.GONE);
                mHolder.mtv_title3.setVisibility(View.GONE);
                break;
            case "0":
                mHolder.mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mHolder.mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth, new_gray);
                mHolder.mtv_title1.setTextColor(new_gray);
                mHolder.mtv_title1.setText(getString(R.string.contact_seller));

                mHolder.mtv_title2.setVisibility(View.VISIBLE);
                t2Dackground = (GradientDrawable) mHolder.mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth, new_gray);
                mHolder.mtv_title2.setTextColor(new_gray);
                mHolder.mtv_title2.setText(getString(R.string.cancel_order));

                mHolder.mtv_title3.setVisibility(View.VISIBLE);
                t3Dackground = (GradientDrawable) mHolder.mtv_title3.getBackground();
                t3Dackground.setStroke(strokeWidth, pink_color);
                mHolder.mtv_title3.setTextColor(pink_color);
                mHolder.mtv_title3.setText(getString(R.string.order_fukuan));
                break;
            case "1":
                mHolder.mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mHolder.mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth, new_gray);
                mHolder.mtv_title1.setTextColor(new_gray);
                mHolder.mtv_title1.setText(getString(R.string.remind_send));

                mHolder.mtv_title2.setVisibility(View.GONE);
                mHolder.mtv_title3.setVisibility(View.GONE);
                break;
            case "2":

                if ("1".equals(is_postpone)){
                    mHolder.mtv_title1.setVisibility(View.GONE);
                }else {
                    mHolder.mtv_title1.setVisibility(View.VISIBLE);
                    t1Dackground = (GradientDrawable) mHolder.mtv_title1.getBackground();
                    t1Dackground.setStroke(strokeWidth, new_gray);
                    mHolder.mtv_title1.setTextColor(new_gray);
                    mHolder.mtv_title1.setText(getString(R.string.extend_the_collection));
                }

                mHolder.mtv_title2.setVisibility(View.VISIBLE);
                t2Dackground = (GradientDrawable) mHolder.mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth, new_gray);
                mHolder.mtv_title2.setTextColor(new_gray);
                mHolder.mtv_title2.setText(getString(R.string.order_wuliu));

                mHolder.mtv_title3.setVisibility(View.VISIBLE);
                t3Dackground = (GradientDrawable) mHolder.mtv_title3.getBackground();
                t3Dackground.setStroke(strokeWidth, pink_color);
                mHolder.mtv_title3.setTextColor(pink_color);
                mHolder.mtv_title3.setText(getString(R.string.confirm_goods));
                break;
            case "3":
                mHolder.mtv_title1.setVisibility(View.GONE);

                mHolder.mtv_title2.setVisibility(View.VISIBLE);
                t2Dackground = (GradientDrawable) mHolder.mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth, new_gray);
                mHolder.mtv_title2.setTextColor(new_gray);
                mHolder.mtv_title2.setText(getString(R.string.order_wuliu));

                mHolder.mtv_title3.setVisibility(View.VISIBLE);
                t3Dackground = (GradientDrawable) mHolder.mtv_title3.getBackground();
                t3Dackground.setStroke(strokeWidth, pink_color);
                mHolder.mtv_title3.setTextColor(pink_color);
                mHolder.mtv_title3.setText(getString(R.string.comment));
                break;
            case "4":
                mHolder.mtv_title1.setVisibility(View.GONE);

                t2Dackground = (GradientDrawable) mHolder.mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth, new_gray);
                mHolder.mtv_title2.setTextColor(new_gray);
                mHolder.mtv_title2.setText(getString(R.string.order_wuliu));

                if ("1".equals(is_append)) {
                    mHolder.mtv_title3.setVisibility(View.VISIBLE);
                    t3Dackground = (GradientDrawable) mHolder.mtv_title3.getBackground();
                    t3Dackground.setStroke(strokeWidth, pink_color);
                    mHolder.mtv_title3.setTextColor(pink_color);
                    mHolder.mtv_title3.setText(getString(R.string.append_comment));
                } else {
                    mHolder.mtv_title3.setVisibility(View.GONE);
                }
                break;
        }
    }


    public class OrderListHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

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

        @BindView(R.id.ll_rootView)
        LinearLayout ll_rootView;

        @BindView(R.id.mllayout_store)
        MyLinearLayout mllayout_store;

        public OrderListHolder(View itemView) {
            super(itemView);
            ll_rootView.setOnClickListener(this);
            mllayout_store.setOnClickListener(this);
            mtv_status.setOnClickListener(this);
            mtv_title1.setOnClickListener(this);
            mtv_title2.setOnClickListener(this);
            mtv_title3.setOnClickListener(this);


            LinearLayoutManager manager = new LinearLayoutManager(context);
            recy_view.setLayoutManager(manager);

            int space = TransformUtil.dip2px(context, 15);
            recy_view.addItemDecoration(new VerticalItemDecoration(space, 0, 0));
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            CharSequence text = null;
            if (mOrderListener != null){
                mOrderListener.onRefreshOrder(getAdapterPosition());
            }
            final MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
            switch (v.getId()) {
                case R.id.ll_rootView:
                case R.id.mtv_status:
                    if (listener != null) {
                        listener.onItemClick(v, getAdapterPosition());
                    }
                    break;
                case R.id.mllayout_store:
                    StoreAct.startAct(context,orders.store_id);
                    break;
                case R.id.mtv_title1:
                    text = mtv_title1.getText();
                    if (getString(R.string.contact_seller).equals(text)) {//联系商家

                    } else if (getString(R.string.remind_send).equals(text)) {//提醒发货
                        if (mAllFrag != null) {
                            mAllFrag.remindseller(orders.id);
                        }

                        if (mSearchOrderAct != null) {
                            mSearchOrderAct.remindseller(orders.id);
                        }
                    } else if (getString(R.string.extend_the_collection).equals(text)) {//延长收货
                        extendTheCollection(orders);
                    }

                    break;
                case R.id.mtv_title2:
                    text = mtv_title2.getText();
                    if (getString(R.string.cancel_order).equals(text)) {//取消订单

                        cancleOrder(orders);

                    } else if (getString(R.string.order_wuliu).equals(text)) {//物流
                        //MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
                        OrderLogisticsActivity.startAct(context, orders.id);
                    }
                    break;
                case R.id.mtv_title3:
                    text = mtv_title3.getText();
                    if (getString(R.string.order_fukuan).equals(text)) {//付款
                        if (mAllFrag != null) {
                            PayListActivity.startAct(mAllFrag.getActivity(),
                                    null, null,orders.id,orders.total_amount);
                        }else if (mSearchOrderAct != null){
                            PayListActivity.startAct(mSearchOrderAct,
                                    null, null,orders.id,orders.total_amount);
                        }

                    } else if (getString(R.string.confirm_goods).equals(text)) {//确认收货

                        confirmreceipt(orders);

                    } else if (getString(R.string.comment).equals(text)) {//评价

                        //MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
                        List<ReleaseCommentEntity> entities = new ArrayList<>();
                        List<MyOrderEntity.OrderGoodsBean> order_goods = orders.order_goods;
                        for (int i = 0; i < order_goods.size(); i++) {
                            MyOrderEntity.OrderGoodsBean bean = order_goods.get(i);
                            ReleaseCommentEntity entity = new ReleaseCommentEntity(orders.order_sn,
                                    bean.thumb, bean.title, bean.price, bean.goods_id);
                            entities.add(entity);
                        }
                        CreatCommentActivity.startAct(context, entities, CreatCommentActivity.CREAT_COMMENT);

                    } else if (getString(R.string.append_comment).equals(text)) {//追评

                        //MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
                        List<ReleaseCommentEntity> entities = new ArrayList<>();
                        List<MyOrderEntity.OrderGoodsBean> order_goods = orders.order_goods;
                        for (int i = 0; i < order_goods.size(); i++) {
                            MyOrderEntity.OrderGoodsBean bean = order_goods.get(i);
                            ReleaseCommentEntity entity = new ReleaseCommentEntity(bean.thumb,
                                    bean.title, bean.price, bean.comment_id);
                            entity.order = bean.order_sn;
                            entity.is_append = bean.is_append;
                            entities.add(entity);
                        }
                        CreatCommentActivity.startAct(context, entities, CreatCommentActivity.APPEND_COMMENT);

                    }
                    break;
            }
        }

        /**
         * 确认收货
         * @param orders
         */
        public void confirmreceipt(final MyOrderEntity.Orders orders) {
            final PromptDialog promptDialog = new PromptDialog((Activity) context);
            promptDialog.setSureAndCancleListener(getString(R.string.confirm_goods_receipt),
                    getString(R.string.confirm_goods_receipt_label),
                    getString(R.string.confirm_goods), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAllFrag != null){
                        mAllFrag.confirmreceipt(orders.id);
                    }
                    if (mSearchOrderAct != null){
                        mSearchOrderAct.confirmreceipt(orders.id);
                    }
                    promptDialog.dismiss();
                }
            }, getString(R.string.errcode_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptDialog.dismiss();
                }
            }).show();
        }

        /*
            取消订单
         */
        public void cancleOrder(final MyOrderEntity.Orders orders) {
            DiscountListDialog dialog = new DiscountListDialog(context);
            dialog.setSelectReason();
            dialog.show();
            dialog.setSelectListener(new DiscountListDialog.ISelectListener() {
                @Override
                public void onSelect(int position) {
                    if (mAllFrag != null) {
                        mAllFrag.cancleOrder(orders.id,position + 1);
                    }
                    if (mSearchOrderAct != null) {
                        mSearchOrderAct.cancleOrder(orders.id,position + 1);
                    }
                }
            });
        }

        /*
        延长收货
         */
        public void extendTheCollection(final MyOrderEntity.Orders orders) {
            final PromptDialog promptDialog = new PromptDialog((Activity) context);
            promptDialog.setSureAndCancleListener(getString(R.string.confirm_extend_goods_time),
                    getString(R.string.order_extend_once),
                    getString(R.string.confirm_goods), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAllFrag != null) {
                        mAllFrag.postpone(orders.id);
                    }
                    if (mSearchOrderAct != null) {
                        mSearchOrderAct.postpone(orders.id);
                    }

                    promptDialog.dismiss();
                }
            }, getString(R.string.errcode_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptDialog.dismiss();
                }
            }).show();
        }
    }

    public void setRefreshOrderListener(RefreshOrderListener orderListener){
        mOrderListener = orderListener;
    }

    /**
     *刷新订单监听
     */
    public interface RefreshOrderListener{
        void onRefreshOrder(int position);
    }
}
