package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;

import java.util.List;

/**
 * Created by zhanghe on 2018/7/24.
 */

public class CouponGoodsAdapter extends BaseRecyclerAdapter<String> {

    public static final int ITEM_HEAD = 1<<3;

    public CouponGoodsAdapter(Context context,List<String> lists) {
        super(context, true, lists);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return ITEM_HEAD;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEAD){
            View view = mInflater.inflate(R.layout.item_coupon,parent,false);
            return new HeadHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
//        View view = new
        return null;
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

    }


    public class HeadHolder extends BaseRecyclerViewHolder{

        public HeadHolder(View itemView) {
            super(itemView);
        }
    }

    public class CouponGoodsHolder extends BaseRecyclerViewHolder{

        public CouponGoodsHolder(View itemView) {
            super(itemView);
        }
    }
}
