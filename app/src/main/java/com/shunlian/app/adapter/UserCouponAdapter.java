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
import com.shunlian.app.bean.StageVoucherGoodsListEntity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/7/24.
 */

public class UserCouponAdapter extends BaseRecyclerAdapter<StageVoucherGoodsListEntity.GoodsList> {

    public static final int ITEM_HEAD = 1<<3;
    private CouponListEntity.VoucherList mVoucher_info;
    private final int margin10;
    private IMoreGoodsListener moreGoodsListener;

    public UserCouponAdapter(Context context,List<StageVoucherGoodsListEntity.GoodsList> lists,
                             CouponListEntity.VoucherList voucher_info) {
        super(context, true, lists);
        mVoucher_info = voucher_info;
        margin10 = TransformUtil.dip2px(context, 10);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEAD){
            View view = mInflater.inflate(R.layout.item_stage_coupon, parent, false);
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
        View view = mInflater.inflate(R.layout.item_coupon_goods, parent, false);
        return new CouponGoodsHolder(view);
    }

    /**
     * 处理列表
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CouponGoodsHolder){
            CouponGoodsHolder mHolder = (CouponGoodsHolder) holder;
            if (position == 1){
                visible(mHolder.view_line);
                gone(mHolder.view_line1);
            }else {
                gone(mHolder.view_line);
                visible(mHolder.view_line1);
            }
            StageVoucherGoodsListEntity.GoodsList goodsList = lists.get(position - 1);

            GlideUtils.getInstance().loadOverrideImage(context,
                    mHolder.miv_storeLogo,goodsList.avatar,80,80);
            StageVoucherGoodsListEntity.Level level = goodsList.level;
            if (level != null){
                if (!isEmpty(level.path)){
                    visible(mHolder.miv_storeLevel);
                    GlideUtils.getInstance().loadImage(context,mHolder.miv_storeLevel,level.path);
                }else {
                    gone(mHolder.miv_storeLevel);
                }
            }
            GlideUtils.getInstance().loadImage(context,mHolder.miv_storeLogo,goodsList.avatar);
            mHolder.mtv_storeName.setText(goodsList.name);
            if ("1".equals(goodsList.show_more)){
                visible(mHolder.mtv_more_goods);
            }else {
                gone(mHolder.mtv_more_goods);
            }

            List<StageVoucherGoodsListEntity.ItemGoods> goods_list = goodsList.goods_list;
            if (!isEmpty(goods_list)){
                visible(mHolder.llayout_goods);
                mHolder.llayout_goods.removeAllViews();
                for (int i = 0; i < goods_list.size(); i++) {
                    StageVoucherGoodsListEntity.ItemGoods itemGoods = goods_list.get(i);
                    View view = addView(itemGoods, mHolder.llayout_goods);
                    mHolder.llayout_goods.addView(view);
                }
            }else {
                gone(mHolder.llayout_goods);
            }
        }
    }

    private View addView(StageVoucherGoodsListEntity.ItemGoods itemGoods, LinearLayout llayout_goods) {
        View view = mInflater.inflate(R.layout.item_category_single, llayout_goods,false);

        view.setOnClickListener(view1 -> Common.goGoGo(context,"goods",itemGoods.goods_id));

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.leftMargin = margin10;
        lp.rightMargin = margin10;
        view.setLayoutParams(lp);

        View view_line = view.findViewById(R.id.view_line);
        view_line.setBackgroundColor(getColor(R.color.white));

        MyImageView miv_icon =  view.findViewById(R.id.miv_icon);
        MyImageView miv_product =  view.findViewById(R.id.miv_product);
        TextView tv_title =  view.findViewById(R.id.tv_title);
        TextView tv_price =  view.findViewById(R.id.tv_price);
        TextView tv_free =  view.findViewById(R.id.tv_free);
        TextView tv_comment =  view.findViewById(R.id.tv_comment);
        TextView tv_address =  view.findViewById(R.id.tv_address);
        TextView tv_earn_money =  view.findViewById(R.id.tv_earn_money);
        LinearLayout ll_earn =  view.findViewById(R.id.ll_earn);
        LinearLayout ll_tag =  view.findViewById(R.id.ll_tag);

        GlideUtils.getInstance().loadImage(context, miv_icon, itemGoods.thumb);
        tv_title.setText(itemGoods.title);
        String price = getString(R.string.common_yuan) + itemGoods.price;
        tv_price.setText(Common.changeTextSize(price, getString(R.string.common_yuan), 11));

        if ("1".equals(itemGoods.free_ship)) {
            tv_free.setVisibility(View.VISIBLE);
        } else {
            tv_free.setVisibility(View.GONE);
        }

        ll_tag.removeAllViews();

        if ("1".equals(itemGoods.is_new)) {
            ll_tag.addView(creatTextTag("新品", getColor(R.color.white),
                    getDrawable(R.drawable.rounded_corner_fbd500_2px), ll_tag));
        }

        if ("1".equals(itemGoods.is_hot)) {
            ll_tag.addView(creatTextTag("热卖", getColor(R.color.white),
                    getDrawable(R.drawable.rounded_corner_fb9f00_2px), ll_tag));
        }

        if ("1".equals(itemGoods.is_explosion)) {
            ll_tag.addView(creatTextTag("爆款", getColor(R.color.white),
                    getDrawable(R.drawable.rounded_corner_fb6400_2px), ll_tag));
        }

        if ("1".equals(itemGoods.is_recommend)) {
            ll_tag.addView(creatTextTag("推荐", getColor(R.color.white),
                    getDrawable(R.drawable.rounded_corner_7898da_2px), ll_tag));
        }

        if ("1".equals(itemGoods.has_coupon)) {
            ll_tag.addView(creatTextTag("劵", getColor(R.color.value_f46c6f),
                    getDrawable(R.drawable.rounded_corner_f46c6f_2px), ll_tag));
        }

        if ("1".equals(itemGoods.has_discount)) {
            ll_tag.addView(creatTextTag("折", getColor(R.color.value_f46c6f),
                    getDrawable(R.drawable.rounded_corner_f46c6f_2px), ll_tag));
        }

        if ("1".equals(itemGoods.has_gift)) {
            ll_tag.addView(creatTextTag("赠", getColor(R.color.value_f46c6f),
                    getDrawable(R.drawable.rounded_corner_f46c6f_2px), ll_tag));
        }

        if ("0".equals(itemGoods.comment_num)) {
            tv_comment.setText("暂无评论");
        } else {
            if ("0".equals(itemGoods.comment_rate)) {
                tv_comment.setText(itemGoods.comment_num + "条评论");
            } else {
                tv_comment.setText(itemGoods.comment_num + "条评论  " + itemGoods.comment_rate + "%好评");
            }
        }
        tv_address.setText(itemGoods.send_area);
        gone(ll_earn,miv_product,tv_earn_money);
        return view;
    }

    public TextView creatTextTag(String content, int colorRes, Drawable drawable,LinearLayout ll_tag) {
        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextSize(9);
        textView.setBackgroundDrawable(drawable);
        textView.setTextColor(colorRes);
        int padding = TransformUtil.dip2px(context, 3f);
        textView.setPadding(padding, 0, padding, 0);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (ll_tag.getChildCount() == 0) {
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
            gone(mtv_use_coupon);
        }
    }

    public class CouponGoodsHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.view_line)
        View view_line;

        @BindView(R.id.miv_storeLogo)
        MyImageView miv_storeLogo;

        @BindView(R.id.mtv_storeName)
        MyTextView mtv_storeName;

        @BindView(R.id.miv_storeLevel)
        MyImageView miv_storeLevel;

        @BindView(R.id.rlayout_go_store)
        RelativeLayout rlayout_go_store;

        @BindView(R.id.llayout_goods)
        LinearLayout llayout_goods;

        @BindView(R.id.mtv_more_goods)
        MyTextView mtv_more_goods;

        @BindView(R.id.view_line1)
        View view_line1;

        public CouponGoodsHolder(View itemView) {
            super(itemView);
            rlayout_go_store.setOnClickListener(view -> {
                StageVoucherGoodsListEntity.GoodsList
                        goodsList = lists.get(getAdapterPosition() - 1);
                String store_id = goodsList.store_id;
                if (!isEmpty(store_id)){
                    StoreAct.startAct(context,store_id);
                }
            });


            mtv_more_goods.setOnClickListener(view -> {
                if (moreGoodsListener != null)
                    moreGoodsListener.onMoreGoods(getAdapterPosition()-1);
            });
        }
    }

    public void setMoreGoodsListener(IMoreGoodsListener moreGoodsListener){

        this.moreGoodsListener = moreGoodsListener;
    }

    public interface IMoreGoodsListener{
        void onMoreGoods(int position);
    }
}
