package com.shunlian.app.ui.coupon;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

/**
 * Created by zhanghe on 2018/7/24.
 */

public class CouponGoodsAct extends BaseActivity {


    public static void startAct(Context context,String id){
        Intent intent = new Intent(context,CouponGoodsAct.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_coupon_goods;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
    }
}
