package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.presenter.PGetCoupon;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/19.
 */

public class NewCouponListAdapter extends BaseRecyclerAdapter<VouchercenterplEntity.MData> {
    private PGetCoupon pGetCoupon;

    public NewCouponListAdapter(Context context, List<VouchercenterplEntity.MData> lists, PGetCoupon pGetCoupon) {
        super(context, false, lists);
        this.pGetCoupon = pGetCoupon;
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_coupon_new, parent, false);
        return new CouponListHolder(view);
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
//        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_order));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        CouponListHolder mHolder = (CouponListHolder) holder;
        VouchercenterplEntity.MData voucherList = lists.get(position);
//        mHolder.mtv_rule.setText(voucherList.title);

        mHolder.mtv_price.setText(voucherList.denomination);
        mHolder.mtv_full_cut.setText(voucherList.use_condition);
        mHolder.mtv_coupon_class.setText(voucherList.title);
        mHolder.mtv_coupon_time.setText("有效期：" + voucherList.valid_date);

        /*String goods_scope = voucherList.goods_scope;
        if ("1".equals(goods_scope)){
            goods_scope = "全场使用";
        }else {
            goods_scope = "APP使用";
        }
        mHolder.mtv_limit.setText(goods_scope);*/
        gone(mHolder.mtv_limit);
        mHolder.mtv_overdue.setText(voucherList.end_time_text);
        if (isEmpty(voucherList.tag)) {
            mHolder.miv_act.setVisibility(View.GONE);
        } else {
            mHolder.miv_act.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImageZheng(context, mHolder.miv_act, voucherList.tag);
        }
        if ("0".equals(voucherList.if_get)) {
            mHolder.mtv_use_coupon.setText(getString(R.string.chat_lijilingqu));
            mHolder.mtv_use_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pGetCoupon.getVoucher(voucherList.id, true, position);
                }
            });
        } else {
            mHolder.mtv_use_coupon.setText(getString(R.string.coupon_lijishiyong));
            mHolder.mtv_use_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.goGoGo(context, voucherList.jump_type, voucherList.lazy_id);
                }
            });
        }


    }


    public class CouponListHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_rmb)
        MyTextView mtv_rmb;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_full_cut)
        MyTextView mtv_full_cut;

        @BindView(R.id.mtv_coupon_class)
        MyTextView mtv_coupon_class;

        @BindView(R.id.mtv_overdue)
        MyTextView mtv_overdue;

        @BindView(R.id.mtv_coupon_time)
        MyTextView mtv_coupon_time;

        @BindView(R.id.mtv_use_coupon)
        MyTextView mtv_use_coupon;

        @BindView(R.id.mtv_rule)
        MyTextView mtv_rule;

        @BindView(R.id.miv_act)
        MyImageView miv_act;

        @BindView(R.id.mtv_limit)
        MyTextView mtv_limit;

        @BindView(R.id.rlayout_root)
        RelativeLayout rlayout_root;

        public CouponListHolder(View itemView) {
            super(itemView);
        }
    }
}
