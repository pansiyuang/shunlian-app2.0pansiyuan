package com.shunlian.app.ui.goods_detail;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDetailAct extends BaseActivity {

    public static void startAct(Context context){
        Intent intent = new Intent(context,GoodsDetailAct.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_goods_detail;
    }

    @Override
    protected void initData() {

    }
}
