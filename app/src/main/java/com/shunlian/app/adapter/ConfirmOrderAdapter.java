package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.ConfirmOrderPresenter;
import com.shunlian.app.ui.receive_adress.AddressListActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.PromptDialog;
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
    public static final int ITEM_AREA_DELIVERY = 4;//局部地区不发货
    private List<GoodsDeatilEntity.Goods> disabled;
    private ConfirmOrderEntity.Address mAddress;
    private boolean mIsOrderBuy;
    private List<ConfirmOrderEntity.NoDelivery> mNoDeliveryList;
    private ISelectVoucherListener mListener;
    private PromptDialog promptDialog;
    private String mFrom;//上一个页面来源

    public ConfirmOrderAdapter(Context context,
                               List<ConfirmOrderEntity.Enabled> lists,
                               List<GoodsDeatilEntity.Goods> disabled,
                               ConfirmOrderEntity.Address address, boolean isOrderBuy,
                               List<ConfirmOrderEntity.NoDelivery> noDeliveryList,String from) {
        super(context, false, lists);
        this.disabled = disabled;
        mAddress = address;
        mIsOrderBuy = isOrderBuy;
        mNoDeliveryList = noDeliveryList;
        mFrom = from;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0){//收货地址
            return ITEM_ADDRESS;
        }else if (isHasInvalid() && position + 1 == getItemCount()){//显示商品失效条目
            return ITEM_INVALID;
        }else if (isHasDelivery() && position >= 1 + lists.size()){//显示不发获取区域条目
            return ITEM_AREA_DELIVERY;
        }else {//正常商品显示
            return super.getItemViewType(position);
        }
    }

    /**
     * 是否有失效商品
     * @return
     */
    private boolean isHasInvalid(){
        return !isEmpty(disabled);
    }

    /**
     * 是否有不发货区域商品
     * @return
     */
    private boolean isHasDelivery(){return !isEmpty(mNoDeliveryList);}

    /**
     * 不发货区域商品数量
     * @return
     */
    private int noDeliveryGoodsCount(){
        return isEmpty(mNoDeliveryList)?0:mNoDeliveryList.size();
    }

    @Override
    public int getItemCount() {
        int count = 1;
        if (isHasDelivery()){
            count += mNoDeliveryList.size();
        }
        if (isHasInvalid()){
            count += 1;
        }
        count += super.getItemCount();
        return count;
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
            case ITEM_AREA_DELIVERY:
                View station_layout = LayoutInflater.from(context)
                        .inflate(R.layout.item_area_delivery, parent, false);
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
            case ITEM_AREA_DELIVERY:
                handleNoDelivery(holder,position);
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
                visible(mHolder.mtv_add_address);
                gone(mHolder.mtv_address,mHolder.mtv_nickname,mHolder.mtv_phone);
            }else {
                gone(mHolder.mtv_add_address);
                visible(mHolder.mtv_address,mHolder.mtv_nickname,mHolder.mtv_phone);
                mHolder.mtv_nickname.setText(mAddress.realname);
                mHolder.mtv_phone.setText(mAddress.mobile);
                mHolder.mtv_address.setText(String.format(getString(R.string.address),mAddress.detail_address));
            }
        }
    }

    private void handleNoDelivery(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  StationHolder) {
            StationHolder mHolder = (StationHolder) holder;
            int i = position - 1 - lists.size();
            if (i >= mNoDeliveryList.size() || i < 0) return;
            ConfirmOrderEntity.NoDelivery goods = mNoDeliveryList.get(i);

            GlideUtils.getInstance().loadImage(context, mHolder.miv_goods, goods.thumb);
            mHolder.mtv_count.setText("x" + goods.qty);
            mHolder.mtv_price.setText(Common.dotAfterSmall(getString(R.string.rmb) + goods.price, 11));
            mHolder.mtv_attribute.setText(goods.sku);

            setLabel(mHolder.mtv_label,mHolder.mtv_title,goods.title,goods.big_label);
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
                        visible(holder.getView(R.id.mtv_inva),holder.getView(R.id.line));
                    }else {
                        gone(holder.getView(R.id.mtv_inva),holder.getView(R.id.line));
                    }
                    MyRelativeLayout mrl_rootview = holder.getView(R.id.mrl_rootview);
                    mrl_rootview.setPadding(padding,padding,padding,0);

                    MyTextView mtv_title = holder.getView(R.id.mtv_title);

                    MyTextView mtv_price = holder.getView(R.id.mtv_price);
                    mtv_price.setText(Common.dotAfterSmall(getString(R.string.rmb) + s.price, 11));
                    MyTextView mtv_attribute = holder.getView(R.id.mtv_attribute);
                    mtv_attribute.setText(s.sku);
                    MyTextView mtv_count = holder.getView(R.id.mtv_count);
                    mtv_count.setText(String.format(getString(R.string.x),s.qty));
                    MyImageView miv_goods = holder.getView(R.id.miv_goods);
                    GlideUtils.getInstance().loadImage(context,miv_goods,s.thumb);

                    MyTextView mtv_label = holder.getView(R.id.mtv_label);


                    setLabel(mtv_label,mtv_title,s.title,s.big_label);
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
                visible(mHolder.recy_view);
                mHolder.recy_view.setAdapter(new AppointGoodsAdapter(context,
                        false, goods,mFrom));
            }else {
                gone(mHolder.recy_view);
            }

            List<ConfirmOrderEntity.Voucher> voucher = enabled.voucher;
            if (!isEmpty(voucher)) {//有可用优惠券，默认使用第一张
                ConfirmOrderEntity.Voucher voucher1 = voucher.get(enabled.selectVoucherId);
                if (enabled.selectVoucherId == voucher.size()-1){
                    mHolder.mtv_discount.setText("请选择");
                }else {
                    mHolder.mtv_discount.setText(voucher1.voucher_hint);
                }
                String temp = Common.formatFloat(enabled.sub_total, voucher1.denomination);
                if (Float.parseFloat(temp) <= 0) temp = "0.00";
                enabled.store_discount_price = temp;
                mHolder.mtv_goods_price.setText(Common.dotAfterSmall(getString(R.string.rmb)+temp,11));
                visible(mHolder.mllayout_discount);
                if (mListener != null){
                    mListener.onSelectVoucher(0);
                }

            }else {
                enabled.selectVoucherId = -1;
                enabled.store_discount_price = enabled.sub_total;
                mHolder.mtv_goods_price.setText(Common.dotAfterSmall(getString(R.string.rmb)
                        +Common.formatFloat(enabled.sub_total),11));
                gone(mHolder.mllayout_discount);
            }


            List<ConfirmOrderEntity.PromotionInfo> promotion_info = enabled.promotion_info;
            if (!isEmpty(promotion_info)){//促销让用户选择
                if (mIsOrderBuy){
                    String prom_lock = enabled.prom_lock;
                    if (!isEmpty(prom_lock) && "1".equals(prom_lock)){//prom_lock 1是默认勾选促销产品，否则默认不选
                        ConfirmOrderEntity.PromotionInfo promotionInfo = promotion_info.get(0);
                        enabled.selectPromotionId = 0;
                        mHolder.mtv_promotion.setText(promotionInfo.prom_title);
                        mHolder.calculatePromotion(0);
                    }else {
                        mHolder.mtv_promotion.setText("");
                    }
                }else {
                    mHolder.mtv_promotion.setText(enabled.promotion_total_hint);
                }
                visible(mHolder.mll_promotion,mHolder.line_activity);
            }else {
                gone(mHolder.mll_promotion,mHolder.line_activity);
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
                    discountDialog.setGoodsDiscount(lists.get(getAdapterPosition()-1),getString(R.string.goods_voucher));
                    discountDialog.setSelectListener(position -> {
                        if (position < 0){
                            return;
                        }
                        if (ConfirmOrderPresenter.isSelectStageVoucher){
                            return;
                        }

                        ConfirmOrderEntity.Enabled enabled = lists.get(getAdapterPosition() - 1);
                        ConfirmOrderEntity.Voucher voucher = enabled.voucher.get(position);
                        if (position + 1== enabled.voucher.size()){
                            ConfirmOrderPresenter.isSelectStoreVoucher = false;
                        }else {
                            ConfirmOrderPresenter.isSelectStoreVoucher = true;
                        }
                        mtv_discount.setText(voucher.voucher_hint);

                        String sub_total = enabled.sub_total;
                        enabled.selectVoucherId = position;

                        /**********计算折后小计***************/
                        if (enabled.selectPromotionId == -1){
                            enabled.store_discount_price = Common.formatFloat(sub_total,
                                    voucher.denomination);
                        }else {
                            if (!isEmpty(enabled.promotion_info)){
                                String prom_reduce = enabled.promotion_info.
                                        get(enabled.selectPromotionId).prom_reduce;
                                String s = Common.formatFloat(sub_total, voucher.denomination);
                                enabled.store_discount_price = Common.formatFloat(s,
                                        isEmpty(prom_reduce) ? "0" : prom_reduce);
                            }
                        }
                        /************计算折后小计*************/
                        //显示店铺小计
                        if (Float.parseFloat(enabled.store_discount_price) <= 0){
                            enabled.store_discount_price = "0.00";
                        }
                        mtv_goods_price.setText(Common.dotAfterSmall(getString(R.string.rmb)
                                .concat(enabled.store_discount_price),11));
                        if (mListener != null){
                            mListener.onSelectVoucher(position);
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
                        promotionDialog.setSelectListener(position -> {
                            if (position < 0){
                                return;
                            }
                            calculatePromotion(position);
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

        private void calculatePromotion(int position) {
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
                enabled.store_discount_price = Common.formatFloat(s1,
                        isEmpty(promotionInfo.prom_reduce) ?
                                "0" : promotionInfo.prom_reduce);
            }else {
                enabled.store_discount_price = Common.formatFloat(enabled.sub_total,
                        isEmpty(promotionInfo.prom_reduce) ?
                                "0" : promotionInfo.prom_reduce);
            }
            /*********优惠券额度*************/

            //显示店铺小计
            if (Float.parseFloat(enabled.store_discount_price) <= 0){
                enabled.store_discount_price = "0.00";
            }
            mtv_goods_price.setText(Common.dotAfterSmall(getString(R.string.rmb)
                    .concat(enabled.store_discount_price),11));
            if (mListener != null){
                mListener.onSelectVoucher(position);
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

        @BindView(R.id.mtv_look_tip)
        MyTextView mtv_look_tip;

        @BindView(R.id.miv_goods)
        MyImageView miv_goods;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_attribute)
        MyTextView mtv_attribute;

        @BindView(R.id.mtv_count)
        MyTextView mtv_count;

        @BindView(R.id.mrl_rootview)
        MyRelativeLayout mrl_rootview;

        @BindView(R.id.mtv_label)
        MyTextView mtv_label;

        public StationHolder(View itemView) {
            super(itemView);
            int px = TransformUtil.dip2px(context, 10);
            LinearLayout.LayoutParams
                    lp = (LinearLayout.LayoutParams) mrl_rootview.getLayoutParams();
            lp.leftMargin = px;
            lp.rightMargin = px;
            lp.bottomMargin = px;
            lp.topMargin = px;
            mrl_rootview.setLayoutParams(lp);

            mtv_look_tip.setOnClickListener(view -> {
                int i = getAdapterPosition() - 1 - lists.size();
                ConfirmOrderEntity.NoDelivery noDelivery = mNoDeliveryList.get(i);
                if (promptDialog == null) {
                    promptDialog = new PromptDialog((Activity) context);
                }
                promptDialog.tvCancleVisibility(true);
                promptDialog.setTvSureText("取消");
                SpannableStringBuilder ssb = Common.changeColor(noDelivery.areas
                        + noDelivery.hint, noDelivery.hint, getColor(R.color.pink_color));
                promptDialog.setTvSureIsBold(false).setTvCancleIsBold(false)
                        .setSureAndCancleListener(ssb, "取消",
                                view1 -> {
                            if (promptDialog != null)
                                promptDialog.dismiss();
                        },"",null).show();
            });
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


    private void setLabel(TextView tv_label,TextView tv_title,String str_title,String str_label){
        int pref_length = 0;
        if (!isEmpty(str_label)){
            visible(tv_label);
            tv_label.setText(str_label);
            pref_length = str_label.length();
        }else {
            gone(tv_label);
            pref_length = 0;
        }
        tv_title.setText(Common.getPlaceholder(pref_length) + str_title);
    }
}
