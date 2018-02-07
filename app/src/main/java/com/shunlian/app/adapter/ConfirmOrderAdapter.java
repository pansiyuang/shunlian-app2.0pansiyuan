package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.receive_adress.AddressListActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.widget.DiscountListDialog;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.RecyclerDialog;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/25.
 */

public class ConfirmOrderAdapter extends BaseRecyclerAdapter<ConfirmOrderEntity.Enabled> {

    public static final int ITEM_ADDRESS = 2;//地址条目
    public static final int ITEM_INVALID = 3;//失效商品
    public static final int ITEM_STATION = 4;//占位条目
    private List<GoodsDeatilEntity.Goods> disabled;
    private ConfirmOrderEntity.Address mAddress;
    private boolean mIsOrderBuy;
    private ISelectVoucherListener mListener;

    public ConfirmOrderAdapter(Context context, boolean isShowFooter,
                               List<ConfirmOrderEntity.Enabled> lists,
                               List<GoodsDeatilEntity.Goods> disabled,
                               ConfirmOrderEntity.Address address, boolean isOrderBuy) {
        super(context, isShowFooter, lists);
        this.disabled = disabled;
        mAddress = address;
        mIsOrderBuy = isOrderBuy;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return ITEM_ADDRESS;
        }else if (position + 1  == getItemCount()){
            return ITEM_STATION;
        }else if (position + 2 == getItemCount() && isHasInvalid()){
            return ITEM_INVALID;
        }else {
            return super.getItemViewType(position);
        }
    }

    /**
     * 是否有失效商品
     * @return
     */
    private boolean isHasInvalid(){
        return disabled != null && disabled.size() > 0;
    }

    @Override
    public int getItemCount() {
        if (isHasInvalid()){
            return super.getItemCount() + 3;
        }else {
            return super.getItemCount() + 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM_ADDRESS:
                View head_address = LayoutInflater.from(context)
                        .inflate(R.layout.head_address, parent, false);
                return new AddressHolder(head_address);
            case ITEM_INVALID:
                View invalid_layout = LayoutInflater.from(context)
                        .inflate(R.layout.only_recycler_layout, parent, false);
                return new InvalidGoodsHolder(invalid_layout);
            case ITEM_STATION:
                View station_layout = LayoutInflater.from(context)
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
                return new StationHolder(station_layout);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case ITEM_ADDRESS:
                handleAddress(holder,position);
                break;
            case ITEM_INVALID:
                handlerInvalidGoods(holder,position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void handleAddress(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddressHolder){
            AddressHolder mHolder = (AddressHolder) holder;
            if (mAddress == null){
                mHolder.mtv_add_address.setVisibility(View.VISIBLE);
                mHolder.mtv_address.setVisibility(View.GONE);
                mHolder.mtv_nickname.setVisibility(View.GONE);
                mHolder.mtv_phone.setVisibility(View.GONE);
            }else {
                mHolder.mtv_add_address.setVisibility(View.GONE);
                mHolder.mtv_address.setVisibility(View.VISIBLE);
                mHolder.mtv_nickname.setVisibility(View.VISIBLE);
                mHolder.mtv_phone.setVisibility(View.VISIBLE);
                mHolder.mtv_nickname.setText(mAddress.realname);
                mHolder.mtv_phone.setText(mAddress.mobile);
                mHolder.mtv_address.setText(String.format(getString(R.string.address),mAddress.detail_address));
            }
        }
    }

    private void handlerInvalidGoods(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InvalidGoodsHolder){
            InvalidGoodsHolder mHolder = (InvalidGoodsHolder) holder;
            final int padding = TransformUtil.dip2px(context, 10);
            SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter<GoodsDeatilEntity.Goods>(context,
                    R.layout.item_invalid_goods,disabled) {

                @Override
                public void convert(SimpleViewHolder holder, GoodsDeatilEntity.Goods s, int position) {
                    if (position == 0){
                        holder.getView(R.id.mtv_inva).setVisibility(View.VISIBLE);
                        holder.getView(R.id.line).setVisibility(View.VISIBLE);
                    }else {
                        holder.getView(R.id.mtv_inva).setVisibility(View.GONE);
                        holder.getView(R.id.line).setVisibility(View.GONE);
                    }
                    MyRelativeLayout mrl_rootview = holder.getView(R.id.mrl_rootview);
                    mrl_rootview.setPadding(padding,padding,padding,0);

                    MyTextView mtv_title = holder.getView(R.id.mtv_title);
                    mtv_title.setText(s.title);
                    MyTextView mtv_price = holder.getView(R.id.mtv_price);
                    mtv_price.setText(Common.dotAfterSmall(getString(R.string.rmb)+s.price,11));
                    MyTextView mtv_attribute = holder.getView(R.id.mtv_attribute);
                    mtv_attribute.setText(s.sku);
                    MyTextView mtv_count = holder.getView(R.id.mtv_count);
                    mtv_count.setText(String.format(getString(R.string.x),s.qty));
                    MyImageView miv_goods = holder.getView(R.id.miv_goods);
                    GlideUtils.getInstance().loadImage(context,miv_goods,s.thumb);
                }
            };
            mHolder.recy_view.setAdapter(adapter);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View confirm_order = LayoutInflater.from(context)
                .inflate(R.layout.item_confirm_order, parent, false);
        return new BuyGoodsHolder(confirm_order);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BuyGoodsHolder){
            BuyGoodsHolder mHolder = (BuyGoodsHolder) holder;
            ConfirmOrderEntity.Enabled enabled = lists.get(position - 1);
            mHolder.mtv_store_name.setText(enabled.store_name);
            String shippingFee = enabled.shippingFee;
            if ("0".equals(shippingFee)){
                mHolder.mtv_shippingFree.setText(getString(R.string.free_shipping));
            }else {
                mHolder.mtv_shippingFree.setText(String.format(getString(R.string.express),shippingFee));
            }
            List<GoodsDeatilEntity.Goods> goods = enabled.goods;
            if (!isEmpty(goods)) {
                mHolder.recy_view.setVisibility(View.VISIBLE);
                mHolder.recy_view.setAdapter(new AppointGoodsAdapter(context,
                        false, goods));
            }else {
                mHolder.recy_view.setVisibility(View.GONE);
            }
            List<ConfirmOrderEntity.Voucher> voucher = enabled.voucher;
            if (!isEmpty(voucher)) {//有可用优惠券，默认使用第一张
                ConfirmOrderEntity.Voucher voucher1 = voucher.get(0);
                mHolder.mtv_discount.setText(voucher1.title);
                enabled.post_discount_price = Common.formatFloat(enabled.sub_total,voucher1.denomination);
                if (mListener != null){
                    mListener.onSelectVoucher(0);
                }
                String v = Common.formatFloat(enabled.sub_total, voucher1.denomination);
                mHolder.mtv_goods_price.setText(Common.dotAfterSmall(getString(R.string.rmb)+v,11));
                mHolder.mllayout_discount.setVisibility(View.VISIBLE);
            }else {
                enabled.selectVoucherId = -1;
                mHolder.mtv_goods_price.setText(Common.dotAfterSmall(getString(R.string.rmb)
                        .concat(Common.formatFloat(enabled.sub_total)),11));
                mHolder.mllayout_discount.setVisibility(View.GONE);
            }
            List<ConfirmOrderEntity.PromotionInfo> promotion_info = enabled.promotion_info;
            if (!isEmpty(promotion_info)){//促销让用户选择
                if (mIsOrderBuy){
                    String prom_lock = enabled.prom_lock;
                    if (!isEmpty(prom_lock) && "1".equals(prom_lock)){
                        ConfirmOrderEntity.PromotionInfo promotionInfo = promotion_info.get(0);
                        enabled.selectPromotionId = 0;
                        mHolder.mtv_promotion.setText(promotionInfo.prom_title);
                    }else {
                        mHolder.mtv_promotion.setText("");
                    }
                }else {
                    mHolder.mtv_promotion.setText(enabled.promotion_total_hint);
                }
                mHolder.mll_promotion.setVisibility(View.VISIBLE);
                mHolder.line_activity.setVisibility(View.VISIBLE);
            }else {
                mHolder.mll_promotion.setVisibility(View.GONE);
                mHolder.line_activity.setVisibility(View.GONE);
            }
            mHolder.mtv_goods_count.setText(String.format(getString(R.string.all_goods),enabled.sub_count));
        }
    }

    public class AddressHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_nickname)
        MyTextView mtv_nickname;

        @BindView(R.id.mtv_phone)
        MyTextView mtv_phone;

        @BindView(R.id.mtv_address)
        MyTextView mtv_address;

        @BindView(R.id.mtv_add_address)
        MyTextView mtv_add_address;
        public AddressHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (mAddress != null)
                AddressListActivity.startAct(context,mAddress.id);
            else
                AddressListActivity.startAct(context,null);
        }
    }


    public class BuyGoodsHolder extends BaseRecyclerViewHolder
            implements View.OnClickListener, TextWatcher {

        @BindView(R.id.recy_view)
        RecyclerView recy_view;

        @BindView(R.id.mllayout_discount)
        MyLinearLayout mllayout_discount;

        @BindView(R.id.met_leav_msg)
        MyEditText met_leav_msg;

        @BindView(R.id.mtv_store_name)
        MyTextView mtv_store_name;

        @BindView(R.id.mtv_shippingFree)
        MyTextView mtv_shippingFree;

        @BindView(R.id.mtv_discount)
        MyTextView mtv_discount;

        @BindView(R.id.mll_promotion)
        MyLinearLayout mll_promotion;

        @BindView(R.id.mtv_promotion)
        MyTextView mtv_promotion;

        @BindView(R.id.mtv_goods_count)
        MyTextView mtv_goods_count;

        @BindView(R.id.mtv_goods_price)
        MyTextView mtv_goods_price;

        @BindView(R.id.line_activity)
        View line_activity;


        public BuyGoodsHolder(View itemView) {
            super(itemView);
            recy_view.setNestedScrollingEnabled(false);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recy_view.setLayoutManager(manager);
            int space = TransformUtil.dip2px(context, 20);
            recy_view.addItemDecoration(new VerticalItemDecoration(space,
                    space / 2,0));
            recy_view.setFocusable(false);

            mllayout_discount.setOnClickListener(this);
            met_leav_msg.setOnClickListener(this);
            mll_promotion.setOnClickListener(this);
            met_leav_msg.addTextChangedListener(this);
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()){
                case R.id.mllayout_discount://选择优惠券
                    DiscountListDialog discountDialog = new DiscountListDialog(context);
                    discountDialog.setGoodsDiscount(lists.get(getAdapterPosition()-1));
                    discountDialog.setSelectListener(new DiscountListDialog.ISelectListener() {
                        @Override
                        public void onSelect(int position) {
                            if (position < 0){
                                return;
                            }
                            ConfirmOrderEntity.Enabled enabled = lists.get(getAdapterPosition() - 1);
                            ConfirmOrderEntity.Voucher voucher = enabled.voucher.get(position);
                            mtv_discount.setText(voucher.title);

                            String sub_total = enabled.sub_total;
                            enabled.selectVoucherId = position;

                            /**********计算折后小计***************/
                            if (enabled.selectPromotionId == -1){
                                enabled.post_discount_price = Common.formatFloat(sub_total,
                                        voucher.denomination);
                            }else {
                                if (!isEmpty(enabled.promotion_info)){
                                    String prom_reduce = enabled.promotion_info.
                                            get(enabled.selectPromotionId).prom_reduce;
                                    String s = Common.formatFloat(sub_total, voucher.denomination);
                                    enabled.post_discount_price = Common.formatFloat(s,
                                            isEmpty(prom_reduce) ? "0" : prom_reduce);
                                }
                            }
                            /************计算折后小计*************/
                            //显示店铺小计
                            mtv_goods_price.setText(getString(R.string.rmb).concat(enabled.post_discount_price));
                            if (mListener != null){
                                mListener.onSelectVoucher(position);
                            }
                        }
                    });
                    discountDialog.show();
                    break;
                case R.id.met_leav_msg:
                    met_leav_msg.requestFocus();
                    met_leav_msg.setFocusable(true);
                    met_leav_msg.setFocusableInTouchMode(true);
                    break;
                case R.id.mll_promotion://选择促销
                    if (mIsOrderBuy){
                        final DiscountListDialog promotionDialog = new DiscountListDialog(context);
                        promotionDialog.setPromotion(lists.get(getAdapterPosition() - 1));
                        promotionDialog.setSelectListener(new DiscountListDialog.ISelectListener() {
                            @Override
                            public void onSelect(int position) {
                                if (position < 0){
                                    return;
                                }
                                ConfirmOrderEntity.Enabled enabled = lists.
                                        get(getAdapterPosition() - 1);
                                ConfirmOrderEntity.PromotionInfo promotionInfo = enabled.
                                        promotion_info.get(position);
                                mtv_promotion.setText(promotionInfo.prom_title);
                                enabled.selectPromotionId = position;

                                /*********优惠券额度*************/
                                if (!isEmpty(enabled.voucher)){
                                    String denomination = enabled.voucher.get(enabled.
                                            selectVoucherId).denomination;
                                    String s1 = Common.formatFloat(enabled.sub_total, denomination);
                                    enabled.post_discount_price = Common.formatFloat(s1,
                                            isEmpty(promotionInfo.prom_reduce) ?
                                                    "0" : promotionInfo.prom_reduce);
                                }else {
                                    enabled.post_discount_price = Common.formatFloat(enabled.sub_total,
                                            isEmpty(promotionInfo.prom_reduce) ?
                                                    "0" : promotionInfo.prom_reduce);
                                }
                                /*********优惠券额度*************/

                                //显示店铺小计
                                mtv_goods_price.setText(getString(R.string.rmb)
                                        .concat(enabled.post_discount_price));
                                if (mListener != null){
                                    mListener.onSelectVoucher(position);
                                }
                            }
                        });
                        promotionDialog.show();
                    }else {
                        RecyclerDialog recyclerDialog = new RecyclerDialog(context);
                        recyclerDialog.setPromotionDetail(lists
                                .get(getAdapterPosition() - 1).promotion_info);
                        recyclerDialog.show();
                    }
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ConfirmOrderEntity.Enabled enabled = lists.get(getAdapterPosition() - 1);
            if (!isEmpty(s))
                enabled.remark = s.toString();
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }

    public class InvalidGoodsHolder extends BaseRecyclerViewHolder{

        protected final RecyclerView recy_view;

        public InvalidGoodsHolder(View itemView) {
            super(itemView);
            recy_view = (RecyclerView) itemView;
            recy_view.setFocusable(true);
            recy_view.setNestedScrollingEnabled(false);
            LinearLayoutManager manager  = new LinearLayoutManager(context);
            recy_view.setLayoutManager(manager);
            int space = TransformUtil.dip2px(context, 10);
            recy_view.addItemDecoration(new VerticalItemDecoration(space,0,0));
        }
    }

    public class StationHolder extends BaseRecyclerViewHolder{

        public StationHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = TransformUtil.dip2px(context,41);
            itemView.setLayoutParams(layoutParams);
        }
    }

    /**
     * 注册选择优惠券监听
     * @param listener
     */
    public void setSelectVoucherListener(ISelectVoucherListener listener){
        mListener = listener;
    }

    public interface ISelectVoucherListener{

        void onSelectVoucher(int position);
    }
}
