package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ConfirmOrderAdapter;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.ConfirmOrderPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IConfirmOrderView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/25.
 * 确认订单
 */

public class ConfirmOrderAct extends BaseActivity implements IConfirmOrderView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_total_price)
    MyTextView mtv_total_price;
    private LinearLayoutManager manager;
    private String mTotalPrice;
    private boolean isOrderBuy = false;//是否直接购买
    private String detail_address;

    public static void startAct(Context context,String cart_ids){
        Intent intent = new Intent(context, ConfirmOrderAct.class);
        intent.putExtra("cart_ids",cart_ids);
        context.startActivity(intent);
    }

    public static void startAct(Context context,String goods_id,String qty,String sku_id){
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
        String cart_ids = intent.getStringExtra("cart_ids");
        String goods_id = intent.getStringExtra("goods_id");
        String qty = intent.getStringExtra("qty");
        String sku_id = intent.getStringExtra("sku_id");
        ConfirmOrderPresenter confirmOrderPresenter = new ConfirmOrderPresenter(this,this);
        if (!TextUtils.isEmpty(cart_ids)){
            isOrderBuy = false;
            confirmOrderPresenter.orderConfirm(cart_ids,"110105");
        }else {
            isOrderBuy = true;
            confirmOrderPresenter.orderBuy(goods_id,qty,sku_id);
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
            detail_address = address.detail_address;
            mtv_address.setText("送至："+detail_address);
        }else {
            mtv_address.setText("请添加您的收货地址");
        }
        if (enabled != null && enabled.size() > 0) {
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
                                float v = Common.formatFloat(prom_reduce);
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
                .getString(R.string.rmb)+price,11));
    }
}
