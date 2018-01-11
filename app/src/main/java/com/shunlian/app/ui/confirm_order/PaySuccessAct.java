package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySuccessAct extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.mtv_price)
    MyTextView mtv_price;

    private String orderId;

    public static void startAct(Context context, String orderId,String price) {
        Intent intent = new Intent(context, PaySuccessAct.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("price", price);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_pay_success;
    }

    @Override
    protected void initData() {
//        if (!TextUtils.isEmpty(getIntent().getStringExtra("orderId"))) {
//            orderId = getIntent().getStringExtra("orderId");
//        }
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        String price = intent.getStringExtra("price");

        mtv_price.setText(price);
    }

    @OnClick(R.id.mtv_order)
    public void seeOrderDetail(){
        OrderDetailAct.startAct(this,orderId);
    }

    @OnClick(R.id.mtv_firstPage)
    public void backFirstPage(){
        // TODO: 2018/1/10 返回首页
    }

    @Override
    public void onClick(View view) {
        if (FastClickListener.isFastClick()) {
            return;
        }
        switch (view.getId()) {
//            case R.id.mtv_copy:
//
//                break;
        }
    }
}
