package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/25.
 */

public class ConfirmOrderAdapter extends BaseRecyclerAdapter<String> {

    public static final int ITEM_ADDRESS = 2;//地址条目
    public static final int item_invalid = 3;//失效商品

    public ConfirmOrderAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return ITEM_ADDRESS;
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM_ADDRESS:
                View head_address = LayoutInflater.from(context)
                        .inflate(R.layout.head_address, parent, false);
                return new AddressHolder(head_address);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View confirm_order = LayoutInflater.from(context)
                .inflate(R.layout.item_confirm_order, parent, false);
        return new BuyGoodsHolder(confirm_order);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

    }

    public class AddressHolder extends BaseRecyclerViewHolder{

        public AddressHolder(View itemView) {
            super(itemView);
        }
    }


    public class BuyGoodsHolder extends BaseRecyclerViewHolder{

        public BuyGoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
