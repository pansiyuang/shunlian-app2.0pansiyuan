package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StorePromotionGoodsListOneEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreDiscountOneAdapter extends BaseRecyclerAdapter<StorePromotionGoodsListOneEntity.MData> {
    private static final int TYPE2 = 2;//文本
    private static final int TYPE3 = 3;//商品
    private Context context;
    private List<StorePromotionGoodsListOneEntity.MData> mDatas;

    public StoreDiscountOneAdapter(Context context, boolean isShowFooter, List<StorePromotionGoodsListOneEntity.MData> mDatas) {
        super(context, isShowFooter, mDatas);
        this.context = context;
        this.mDatas = mDatas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE3:
                return new ThreeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_discount_two, parent, false));
            case TYPE2:
                return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_discount_one, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new DefaultHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_default, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mDatas.size()) {
            if ("3".equals(mDatas.get(position).type)) {
                return TYPE3;
            } else {
                return TYPE2;
            }
        }
        return super.getItemViewType(position);
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE3:
                if (holder instanceof ThreeHolder) {
                    ThreeHolder threeHolder = (ThreeHolder) holder;
                    final StorePromotionGoodsListOneEntity.MData mData = mDatas.get(position);
                    threeHolder.mtv_descl.setText(mData.ldata.title);
                    threeHolder.mtv_pricel.setText(mData.ldata.price);
                    threeHolder.mtv_pricer.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
                    threeHolder.mtv_pricer.setText(context.getResources().getString(R.string.common_yuan) + mData.ldata.market_price);
                    GlideUtils.getInstance().loadImage(context, threeHolder.miv_onel, mData.ldata.whole_thumb);
                    if (TextUtils.isEmpty(mData.rdata.title) && TextUtils.isEmpty(mData.rdata.whole_thumb)) {
                        threeHolder.mllayout_oner.setVisibility(View.INVISIBLE);
                    } else {
                        threeHolder.mllayout_oner.setVisibility(View.VISIBLE);
                        threeHolder.mtv_descr.setText(mData.rdata.title);
                        threeHolder.mtv_pricels.setText(mData.rdata.price);
                        threeHolder.mtv_pricers.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
                        threeHolder.mtv_pricers.setText(context.getResources().getString(R.string.common_yuan) + mData.ldata.market_price);
                        GlideUtils.getInstance().loadImage(context, threeHolder.miv_oner, mData.rdata.whole_thumb);
                    }
                    if (TextUtils.isEmpty(mData.ldata.giftGoodsName)) {
                        threeHolder.mllayout_l.setVisibility(View.GONE);
                    }else {
                        threeHolder.mllayout_l.setVisibility(View.VISIBLE);
                        threeHolder.mtv_zengl.setText(mData.ldata.giftGoodsName);
                    }
                    if (TextUtils.isEmpty(mData.rdata.giftGoodsName)) {
                        threeHolder.mllayout_r.setVisibility(View.GONE);
                    }else {
                        threeHolder.mllayout_r.setVisibility(View.VISIBLE);
                        threeHolder.mtv_zengr.setText(mData.rdata.giftGoodsName);
                    }
                    threeHolder.mllayout_onel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GoodsDetailAct.startAct(context,mData.ldata.id);
                        }
                    });
                    threeHolder.mllayout_oner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GoodsDetailAct.startAct(context,mData.rdata.id);
                        }
                    });
                }
                break;
            case TYPE2:
                if (holder instanceof TwoHolder) {
                    TwoHolder twoHolder = (TwoHolder) holder;
                    StorePromotionGoodsListOneEntity.MData mData = mDatas.get(position);
                    twoHolder.mtv_desc.setText(mData.mRemark);
                }
                break;
        }
    }

    class ThreeHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_zengl,mtv_zengr,mtv_descl, mtv_pricel, mtv_pricer, mtv_descr, mtv_pricels, mtv_pricers;
        private MyImageView miv_onel, miv_oner;
        private MyLinearLayout mllayout_oner,mllayout_l,mllayout_r,mllayout_onel;


        ThreeHolder(View itemView) {
            super(itemView);
            miv_onel = (MyImageView) itemView.findViewById(R.id.miv_onel);
            mtv_descl = (MyTextView) itemView.findViewById(R.id.mtv_descl);
            mtv_zengl = (MyTextView) itemView.findViewById(R.id.mtv_zengl);
            mtv_zengr = (MyTextView) itemView.findViewById(R.id.mtv_zengr);
            mtv_pricel = (MyTextView) itemView.findViewById(R.id.mtv_pricel);
            mtv_pricer = (MyTextView) itemView.findViewById(R.id.mtv_pricer);
            miv_oner = (MyImageView) itemView.findViewById(R.id.miv_oner);
            mtv_descr = (MyTextView) itemView.findViewById(R.id.mtv_descr);
            mtv_pricels = (MyTextView) itemView.findViewById(R.id.mtv_pricels);
            mtv_pricers = (MyTextView) itemView.findViewById(R.id.mtv_pricers);
            mllayout_oner = (MyLinearLayout) itemView.findViewById(R.id.mllayout_oner);
            mllayout_l = (MyLinearLayout) itemView.findViewById(R.id.mllayout_l);
            mllayout_r = (MyLinearLayout) itemView.findViewById(R.id.mllayout_r);
            mllayout_onel = (MyLinearLayout) itemView.findViewById(R.id.mllayout_onel);
        }
    }

    class TwoHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_desc;


        TwoHolder(View itemView) {
            super(itemView);
            mtv_desc = (MyTextView) itemView.findViewById(R.id.mtv_desc);
        }
    }

    class DefaultHolder extends RecyclerView.ViewHolder {

        DefaultHolder(View itemView) {
            super(itemView);
        }
    }
}
