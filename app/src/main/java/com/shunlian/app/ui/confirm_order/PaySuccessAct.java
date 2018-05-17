package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.PaySuccessPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPaySuccessView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySuccessAct extends BaseActivity implements View.OnClickListener ,IPaySuccessView{
    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.mtv_price)
    MyTextView mtv_price;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private String orderId;
    private String pay_sn;

    public static void startAct(Context context, String orderId, String price, String pay_sn) {
        Intent intent = new Intent(context, PaySuccessAct.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("price", price);
        intent.putExtra("pay_sn", pay_sn);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_pay_success;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        pay_sn = intent.getStringExtra("pay_sn");
        String price = intent.getStringExtra("price");
        mtv_price.setText(price);

        GridLayoutManager manager = new GridLayoutManager(this,2);
        rv_goods.setLayoutManager(manager);
        rv_goods.addItemDecoration(new GridSpacingItemDecoration
                (TransformUtil.dip2px(this, 5), false));
        PaySuccessPresenter presenter = new PaySuccessPresenter(this,this,pay_sn);
    }

    @OnClick(R.id.mtv_order)
    public void seeOrderDetail(){
        if (TextUtils.isEmpty(orderId)){
            MyOrderAct.startAct(this,0);
        }else {
            OrderDetailAct.startAct(this,orderId);
        }
    }

    @OnClick(R.id.mtv_firstPage)
    public void backFirstPage(){
        Common.goGoGo(this,"");
    }

    @OnClick(R.id.rl_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.paySuccess();
    }

    @Override
    public void onClick(View view) {
        if (MyOnClickListener.isFastClick()) {
            return;
        }
        switch (view.getId()) {

        }
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        rv_goods.setAdapter(adapter);
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }
}
