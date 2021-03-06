package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StorePromotionGoodsListTwoEntity;
import com.shunlian.app.ui.goods_detail.ComboDetailAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreDiscountTwoAdapter extends BaseRecyclerAdapter<StorePromotionGoodsListTwoEntity.Lists.Good> {
    private Context context;
    private List<StorePromotionGoodsListTwoEntity.Lists.Good> mDatas;

    public StoreDiscountTwoAdapter(Context context, boolean isShowFooter, List<StorePromotionGoodsListTwoEntity.Lists.Good> mDatas) {
        super(context, isShowFooter, mDatas);
        this.context = context;
        this.mDatas = mDatas;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount_combo, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoHolder) {
            TwoHolder twoHolder = (TwoHolder) holder;
            StorePromotionGoodsListTwoEntity.Lists.Good mData = mDatas.get(position);
            twoHolder.mtv_title.setText(mData.title);
            twoHolder.mtv_marketPrice.setText(getString(R.string.store_taocan)+mData.old_price);
            twoHolder.mtv_original.setText(mData.price);
            twoHolder.rv_combo.setVisibility(View.GONE);
            twoHolder.rv_combo.setNestedScrollingEnabled(false);//防止横滑的view区域上划失效
            if (mData.data != null && mData.data.size() > 0) {
                if (twoHolder.storeDiscountTwoAdapters==null){
                    twoHolder.rv_combo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    twoHolder.storeDiscountTwoAdapters=new StoreDiscountTwoAdapters(context, false, mData.data);
                    twoHolder.storeDiscountTwoAdapters.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ComboDetailAct.startAct(context,mData.id,mData.data.get(position).id);
                        }
                    });
                }else {
                    twoHolder.storeDiscountTwoAdapters=new StoreDiscountTwoAdapters(context, false, mData.data);
                }
                twoHolder.rv_combo.setVisibility(View.VISIBLE);
                twoHolder.rv_combo.setAdapter(twoHolder.storeDiscountTwoAdapters);
            }
        }
    }

    class TwoHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_title, mtv_marketPrice, mtv_original;
        private RecyclerView rv_combo;
        private StoreDiscountTwoAdapters storeDiscountTwoAdapters;
        TwoHolder(View itemView) {
            super(itemView);
            mtv_title = (MyTextView) itemView.findViewById(R.id.mtv_title);
            mtv_marketPrice = (MyTextView) itemView.findViewById(R.id.mtv_marketPrice);
            mtv_original = (MyTextView) itemView.findViewById(R.id.mtv_original);
            rv_combo = (RecyclerView) itemView.findViewById(R.id.rv_combo);
        }
    }

}

