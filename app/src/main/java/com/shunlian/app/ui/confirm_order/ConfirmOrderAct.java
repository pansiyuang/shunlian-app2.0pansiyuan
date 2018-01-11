package com.shunlian.app.ui.confirm_order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.ConfirmOrderAdapter;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.SubmitGoodsEntity;
import com.shunlian.app.presenter.ConfirmOrderPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IConfirmOrderView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pay.PayListActivity;

/**
 * Created by Administrator on 2017/11/25.
 * 确认订单
 */

public class ConfirmOrderAct extends BaseActivity implements IConfirmOrderView, View.OnClickListener {

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


    private LinearLayoutManager manager;
    private String mTotalPrice;
    private boolean isOrderBuy = false;//是否直接购买
    private String detail_address;
    private String addressId;
    private String cart_ids;
    private String goods_id;
    private String qty;
    private String sku_id;
    private ConfirmOrderPresenter confirmOrderPresenter;
    public static final String TYPE_CART = "cart";//购物车
    public static final String TYPE_COMBO = "combo";//套餐
    private String type;
    private List<ConfirmOrderEntity.Enabled> enabled;

    public static void startAct(Context context,String cart_ids,String type){
        if (!Common.isAlreadyLogin()){
            Common.staticToast(context.getResources().getString(R.string.plase_login));
            return;
        }
        Intent intent = new Intent(context, ConfirmOrderAct.class);
        intent.putExtra("cart_ids",cart_ids);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    public static void startAct(Context context,String goods_id,String qty,String sku_id){
        if (!Common.isAlreadyLogin()){
            Common.staticToast(context.getResources().getString(R.string.plase_login));
            return;
        }
        Intent intent = new Intent(context, ConfirmOrderAct.class);
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
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int firstPosition = manager.findFirstVisibleItemPosition();
                    if (firstPosition == 0){
                        mtv_address.setVisibility(View.GONE);
                    }else {
                        mtv_address.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        Intent intent = getIntent();
        cart_ids = intent.getStringExtra("cart_ids");
        goods_id = intent.getStringExtra("goods_id");
        qty = intent.getStringExtra("qty");
        sku_id = intent.getStringExtra("sku_id");
        type = intent.getStringExtra("type");
        confirmOrderPresenter = new ConfirmOrderPresenter(this,this);
        if (!TextUtils.isEmpty(cart_ids)){
            isOrderBuy = false;
            if (TYPE_CART.equals(type)) {
                confirmOrderPresenter.orderConfirm(cart_ids, null);
            }else {
                confirmOrderPresenter.buyCombo(cart_ids,null);
            }
        }else {
            isOrderBuy = true;
            confirmOrderPresenter.orderBuy(goods_id, qty, sku_id,null);
        }
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
    public void confirmOrderAllGoods(final List<ConfirmOrderEntity.Enabled> enabled, List<GoodsDeatilEntity.Goods> disabled,ConfirmOrderEntity.Address address) {
        if (address != null){
            addressId = address.id;
            detail_address = address.detail_address;
            mtv_address.setText(String.format(getResources().getString(R.string.send_to),detail_address));
        }else {
            mtv_address.setText(getResources().getString(R.string.add_address));
        }
        if (enabled != null && enabled.size() > 0) {
            this.enabled = enabled;
            manager = new LinearLayoutManager(this);
            recy_view.setLayoutManager(manager);
            int space = TransformUtil.dip2px(this, 10);
            recy_view.addItemDecoration(new VerticalItemDecoration(space,
                    0, 0, getResources().getColor(R.color.white_ash)));
            ConfirmOrderAdapter df = new ConfirmOrderAdapter(this, false, enabled, disabled,address,isOrderBuy);
            recy_view.setAdapter(df);

            df.setSelectVoucherListener(new ConfirmOrderAdapter.ISelectVoucherListener() {
                @Override
                public void onSelectVoucher(int position) {
                    float currentPrice = Float.parseFloat(mTotalPrice);
                    for (int i = 0; i < enabled.size(); i++) {
                        ConfirmOrderEntity.Enabled enabled1 = enabled.get(i);
                        int selectVoucherId = enabled1.selectVoucherId;
                        if (isOrderBuy) {
                            int selectPromotionId = enabled1.selectPromotionId;
                            if (selectPromotionId >= 0 && enabled1.promotion_info != null
                                    && enabled1.promotion_info.size() > 0){
                                ConfirmOrderEntity.PromotionInfo promotionInfo = enabled1.
                                        promotion_info.get(selectPromotionId);
                                String prom_reduce = promotionInfo.prom_reduce;
                                float v = Float.parseFloat(prom_reduce);
                                currentPrice -= v;
                            }
                        }
                        if (selectVoucherId >= 0) {
                            ConfirmOrderEntity.Voucher voucher1 = enabled1.voucher.get(selectVoucherId);
                            float discount = Float.parseFloat(voucher1.denomination);
                            currentPrice -= discount;
                        } else {
                            continue;
                        }
                    }
                    mtv_total_price.setText(Common.dotAfterSmall(getResources()
                            .getString(R.string.rmb) + Common.formatFloat(currentPrice),11));
                }
            });
        }
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
        mtv_total_price.setText(Common.dotAfterSmall(getResources()
                .getString(R.string.rmb).concat(price),11));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && Activity.RESULT_OK == resultCode){
            addressId = data.getStringExtra("addressId");
            if (!TextUtils.isEmpty(cart_ids)){
                isOrderBuy = false;
                if (TYPE_CART.equals(type)) {
                    confirmOrderPresenter.orderConfirm(cart_ids,addressId);
                }else {
                    confirmOrderPresenter.buyCombo(cart_ids,addressId);
                }
            }else {
                isOrderBuy = true;
                confirmOrderPresenter.orderBuy(goods_id, qty, sku_id,addressId);
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
                String shop_goods = null;
                try {
                    shop_goods = new ObjectMapper().writeValueAsString(mosaicParams());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                LogUtil.zhLogW("go_pay=============="+shop_goods);
                String price = mtv_total_price.getText().toString();
                PayListActivity.startAct(this,shop_goods,addressId,null,price.substring(1,price.length()));
                break;
            case R.id.miv_close:
                backSelect();
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
                .setSureAndCancleListener("忍心丢下心仪商品？", "我再想想", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.dismiss();
            }
        }, "去意已决", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
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
                if (!isEmpty(voucher) && selectVoucherId != 0){
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
}
