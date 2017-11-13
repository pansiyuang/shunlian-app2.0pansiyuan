package com.shunlian.app.ui.store;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;

/**
 * Created by Administrator on 2017/11/7.
 */

public class StoreAct extends BaseActivity {

    public static void startAct(Context context){
        Intent intent = new Intent(context,StoreAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_store;
    }

    @Override
    protected void initData() {

    }
}
