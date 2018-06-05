package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

/**
 * Created by Administrator on 2018/6/4.
 */

public class CouponMsgAct extends BaseActivity {

    public static void startAct(Context context, String couponId) {
        Intent intent = new Intent(context, CouponMsgAct.class);
        intent.putExtra("coupon_id", couponId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_coupon_msg;
    }

    @Override
    protected void initData() {

    }
}
