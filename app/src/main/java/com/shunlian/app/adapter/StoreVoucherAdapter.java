package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public class StoreVoucherAdapter extends BaseRecyclerAdapter<StoreIndexEntity.Voucher> {
    private List<StoreIndexEntity.Voucher> mData;
    private Context mContext;

    public StoreVoucherAdapter(Context context, boolean isShowFooter, List<StoreIndexEntity.Voucher> lists) {
        super(context, isShowFooter, lists);
        this.mContext = context;
        this.mData = lists;
    }

    public void setData(List<StoreIndexEntity.Voucher> vouchers) {
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
            StoreIndexEntity.Voucher voucher = mData.get(position);
            voucherViewHolder.mtv_price.setText(voucher.denomination);
            voucherViewHolder.mtv_name.setText(voucher.title);
            String format = "满%s元可用";
            voucherViewHolder.mtv_full_cut.setText(String.format(format, voucher.use_condition));
            if ("0".equals(voucher.is_get)) {
                voucherViewHolder.mrl_bg.setBackgroundResource(R.mipmap.img_youhuiquan_2);
            } else {
                voucherViewHolder.mrl_bg.setBackgroundResource(R.mipmap.img_youhuiquan_1);
            }
        }
    }

    public class VoucherViewHolder extends BaseRecyclerViewHolder {
        private MyTextView mtv_price, mtv_name, mtv_full_cut;
        private MyRelativeLayout mrl_bg;

        public VoucherViewHolder(View itemView) {
            super(itemView);
            mtv_price = (MyTextView) itemView.findViewById(R.id.mtv_price);
            mtv_name = (MyTextView) itemView.findViewById(R.id.mtv_name);
            mtv_full_cut = (MyTextView) itemView.findViewById(R.id.mtv_full_cut);
            mrl_bg = (MyRelativeLayout) itemView.findViewById(R.id.mrl_bg);
        }
    }
}
