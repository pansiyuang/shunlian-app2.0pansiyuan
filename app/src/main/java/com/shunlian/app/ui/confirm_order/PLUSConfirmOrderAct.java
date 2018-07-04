package com.shunlian.app.ui.confirm_order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.PLUSConfirmOrderPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IPLUSConfirmView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import pay.PayListActivity;

/**
 * Created by Administrator on 2017/11/25.
 * 确认订单
 */

public class PLUSConfirmOrderAct extends BaseActivity implements IPLUSConfirmView, View.OnClickListener {

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
    private String addressId;
    private String product_id;
    private String sku_id;
    private PLUSConfirmOrderPresenter mPLUSPresenter;


    public static void startAct(Context context,String product_id,String sku_id){
        if (!Common.isAlreadyLogin()){
            Common.staticToast(context.getResources().getString(R.string.plase_login));
            return;
        }
        Intent intent = new Intent(context, PLUSConfirmOrderAct.class);
        intent.putExtra("product_id",product_id);
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
        product_id = intent.getStringExtra("product_id");
        sku_id = intent.getStringExtra("sku_id");
        mPLUSPresenter = new PLUSConfirmOrderPresenter(this,this);
        mPLUSPresenter.orderBuy(product_id,sku_id,null);
        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(this, 10);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0, 0, getResources().getColor(R.color.white_ash)));
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    /**
     * 商品总价和总数量
     */
    @Override
    public void goodsTotalPrice(String price,String addressId) {
        mTotalPrice = Common.formatFloat(price);
        this.addressId = addressId;
        mtv_total_price.setText(Common.dotAfterSmall(
                getStringResouce(R.string.rmb)+(mTotalPrice),11));
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && Activity.RESULT_OK == resultCode){
            addressId = data.getStringExtra("addressId");
            mPLUSPresenter.orderBuy(product_id,sku_id,addressId);
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

                PayListActivity.startAct(this,product_id,sku_id,addressId,mTotalPrice,"");
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
                .setSureAndCancleListener(getStringResouce(R.string.leave_behind_goods),
                        getStringResouce(R.string.i_think), v -> promptDialog.dismiss(),
                        getStringResouce(R.string.to_resolve), v -> {
            promptDialog.dismiss();
            finish();
        }).show();
    }
}
