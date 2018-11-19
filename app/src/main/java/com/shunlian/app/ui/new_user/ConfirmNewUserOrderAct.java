package com.shunlian.app.ui.new_user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.ConfirmOrderAdapter;
import com.shunlian.app.bean.BuyGoodsParams;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.SubmitGoodsEntity;
import com.shunlian.app.presenter.ConfirmOrderPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.confirm_order.OrderDecoration;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IConfirmOrderView;
import com.shunlian.app.widget.DiscountListDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyNestedScrollView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pay.PayListActivity;

/**
 * Created by Administrator on 2017/11/25.
 * 确认订单
 */

public class ConfirmNewUserOrderAct extends BaseActivity implements IConfirmOrderView, View.OnClickListener {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_total_price)
    MyTextView mtv_total_price;

    @BindView(R.id.mtv_go_pay)
    MyTextView mtv_go_pay;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.nsv_view)
    MyNestedScrollView nsv_view;

    @BindView(R.id.view_line1)
    View view_line1;

    //平台优惠券
    @BindView(R.id.mllayout_discount)
    MyLinearLayout mllayout_discount;

    @BindView(R.id.mtv_discount)
    MyTextView mtv_discount;

    @BindView(R.id.rlayout_anonymous)
    RelativeLayout rlayout_anonymous;

    @BindView(R.id.miv_anonymous)
    MyImageView miv_anonymous;

    @BindView(R.id.mtv_station)
    MyTextView mtv_station;

    @BindView(R.id.rlayout_golden_eggs)
    RelativeLayout rlayout_golden_eggs;

    @BindView(R.id.mtv_golden_eggs)
    MyTextView mtv_golden_eggs;

    @BindView(R.id.miv_golden_eggs)
    MyImageView miv_golden_eggs;

    private String mTotalPrice;
    private boolean isOrderBuy = false;//是否直接购买
    private String detail_address;
    private String addressId;
    private String goods_id;
    private String qty;
    private String sku_id;
    private ConfirmOrderPresenter confirmOrderPresenter;
    public static final String TYPE_CART = "cart";//购物车
    public static final String TYPE_COMBO = "combo";//套餐
    private String type;
    private List<ConfirmOrderEntity.Enabled> enabled;
    private DiscountListDialog mStageVoucherDialog;
    private ConfirmOrderEntity.Enabled mStageVoucherEntity;
    private String mStageVoucherId="";//平台优化券id
    private boolean isAnonymous;//是否匿名
    private boolean isUserGoldenEggs;//是否使用金蛋
    private ObjectMapper mOM;
    private float mEggReduce;
    public static final String EGGS_TIP = "订单支付金额必须大于1元";//金蛋减免提示

    public static void startAct(Context context,String type){
        if (!Common.isAlreadyLogin()){
            Common.goGoGo(context,"login");
            return;
        }
        Intent intent = new Intent(context, ConfirmNewUserOrderAct.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    public static void startAct(Context context,String goods_id,String qty,String sku_id){
        if (!Common.isAlreadyLogin()){
            Common.goGoGo(context,"login");
            return;
        }
        Intent intent = new Intent(context, ConfirmNewUserOrderAct.class);
        intent.putExtra("goods_id",goods_id);
        intent.putExtra("qty",qty);
        intent.putExtra("sku_id",sku_id);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_confirm_order;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_go_pay.setOnClickListener(this);
        miv_close.setOnClickListener(this);
        mllayout_discount.setOnClickListener(this);
        rlayout_anonymous.setOnClickListener(this);
        rlayout_golden_eggs.setOnClickListener(this);

        nsv_view.setOnScrollChangeListener((MyNestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            //LogUtil.zhLogW("v="+v+String.format("scrollY=%d;oldScrollY=%d",scrollY,oldScrollY));
            //LogUtil.zhLogW("v="+v+String.format("scrollX=%d;oldScrollX=%d",scrollX,oldScrollX));
            if (scrollY >= 260) {
                visible(mtv_address,mtv_station);
            } else {
                gone(mtv_address,mtv_station);
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mOM = new ObjectMapper();
        Intent intent = getIntent();
        goods_id = intent.getStringExtra("goods_id");
        qty = intent.getStringExtra("qty");
        sku_id = intent.getStringExtra("sku_id");
        type = intent.getStringExtra("type");
        confirmOrderPresenter = new ConfirmOrderPresenter(this,this);
         confirmOrderPresenter.orderNewUserConfirm( null);
        recy_view.setLayoutManager(new LinearLayoutManager(this));
        recy_view.setNestedScrollingEnabled(false);
        int space = TransformUtil.dip2px(this, 10);
        recy_view.addItemDecoration(new OrderDecoration(space,getColorResouce(R.color.white_ash)));
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    /**
     * 订单页所有商品
     *
     * @param enabled
     * @param disabled
     */
    @Override
    public void confirmOrderAllGoods(final List<ConfirmOrderEntity.Enabled> enabled,
                                     List<GoodsDeatilEntity.Goods> disabled,
                                     ConfirmOrderEntity.Address address,
                                     List<ConfirmOrderEntity.NoDelivery> noDeliveryList) {
        String text = "";
        if (address != null){
            addressId = address.id;
            detail_address = address.detail_address;
            text = String.format(getStringResouce(R.string.send_to), detail_address);
        }else {
            text = getStringResouce(R.string.add_address);
        }
        mtv_address.setText(text);
        mtv_station.setText(text);

        //如果有不在发货区域商品就禁止下单
        if (!isEmpty(noDeliveryList)) {
            mtv_go_pay.setEnabled(false);
        }else {
            mtv_go_pay.setEnabled(true);
        }

        this.enabled = enabled;
        ConfirmOrderAdapter df = new ConfirmOrderAdapter(this,
                enabled, disabled,address,isOrderBuy,noDeliveryList);
        recy_view.setAdapter(df);

        df.setSelectVoucherListener(position ->calculateAmount(enabled));

    }

    /**
     * 计算总额
     * @param enabled
     */
    private float calculateAmount(List<ConfirmOrderEntity.Enabled> enabled) {
        if (enabled == null)enabled = new ArrayList<>();
        float currentPrice = 0;
        for (int i = 0; i < enabled.size(); i++) {//计算店铺小计
            String store_discount_price = enabled.get(i).store_discount_price;
            currentPrice += Float.parseFloat(isEmpty(store_discount_price)
                    ? enabled.get(i).sub_total : store_discount_price);
        }
        String totalPrice = null;
        if (mStageVoucherEntity != null){//使用平台优惠券
            ConfirmOrderEntity.Voucher voucher = mStageVoucherEntity
                    .voucher.get(mStageVoucherEntity.selectVoucherId);
            currentPrice -= Float.parseFloat(voucher.denomination);
        }

        if (isUserGoldenEggs){//减去金蛋抵扣的钱
            currentPrice -= mEggReduce;
        }

        if (isUserGoldenEggs && currentPrice < 1){
            Common.staticToast(EGGS_TIP);//提示用户至少支付的钱数
        }

        if (currentPrice <= 0){
            totalPrice = "0.00";
        }else {
            totalPrice = Common.formatFloat(currentPrice);
        }
        mtv_total_price.setText(Common.dotAfterSmall(getStringResouce(R.string.rmb)+totalPrice,11));
        return currentPrice;
    }

    /**
     * 商品总价和总数量
     *
     * @param count
     * @param price
     */
    @Override
    public void goodsTotalPrice(String count, String price) {
        mTotalPrice = price;
        String s = Common.formatFloat(mTotalPrice);
        if (Float.parseFloat(s) <= 0){
            mTotalPrice = "0.00";
        }
        mtv_total_price.setText(Common.dotAfterSmall(getStringResouce(R.string.rmb)+mTotalPrice,11));
    }

    /**
     * 平台优惠券
     *
     * @param user_stage_voucher 1 表示使用平台优惠券时最优
     * @param stage_voucher
     */
    @Override
    public void stageVoucher(String user_stage_voucher, List<ConfirmOrderEntity.Voucher> stage_voucher) {
        if (!isEmpty(stage_voucher)){
            visible(view_line1,mllayout_discount);
            mStageVoucherEntity = new ConfirmOrderEntity.Enabled();
            mStageVoucherEntity.voucher = stage_voucher;
            if (!"1".equals(user_stage_voucher)){
                mStageVoucherEntity.selectVoucherId = stage_voucher.size()-1;
            }else {
                mtv_discount.setText(stage_voucher.get(0).voucher_hint);
                mStageVoucherEntity.selectVoucherId = 0;
                mStageVoucherId = stage_voucher.get(0).voucher_id;
                calculateAmount(enabled);
            }
        }else {
            gone(view_line1,mllayout_discount);
        }
    }

    @Override
    public void goldenEggs(String golden_eggs_tip, String golden_eggs_count, String egg_reduce) {
        try {
            mEggReduce = isEmpty(egg_reduce)?0:Float.parseFloat(egg_reduce);
        }catch (Exception e){
            mEggReduce = 0;
        }
        if (!isEmpty(golden_eggs_tip)){
            visible(rlayout_golden_eggs);
            mtv_golden_eggs.setText(golden_eggs_tip);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && Activity.RESULT_OK == resultCode){
                 addressId = data.getStringExtra("addressId");
                isOrderBuy = false;
                if (TYPE_CART.equals(type)) {
                    confirmOrderPresenter.orderNewUserConfirm(addressId);
                }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mtv_go_pay:
                if (isEmpty(addressId)) {
                    Common.staticToast(getStringResouce(R.string.add_address));
                    recy_view.scrollToPosition(0);
                    return;
                }

                String price = mtv_total_price.getText().toString();
                String price_num = price.substring(1,price.length());
                if (isUserGoldenEggs && Float.parseFloat(price_num) < 1){
                    //提示用户至少支付的钱数
                    Common.staticToast(EGGS_TIP);
                    return;
                }

                String shop_goods = null;
                try {
                    shop_goods = mOM.writeValueAsString(mosaicParams());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                //LogUtil.zhLogW("go_pay=============="+shop_goods);

                BuyGoodsParams params = new BuyGoodsParams();
                params.addressId = addressId;
                params.shop_goods = shop_goods;
                params.price = price_num;
                params.stage_voucher_id = mStageVoucherId;
                params.anonymous = isAnonymous?"1":"0";//1匿名 0不匿名
                params.use_egg = isUserGoldenEggs?"1":"0";//是否使用金蛋 1是 0否

                String paramsStr = "";
                try {
                    paramsStr = mOM.writeValueAsString(params);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                PayListActivity.startAct(this,paramsStr);
                break;
            case R.id.miv_close:
                backSelect();
                break;
            case R.id.mllayout_discount://选择平台优惠券
                mStageVoucherDialog = new DiscountListDialog(this);
                mStageVoucherDialog.setGoodsDiscount(mStageVoucherEntity,"平台优惠券");
                mStageVoucherDialog.setSelectListener(position -> {
                    if (position < 0)return;
                    if (ConfirmOrderPresenter.isSelectStoreVoucher){
                        return;
                    }
                    if (position + 1== mStageVoucherEntity.voucher.size()){
                        ConfirmOrderPresenter.isSelectStageVoucher = false;
                    }else {
                        ConfirmOrderPresenter.isSelectStageVoucher = true;
                    }
                    ConfirmOrderEntity.Voucher voucher = mStageVoucherEntity.voucher.get(position);
                    mtv_discount.setText(voucher.voucher_hint);
                    mStageVoucherEntity.selectVoucherId = position;
                    mStageVoucherId = voucher.voucher_id;
                    calculateAmount(enabled);
                });
                mStageVoucherDialog.show();
                break;
            case R.id.rlayout_anonymous:
                if (!isAnonymous){
                    miv_anonymous.setImageResource(R.mipmap.img_xuanze_h);
                }else {
                    miv_anonymous.setImageResource(R.mipmap.img_xuanze_n);
                }
                isAnonymous = !isAnonymous;
                break;
            case R.id.rlayout_golden_eggs:
                isUserGoldenEggs = !isUserGoldenEggs;
                float p = calculateAmount(enabled);//每次改变按钮状态都要重新计算金额
                if (p < 1){
                    isUserGoldenEggs = false;
                    calculateAmount(enabled);
                    miv_golden_eggs.setImageResource(R.mipmap.img_xuanze_n);
                }else {
                    if (isUserGoldenEggs){
                        miv_golden_eggs.setImageResource(R.mipmap.img_xuanze_h);
                    }else {
                        miv_golden_eggs.setImageResource(R.mipmap.img_xuanze_n);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            backSelect();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backSelect() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setTvSureIsBold(false).setTvCancleIsBold(false)
                .setSureAndCancleListener(getStringResouce(R.string.leave_behind_goods),
                        getStringResouce(R.string.i_think), v -> promptDialog.dismiss(),
                        getStringResouce(R.string.to_resolve), v -> {
            promptDialog.dismiss();
            finish();
        }).show();
    }

    /**
     * 拼接参数
     */
    private List<SubmitGoodsEntity> mosaicParams() {
        List<SubmitGoodsEntity> goodsEntityList = new ArrayList<>();
        if (enabled != null){
            for (int i = 0; i < enabled.size(); i++) {
                ConfirmOrderEntity.Enabled enabled = this.enabled.get(i);
                SubmitGoodsEntity goodsEntity = new SubmitGoodsEntity();
                goodsEntity.store_id = enabled.store_id;
                goodsEntity.goods_items = new ArrayList<>();
                for (int j = 0; j < enabled.goods.size(); j++) {
                    GoodsDeatilEntity.Goods goods = enabled.goods.get(j);
                    SubmitGoodsEntity.GoodsItems goodsItems =new  SubmitGoodsEntity.GoodsItems();
                    goodsItems.goods_id = goods.goods_id;
                    goodsItems.qty = goods.qty;
                    goodsItems.sku_id = goods.sku_id;
                    goodsItems.prom_id = goods.prom_id;
                    goodsEntity.goods_items.add(goodsItems);
                }
                int selectVoucherId = enabled.selectVoucherId;
                List<ConfirmOrderEntity.Voucher> voucher = enabled.voucher;
                if (!isEmpty(voucher)){
                    goodsEntity.voucher_id = voucher.get(selectVoucherId).voucher_id;
                }

                if (isOrderBuy){
                    int selectPromotionId = enabled.selectPromotionId;
                    List<ConfirmOrderEntity.PromotionInfo> promotion_info = enabled.promotion_info;
                    if (!isEmpty(promotion_info) && selectPromotionId != -1){
                        //不选择促销会崩
                        goodsEntity.goods_items.get(0).prom_id = promotion_info.
                                get(selectPromotionId).prom_id;
                    }
                }
                goodsEntity.remark = enabled.remark;
                goodsEntityList.add(goodsEntity);
            }
        }
        return goodsEntityList;
    }

    public static void selectVoucherTip(boolean isStore){
        String tip = "";
        if (isStore){
            tip = "平台优惠券和店铺优惠券\n不能叠加使用，请先取消\n平台优惠券再进行选择";
        }else {
            tip = "平台优惠券和店铺优惠券\n不能叠加使用，请先取消\n店铺优惠券再进行选择";
        }
        Common.staticToasts(Common.getApplicationContext(), tip,R.mipmap.icon_common_tanhao);
    }
}
