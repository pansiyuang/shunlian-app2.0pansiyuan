package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.Common.firstSmallText;

/**
 * Created by Administrator on 2017/11/16.
 */

public class VoucherAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Voucher> {
    private List<GoodsDeatilEntity.Voucher> mData;
    private Context mContext;
    private OnVoucherSelectCallBack mCallBack;

    public VoucherAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Voucher> lists) {
        super(context, isShowFooter, lists);
        this.mContext = context;
        this.mData = lists;
    }

    public void setData(List<GoodsDeatilEntity.Voucher> vouchers) {
        this.mData = vouchers;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        VoucherViewHolder voucherViewHolder = new VoucherViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_voucher, parent, false));
        return voucherViewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        final GoodsDeatilEntity.Voucher voucher = mData.get(position);
        final VoucherViewHolder voucherViewHolder = (VoucherViewHolder) holder;
        String price = mContext.getResources().getString(R.string.rmb) + voucher.denomination;
        firstSmallText(voucherViewHolder.tv_voucher_price, price, 13);

        voucherViewHolder.tv_voucher_title.setText(String.format(mContext.getResources().getString(R.string.voucher_full_use), voucher.use_condition));
        voucherViewHolder.tv_voucher_date.setText(String.format(mContext.getResources().getString(R.string.valid_date), voucher.start_time, voucher.end_time));

        if ("ALL".equals(voucher.goods_scope)) { //全店通用卷
            voucherViewHolder.tv_voucher_use.setText(mContext.getResources().getText(R.string.all_store_use));
        } else if ("ASSIGN".equals(voucher.goods_scope)) {
            // TODO: 2017/11/16
        }

        if ("1".equals(voucher.is_get)) {  //1为已经领取
            voucherViewHolder.ll_voucher.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.img_dianpu_youhuiquan_n));
            voucherViewHolder.tv_draw.setText(mContext.getResources().getText(R.string.hava_received));
            voucherViewHolder.tv_draw.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_line_gray));
        } else {
            voucherViewHolder.ll_voucher.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.img_dianpu_youhuiquan_h));
            voucherViewHolder.tv_draw.setText(mContext.getResources().getText(R.string.receive));
            voucherViewHolder.tv_draw.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_line_pink));
        }
        voucherViewHolder.tv_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.OnVoucherSelect(voucher);
                }
            }
        });
    }

    public class VoucherViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.ll_voucher)
        LinearLayout ll_voucher;

        @BindView(R.id.tv_voucher_price)
        TextView tv_voucher_price;

        @BindView(R.id.tv_voucher_title)
        TextView tv_voucher_title;

        @BindView(R.id.tv_voucher_use)
        TextView tv_voucher_use;

        @BindView(R.id.tv_voucher_date)
        TextView tv_voucher_date;

        @BindView(R.id.tv_draw)
        TextView tv_draw;


        public VoucherViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public void setOnVoucherSelectCallBack(OnVoucherSelectCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnVoucherSelectCallBack {
        void OnVoucherSelect(GoodsDeatilEntity.Voucher voucher);
    }
}
