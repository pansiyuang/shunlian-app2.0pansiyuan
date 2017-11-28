package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ConfirmOrderAdapter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/25.
 * 确认订单
 */

public class ConfirmOrderAct extends BaseActivity {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    public static void startAct(Context context){
        context.startActivity(new Intent(context,ConfirmOrderAct.class));
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_confirm_order;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(this, 10);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getResources().getColor(R.color.white_ash)));
        recy_view.setAdapter(new ConfirmOrderAdapter(this,false, DataUtil.getListString(10,"df")));
    }
}
