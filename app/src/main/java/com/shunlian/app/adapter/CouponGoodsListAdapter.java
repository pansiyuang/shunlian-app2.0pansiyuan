package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CouponListEntity;
import com.shunlian.app.bean.StageGoodsListEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/7/26.
 */

public class CouponGoodsListAdapter extends BaseRecyclerAdapter<StageGoodsListEntity.ItemGoods> {

    private CouponListEntity.VoucherList mVoucher_info;
    private int mTotalCount;
    public static final int ITEM_HEAD = 1<<3;

    public CouponGoodsListAdapter(Context context,
                                  List<StageGoodsListEntity.ItemGoods> lists,
                                  CouponListEntity.VoucherList voucher_info, int totalCount) {
        super(context, true, lists);
        mVoucher_info = voucher_info;
        mTotalCount = totalCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEAD){
            View view = mInflater.inflate(R.layout.item_coupon_head, parent, false);
            return new HeadHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
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
        baseFooterHolder.layout_no_more.setText("各位客官！已经到尽头啦!!");
        baseFooterHolder.layout_no_more.setTextSize(9);
        baseFooterHolder.layout_load_error.setTextSize(9);
        baseFooterHolder.mtv_loading.setTextSize(9);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return ITEM_HEAD;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ITEM_HEAD){
            handleHead(holder,position);
        }else {
            super.onBindViewHolder(holder, position);
        }
    }

    private void handleHead(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadHolder) {
            HeadHolder mHolder = (HeadHolder) holder;
            CouponListEntity.VoucherList voucherList = mVoucher_info;
            gone(mHolder.mtv_use_coupon);
            visible(mHolder.mtv_use_has);
            if (voucherList == null)return;

            //状态 0是未使用 1已经使用  -2已经过期
            String voucher_type_desc = voucherList.voucher_type_desc;
            changeState(voucherList.stauts, mHolder, voucher_type_desc, voucherList.expired);
            mHolder.mtv_rule.setText(voucher_type_desc);

            mHolder.mtv_price.setText(voucherList.denomination);
            if (Float.parseFloat(voucherList.use_condition) > 0) {
                mHolder.mtv_full_cut.setText(String.format(getString(R.string.voucher_full_use)
                        , voucherList.use_condition));
            } else {
                mHolder.mtv_full_cut.setText("无门槛");
            }
            mHolder.mtv_coupon_class.setText(voucherList.voucher_type);
            mHolder.mtv_coupon_time.setText("有效期：" + voucherList.valid_time);

            gone(mHolder.mtv_limit);
            mHolder.mtv_overdue.setText(voucherList.expire_after);
        }
    }

    private void changeState(String state, HeadHolder mHolder, String text, String expired){
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
            gone(mHolder.mtv_use_coupon);
            visible(mHolder.mtv_use_has);
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

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_voucher_goods,parent,false);
        return new VoucherGoods(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoucherGoods){
            VoucherGoods mHolder = (VoucherGoods) holder;

            StageGoodsListEntity.ItemGoods goods = lists.get(position - 1);

            mHolder.mtv_goods_count.setText("商品列表("+mTotalCount+")");
            if (position == 1){
                visible(mHolder.mtv_goods_count,mHolder.view_line1);
            }else {
                gone(mHolder.mtv_goods_count,mHolder.view_line1);
            }

            mHolder.view_line.setBackgroundColor(getColor(R.color.white));
            GlideUtils.getInstance().loadImage(context, mHolder.miv_icon, goods.thumb);
            if (mHolder.tv_title != null)
                mHolder.tv_title.setText(goods.title);
            String price = getString(R.string.common_yuan) + goods.price;
            mHolder.tv_price.setText(Common.changeTextSize(price, getString(R.string.common_yuan), 11));

            if ("1".equals(goods.free_ship)) {
                mHolder.tv_free.setVisibility(View.VISIBLE);
            } else {
                mHolder.tv_free.setVisibility(View.GONE);
            }

            mHolder.ll_tag.removeAllViews();

            if ("1".equals(goods.is_new)) {
                mHolder.ll_tag.addView(creatTextTag("新品", getColor(R.color.white),
                        getDrawable(R.drawable.rounded_corner_fbd500_2px), mHolder));
            }

            if ("1".equals(goods.is_hot)) {
                mHolder.ll_tag.addView(creatTextTag("热卖", getColor(R.color.white),
                        getDrawable(R.drawable.rounded_corner_fb9f00_2px), mHolder));
            }

            if ("1".equals(goods.is_explosion)) {
                mHolder.ll_tag.addView(creatTextTag("爆款", getColor(R.color.white),
                        getDrawable(R.drawable.rounded_corner_fb6400_2px), mHolder));
            }

            if ("1".equals(goods.is_recommend)) {
                mHolder.ll_tag.addView(creatTextTag("推荐", getColor(R.color.white),
                        getDrawable(R.drawable.rounded_corner_7898da_2px), mHolder));
            }

            if ("1".equals(goods.has_coupon)) {
                mHolder.ll_tag.addView(creatTextTag("劵", getColor(R.color.value_f46c6f),
                        getDrawable(R.drawable.rounded_corner_f46c6f_2px), mHolder));
            }

            if ("1".equals(goods.has_discount)) {
                mHolder.ll_tag.addView(creatTextTag("折", getColor(R.color.value_f46c6f),
                        getDrawable(R.drawable.rounded_corner_f46c6f_2px), mHolder));
            }

            if ("1".equals(goods.has_gift)) {
                mHolder.ll_tag.addView(creatTextTag("赠", getColor(R.color.value_f46c6f),
                        getDrawable(R.drawable.rounded_corner_f46c6f_2px), mHolder));
            }

            if ("0".equals(goods.comment_num)) {
                mHolder.tv_comment.setText("暂无评论");
            } else {
                if ("0".equals(goods.comment_rate)) {
                    mHolder.tv_comment.setText(goods.comment_num + "条评论");
                } else {
                    mHolder.tv_comment.setText(goods.comment_num + "条评论  " + goods.comment_rate + "%好评");
                }
            }
            mHolder.tv_address.setText(goods.send_area);
            /*if (goods.type == 1) { //是优品
                mHolder.miv_product.setVisibility(View.VISIBLE);
                if (!isEmpty(goods.self_buy_earn)) { //有字段才显示布局
                    mHolder.ll_earn.setVisibility(View.VISIBLE);
                    mHolder.tv_earn_money.setText(getString(R.string.common_yuan) + goods.self_buy_earn);
                } else {
                    mHolder.ll_earn.setVisibility(View.GONE);
                }
            } else {
                mHolder.ll_earn.setVisibility(View.GONE);
                mHolder.miv_product.setVisibility(View.GONE);
            }*/
            gone(mHolder.ll_earn,mHolder.miv_product,mHolder.tv_earn_money);
        }
    }

    public TextView creatTextTag(String content, int colorRes, Drawable drawable,
                                 VoucherGoods viewHolder) {
        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextSize(9);
        textView.setBackgroundDrawable(drawable);
        textView.setTextColor(colorRes);
        int padding = TransformUtil.dip2px(context, 3f);
        textView.setPadding(padding, 0, padding, 0);

        LinearLayout.LayoutParams params = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (viewHolder.ll_tag.getChildCount() == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(TransformUtil.dip2px(context, 5.5f), 0, 0, 0);
        }
        textView.setLayoutParams(params);
        return textView;
    }


    public class HeadHolder extends BaseRecyclerViewHolder{
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

        @BindView(R.id.mtv_use_has)
        MyTextView mtv_use_has;
        public HeadHolder(View itemView) {
            super(itemView);
        }
    }


    public class VoucherGoods extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_goods_count)
        MyTextView mtv_goods_count;

        @BindView(R.id.view_line1)
        View view_line1;


        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_product)
        MyImageView miv_product;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.ll_tag)
        LinearLayout ll_tag;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_free)
        TextView tv_free;

        @BindView(R.id.tv_comment)
        TextView tv_comment;

        @BindView(R.id.tv_address)
        TextView tv_address;

        @BindView(R.id.ll_earn)
        LinearLayout ll_earn;

        @BindView(R.id.tv_earn_money)
        TextView tv_earn_money;

        @BindView(R.id.view_line)
        View view_line;

        @BindView(R.id.lLayout_root)
        LinearLayout lLayout_root;

        public VoucherGoods(View itemView) {
            super(itemView);

            LinearLayout.LayoutParams
                    lp = (LinearLayout.LayoutParams) lLayout_root.getLayoutParams();
            lp.leftMargin = TransformUtil.dip2px(context, 10);
            lLayout_root.setLayoutParams(lp);

            itemView.setOnClickListener(view -> {
                StageGoodsListEntity.ItemGoods itemGoods = lists.get(getAdapterPosition() - 1);
                Common.goGoGo(context,"goods",itemGoods.goods_id);
            });
        }
    }
}
