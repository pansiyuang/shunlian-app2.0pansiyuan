package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/20.
 */

public class StoreEnableAdapter extends BaseRecyclerAdapter<ShoppingCarEntity.Enabled> {
    private Context mContext;
    private List<ShoppingCarEntity.Enabled> mData;

    public StoreEnableAdapter(Context context, boolean isShowFooter, List<ShoppingCarEntity.Enabled> lists) {
        super(context, isShowFooter, lists);
        this.mContext = context;
        this.mData = lists;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        StoreViewHolder storeViewHolder = new StoreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shoppingcar_store, parent, false));
        return storeViewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        StoreViewHolder storeViewHolder = (StoreViewHolder) holder;
        ShoppingCarEntity.Enabled enableds = mData.get(position);
        storeViewHolder.tv_store.setText(enableds.store_name);
    }

    public class StoreViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_store_select)
        MyImageView miv_store_select;

        @BindView(R.id.tv_store)
        TextView tv_store;

        @BindView(R.id.rl_merge)
        RelativeLayout rl_merge;

        @BindView(R.id.tv_full_min)
        TextView tv_full_min;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.recycler_goods)
        RecyclerView recycler_goods;

        @BindView(R.id.tv_prefer)
        TextView tv_prefer;

        @BindView(R.id.tv_discount)
        TextView tv_discount;

        @BindView(R.id.rl_prefer)
        RelativeLayout rl_prefer;

        public StoreViewHolder(View itemView) {
            super(itemView);
        }
    }
}
