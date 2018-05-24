package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.CouponListEntity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/19.
 */

public class CouponListAdapter extends BaseRecyclerAdapter<CouponListEntity.VoucherList> {

    public CouponListAdapter(Context context, List<CouponListEntity.VoucherList> lists) {
        super(context, true, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_conpou, parent, false);
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
        CouponListEntity.VoucherList voucherList = lists.get(position);
        //状态 0是未使用 1已经使用  -2已经过期
        String voucher_type_desc = voucherList.voucher_type_desc;
        changeState(voucherList.stauts,mHolder,voucher_type_desc,voucherList.expired);
        mHolder.mtv_rule.setText(voucher_type_desc);

        mHolder.mtv_price.setText(voucherList.denomination);
        if (Float.parseFloat(voucherList.use_condition) > 0) {
            mHolder.mtv_full_cut.setText(String.format(getString(R.string.voucher_full_use)
                    , voucherList.use_condition));
        }else {
            mHolder.mtv_full_cut.setText("无门槛");
        }
        mHolder.mtv_coupon_class.setText(voucherList.voucher_type);
        mHolder.mtv_coupon_time.setText("有效期："+voucherList.valid_time);

        /*String goods_scope = voucherList.goods_scope;
        if ("1".equals(goods_scope)){
            goods_scope = "全场使用";
        }else {
            goods_scope = "APP使用";
        }
        mHolder.mtv_limit.setText(goods_scope);*/
        gone(mHolder.mtv_limit);
        mHolder.mtv_overdue.setText(voucherList.expire_after);

    }

    private void changeState(String state, CouponListHolder mHolder, String text, String expired){
        //状态 0是未使用 1已经使用    expired 是否过期  1为已经过期
        if (isEmpty(text)){
            text = "";
        }
        int length = text.length();
        int pink_color = 0;
        int new_text = 0;
        int value_555555 = 0;

        if ("1".equals(state) || "1".equals(expired)){//已使用  已过期
            pink_color = getColor(R.color.share_text);
            new_text = pink_color;
            value_555555 = pink_color;
            gone(mHolder.mtv_use_coupon);

            if ("1".equals(state)){//已使用
                if (length > 26){
                    mHolder.rlayout_root.setBackgroundResource(R.mipmap.img_youhuiquan_gao);
                }else {
                    mHolder.rlayout_root.setBackgroundResource(R.mipmap.img_youhuiquan_di);
                }
            }else if ("1".equals(expired)){//已过期
                if (length > 26){
                    mHolder.rlayout_root.setBackgroundResource(R.mipmap.img_youhuiquan_guoqi_gao);
                }else {
                    mHolder.rlayout_root.setBackgroundResource(R.mipmap.img_youhuiquan_guoqi_di);
                }
            }
        }else {//未使用
            pink_color = getColor(R.color.pink_color);
            new_text = getColor(R.color.new_text);
            value_555555 = getColor(R.color.value_555555);
            visible(mHolder.mtv_use_coupon);
            if (length > 26){
                mHolder.rlayout_root.setBackgroundResource(R.mipmap.img_youhuiquan_gao);
            }else {
                mHolder.rlayout_root.setBackgroundResource(R.mipmap.img_youhuiquan_di);
            }
        }
        mHolder.mtv_rmb.setTextColor(pink_color);
        mHolder.mtv_price.setTextColor(pink_color);
        mHolder.mtv_overdue.setTextColor(pink_color);

        mHolder.mtv_coupon_class.setTextColor(new_text);

        mHolder.mtv_full_cut.setTextColor(value_555555);
        mHolder.mtv_coupon_time.setTextColor(value_555555);
        mHolder.mtv_rule.setTextColor(value_555555);
        mHolder.mtv_limit.setTextColor(value_555555);
    }

    public class CouponListHolder extends BaseRecyclerViewHolder{

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

        @BindView(R.id.mtv_limit)
        MyTextView mtv_limit;

        @BindView(R.id.rlayout_root)
        RelativeLayout rlayout_root;

        public CouponListHolder(View itemView) {
            super(itemView);
            mtv_use_coupon.setOnClickListener((v)->{
                CouponListEntity.VoucherList voucherList = lists.get(getAdapterPosition());
                if ("0".equals(voucherList.store_id)){//平台的
                    Common.goGoGo(context,"");
                }else {
                    StoreAct.startAct(context,voucherList.store_id);
                }
            });
        }
    }
}
