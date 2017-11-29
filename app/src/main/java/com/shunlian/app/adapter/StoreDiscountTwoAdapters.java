package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StorePromotionGoodsListTwoEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreDiscountTwoAdapters extends BaseRecyclerAdapter<StorePromotionGoodsListTwoEntity.Lists.Good.Data> {
    private Context context;
    private List<StorePromotionGoodsListTwoEntity.Lists.Good.Data> mDatas;

    public StoreDiscountTwoAdapters(Context context, boolean isShowFooter, List<StorePromotionGoodsListTwoEntity.Lists.Good.Data> mDatas) {
        super(context, isShowFooter, mDatas);
        this.context = context;
        this.mDatas = mDatas;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount_combos, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoHolder) {
            TwoHolder twoHolder = (TwoHolder) holder;
            StorePromotionGoodsListTwoEntity.Lists.Good.Data mData = mDatas.get(position);
            GlideUtils.getInstance().loadImage(context, twoHolder.miv_img, mData.whole_thumb);
            twoHolder.mtv_text.setText(mData.title);
        }
    }

    class TwoHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_text;
        private MyImageView miv_img;

        TwoHolder(View itemView) {
            super(itemView);
            mtv_text = (MyTextView) itemView.findViewById(R.id.mtv_text);
            miv_img = (MyImageView) itemView.findViewById(R.id.miv_img);
        }
    }

}

