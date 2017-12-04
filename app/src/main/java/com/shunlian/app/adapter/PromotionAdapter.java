package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/15.
 */

public class PromotionAdapter extends BaseRecyclerAdapter<ConfirmOrderEntity.PromotionInfo> {

    public PromotionAdapter(Context context, boolean isShowFooter, List<ConfirmOrderEntity.PromotionInfo> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        PromotionHolder viewHolder = new PromotionHolder(LayoutInflater.from(context).inflate(R.layout.item_dialog_combo, parent, false));
        return viewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromotionHolder){
            PromotionHolder mHolder = (PromotionHolder) holder;
            ConfirmOrderEntity.PromotionInfo promotionInfo = lists.get(position);
            mHolder.mtv_discount.setText(promotionInfo.prom_label);
            mHolder.tv_market_detail.setText(promotionInfo.prom_title);
            mHolder.mtv_already_discounted.setText(promotionInfo.prom_hint);

            PromotionPicAdapter adapter = new PromotionPicAdapter(context,false,promotionInfo.goods);
            mHolder.recycler_combo.setAdapter(adapter);
        }

    }

    public class PromotionHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.tv_combo_price)
        TextView tv_combo_price;

        @BindView(R.id.tv_market_price)
        TextView tv_market_price;

        @BindView(R.id.recycler_combo)
        RecyclerView recycler_combo;

        @BindView(R.id.ll_discounted)
        LinearLayout ll_discounted;

        @BindView(R.id.mtv_already_discounted)
        MyTextView mtv_already_discounted;

        @BindView(R.id.mtv_discount)
        MyTextView mtv_discount;

        @BindView(R.id.tv_market_detail)
        TextView tv_market_detail;

        @BindView(R.id.ll_combo)
        LinearLayout ll_combo;

        @BindView(R.id.view_line)
        View view_line;

        public PromotionHolder(View itemView) {
            super(itemView);
            ll_combo.setVisibility(View.GONE);
            view_line.setVisibility(View.GONE);
            ll_discounted.setVisibility(View.VISIBLE);
            mtv_already_discounted.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            recycler_combo.setLayoutManager(manager);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycler_combo.getLayoutParams();
            layoutParams.leftMargin = TransformUtil.dip2px(context,10);
            recycler_combo.setLayoutParams(layoutParams);

        }
    }

    public class PromotionPicAdapter extends BaseRecyclerAdapter<ConfirmOrderEntity.Goods> {

        public PromotionPicAdapter(Context context, boolean isShowFooter, List<ConfirmOrderEntity.Goods> lists) {
            super(context, isShowFooter, lists);
        }

        @Override
        protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
            PicViewHolder viewHolder = new PicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_combo_pic, parent, false));
            return viewHolder;
        }

        @Override
        public void handleList(RecyclerView.ViewHolder holder, int position) {
            PicViewHolder mHoler = (PicViewHolder) holder;
            ConfirmOrderEntity.Goods goods = lists.get(position);
            GlideUtils.getInstance().loadImage(context, mHoler.miv_combo, goods.thumb);
            mHoler.mtv_price.setText(Common.dotAfterSmall(getString(R.string.rmb)+goods.price,11));
        }

        public class PicViewHolder extends BaseRecyclerViewHolder{
            @BindView(R.id.miv_combo)
            MyImageView miv_combo;

            @BindView(R.id.mtv_price)
            MyTextView mtv_price;
            public PicViewHolder(View itemView) {
                super(itemView);
                mtv_price.setVisibility(View.VISIBLE);
            }
        }
    }
}
