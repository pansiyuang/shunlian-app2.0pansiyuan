package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public class StoreVoucherAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Voucher> {
    private List<GoodsDeatilEntity.Voucher> mData;
    private Context mContext;
    public int detailBottomCouponPosition = -1;//详情下的优惠券位置

    public StoreVoucherAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Voucher> lists) {
        super(context, isShowFooter, lists);
        this.mContext = context;
        this.mData = lists;
    }

    public void setData(List<GoodsDeatilEntity.Voucher> vouchers) {
        this.mData = vouchers;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new VoucherViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_coupon, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoucherViewHolder) {
            VoucherViewHolder voucherViewHolder = (VoucherViewHolder) holder;
            GoodsDeatilEntity.Voucher voucher = mData.get(position);
            voucherViewHolder.mtv_price.setText(voucher.denomination);
            voucherViewHolder.mtv_name.setText("优惠券");
            String format = "满%s元可用";
            if (!isEmpty(voucher.use_condition) &&
                    Float.parseFloat(voucher.use_condition) == 0){
                voucherViewHolder.mtv_full_cut.setText(getString(R.string.no_doorsill_voucher));
            }else {
                voucherViewHolder.mtv_full_cut.setText(String.format(format, voucher.use_condition));
            }
            if ("1".equals(voucher.is_get)) {
                voucherViewHolder.mrl_bg.setBackgroundResource(R.mipmap.img_youhuiquan_2);
            } else {
                voucherViewHolder.mrl_bg.setBackgroundResource(R.mipmap.img_youhuiquan_1);
            }
        }
    }

    public class VoucherViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        private MyTextView mtv_price, mtv_name, mtv_full_cut;
        private MyLinearLayout mrl_bg;

        public VoucherViewHolder(View itemView) {
            super(itemView);
            mtv_price = (MyTextView) itemView.findViewById(R.id.mtv_price);
            mtv_name = (MyTextView) itemView.findViewById(R.id.mtv_name);
            mtv_full_cut = (MyTextView) itemView.findViewById(R.id.mtv_full_cut);
            mrl_bg = (MyLinearLayout) itemView.findViewById(R.id.mrl_bg);
            itemView.setOnClickListener(this);
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
