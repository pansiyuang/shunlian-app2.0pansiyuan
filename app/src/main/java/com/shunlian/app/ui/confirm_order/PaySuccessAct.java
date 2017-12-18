package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class PaySuccessAct extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;


    public static void startAct(Context context, String orderId) {
        Intent intent = new Intent(context, PaySuccessAct.class);
        intent.putExtra("orderId", orderId);
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
