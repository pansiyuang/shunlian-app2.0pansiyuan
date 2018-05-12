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

import butterknife.BindView;

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

    class TwoHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.mtv_text)
        MyTextView mtv_text;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        TwoHolder(View itemView) {
            super(itemView);
        }
    }

}

