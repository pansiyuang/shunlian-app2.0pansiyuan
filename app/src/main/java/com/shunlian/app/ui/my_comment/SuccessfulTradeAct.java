package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

/**
 * Created by Administrator on 2017/12/20.
 */

public class SuccessfulTradeAct extends BaseActivity {


    public static void startAct(Context context){
        context.startActivity(new Intent(context,SuccessfulTradeAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_successful_trade;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.pink_color);

    }
}
