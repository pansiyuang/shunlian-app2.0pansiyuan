package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.RefundListEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.CustomerGoodsView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RefundAfterSaleAdapter extends BaseRecyclerAdapter<RefundListEntity.RefundList> {

    public RefundAfterSaleAdapter(Context context, boolean isShowFooter, List<RefundListEntity.RefundList> lists) {
        super(context, isShowFooter, lists);
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_order));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_refund_after_sale, parent, false);
        return new RefundAfterSaleHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RefundAfterSaleHolder){
            RefundAfterSaleHolder mHolder = (RefundAfterSaleHolder) holder;
            RefundListEntity.RefundList refundList = lists.get(position);
            mHolder.cgv_goods.setLabelName(refundList.store_name,true)
            .setGoodsTitle(refundList.title).setGoodsParams(refundList.sku_desc)
            .setGoodsCount(String.format(getString(R.string.x),refundList.goods_num))
                    .setGoodsPrice(getString(R.string.rmb).concat(refundList.price))
            .setRefundPrice(getString(R.string.refund_balance)
                    .concat(getString(R.string.rmb).concat(refundList.refund_amount))).setIsArrow(true);
            GlideUtils.getInstance().loadImage(context,mHolder.cgv_goods.getGoodsIcon(),refundList.thumb);
            mHolder.mtv_label.setText(refundList.status_msg);
            GlideUtils.getInstance().loadImage(context,mHolder.miv_icon,refundList.type_icon);

        }
    }

    public class RefundAfterSaleHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.cgv_goods)
        CustomerGoodsView cgv_goods;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.mtv_label)
        MyTextView mtv_label;

        @BindView(R.id.mtv_look_detail)
        MyTextView mtv_look_detail;

        public RefundAfterSaleHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            GradientDrawable background = (GradientDrawable) mtv_look_detail.getBackground();
            background.setColor(getColor(R.color.white));
            background.setStroke(TransformUtil.dip2px(context,0.5f),getColor(R.color.pink_color));
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
