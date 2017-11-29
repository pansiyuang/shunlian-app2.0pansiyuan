package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.widget.DiscountListDialog;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/25.
 */

public class ConfirmOrderAdapter extends BaseRecyclerAdapter<String> {

    public static final int ITEM_ADDRESS = 2;//地址条目
    public static final int ITEM_INVALID = 3;//失效商品
    public static final int ITEM_STATION = 4;//占位条目

    public ConfirmOrderAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println("===position===="+position);
        if (position == 0){
            return ITEM_ADDRESS;
        }else if (position + 1  == getItemCount()){
            return ITEM_STATION;
        }else if (position + 2 == getItemCount()){
            return ITEM_INVALID;
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM_ADDRESS:
                View head_address = LayoutInflater.from(context)
                        .inflate(R.layout.head_address, parent, false);
                return new AddressHolder(head_address);
            case ITEM_INVALID:
                View invalid_layout = LayoutInflater.from(context)
                        .inflate(R.layout.only_recycler_layout, parent, false);
                return new InvalidGoodsHolder(invalid_layout);
            case ITEM_STATION:
                View station_layout = LayoutInflater.from(context)
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
                return new StationHolder(station_layout);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case ITEM_ADDRESS:
                break;
            case ITEM_INVALID:
                handlerInvalidGoods(holder,position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void handlerInvalidGoods(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InvalidGoodsHolder){
            InvalidGoodsHolder mHolder = (InvalidGoodsHolder) holder;
            final int padding = TransformUtil.dip2px(context, 10);
            SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter<String>(context,
                    R.layout.item_invalid_goods,DataUtil.getListString(5,"ss")) {

                @Override
                public void convert(SimpleViewHolder holder, String s, int position) {
                    if (position == 0){
                        holder.getView(R.id.mtv_inva).setVisibility(View.VISIBLE);
                        holder.getView(R.id.line).setVisibility(View.VISIBLE);
                    }else {
                        holder.getView(R.id.mtv_inva).setVisibility(View.GONE);
                        holder.getView(R.id.line).setVisibility(View.GONE);
                    }
                    MyRelativeLayout mrl_rootview = holder.getView(R.id.mrl_rootview);

                    mrl_rootview.setPadding(padding,padding,padding,0);
                }
            };
            mHolder.recy_view.setAdapter(adapter);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View confirm_order = LayoutInflater.from(context)
                .inflate(R.layout.item_confirm_order, parent, false);
        return new BuyGoodsHolder(confirm_order);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BuyGoodsHolder){
            BuyGoodsHolder mHolder = (BuyGoodsHolder) holder;
            mHolder.recy_view.setAdapter(new AppointGoodsAdapter(context,
                    false,
                    DataUtil.getListString(2,"dfa")));
        }
    }

    public class AddressHolder extends BaseRecyclerViewHolder{

        public AddressHolder(View itemView) {
            super(itemView);
        }
    }


    public class BuyGoodsHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.recy_view)
        RecyclerView recy_view;

        @BindView(R.id.mllayout_discount)
        MyLinearLayout mllayout_discount;

        public BuyGoodsHolder(View itemView) {
            super(itemView);
            recy_view.setNestedScrollingEnabled(false);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recy_view.setLayoutManager(manager);
            int space = TransformUtil.dip2px(context, 20);
            recy_view.addItemDecoration(new VerticalItemDecoration(space,space / 2,0));
            recy_view.setFocusable(false);

            mllayout_discount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mllayout_discount:
                    DiscountListDialog dialog = new DiscountListDialog(context);
                    dialog.setGoodsDiscount();
                    dialog.show();
                    break;
            }
        }
    }

    public class InvalidGoodsHolder extends BaseRecyclerViewHolder{

        protected final RecyclerView recy_view;

        public InvalidGoodsHolder(View itemView) {
            super(itemView);
            recy_view = (RecyclerView) itemView;
            recy_view.setFocusable(true);
            recy_view.setNestedScrollingEnabled(false);
            LinearLayoutManager manager  = new LinearLayoutManager(context);
            recy_view.setLayoutManager(manager);
            int space = TransformUtil.dip2px(context, 10);
            recy_view.addItemDecoration(new VerticalItemDecoration(space,0,0));
        }
    }

    public class StationHolder extends BaseRecyclerViewHolder{

        public StationHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = TransformUtil.dip2px(context,41);
            itemView.setLayoutParams(layoutParams);
        }
    }
}
