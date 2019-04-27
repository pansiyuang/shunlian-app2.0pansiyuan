package com.shunlian.app.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;


import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

/**
 * Created by Administrator on 2019/3/30.
 */

public class DemoGoodsDetailAct extends BaseActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toolbar toolbat= findViewById(R.id.my_toolbar);
//        toolbat.setNavigationIcon(R.drawable.ic_check);
//        toolbat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        toolbat.setTitle("11111");
//        setSupportActionBar(toolbat);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, DemoGoodsDetailAct.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_ad_new;
    }

    @Override
    protected void initData() {

    }
}
