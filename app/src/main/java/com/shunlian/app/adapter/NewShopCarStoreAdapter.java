package com.shunlian.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import com.shunlian.app.R;
import com.shunlian.app.bean.ShoppingCarEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */

public class NewShopCarStoreAdapter extends GroupedRecyclerViewAdapter {
    private static final int TYPE_HEADER_1 = 1;
    private static final int TYPE_HEADER_2 = 2;

    private static final int TYPE_CHILD_1 = 3;
    private static final int TYPE_CHILD_2 = 4;

    private List<ShoppingCarEntity.Enabled> mStores;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public NewShopCarStoreAdapter(Context context, List<ShoppingCarEntity.Enabled> enableds) {
        super(context);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.mStores = enableds;
    }

    @Override
    public int getGroupCount() {
        return mStores.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mStores.get(groupPosition).promotion.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }


    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        if (viewType == TYPE_HEADER_1) {
            return R.layout.item_shoppingcar_promotion;
        } else {
            return R.layout.foot_shoppingcar_disable;
        }
    }

    @Override
    public int getHeaderLayout(int viewType) {
        if (viewType == TYPE_HEADER_1) {
            return R.layout.item_shoppingcar_store;
        } else {
            return R.layout.foot_shoppingcar_disable;
        }
    }

    @Override
    public void onBindHeaderViewHolder(BaseRecyclerAdapter.BaseRecyclerViewHolder holder, int groupPosition) {
        int viewType = getHeaderViewType(groupPosition);
    }

    @Override
    public void onBindFooterViewHolder(BaseRecyclerAdapter.BaseRecyclerViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseRecyclerAdapter.BaseRecyclerViewHolder holder, int groupPosition, int childPosition) {

    }
}
