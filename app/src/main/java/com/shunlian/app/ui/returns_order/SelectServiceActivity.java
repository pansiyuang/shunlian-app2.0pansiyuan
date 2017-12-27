package com.shunlian.app.ui.returns_order;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.ui.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/26.
 */

public class SelectServiceActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    @BindView(R.id.tv_store_name)
    TextView tv_store_name;

    @BindView(R.id.tv_goods_name)
    TextView tv_goods_name;

    @BindView(R.id.tv_goods_price)
    TextView tv_goods_price;

    @BindView(R.id.tv_goods_param)
    TextView tv_goods_param;

    @BindView(R.id.tv_goods_count)
    TextView tv_goods_count;

    @BindView(R.id.rl_money_only)
    RelativeLayout rl_money_only;

    @BindView(R.id.rl_money_goods)
    RelativeLayout rl_money_goods;

    @BindView(R.id.rl_change_goods)
    RelativeLayout rl_change_goods;


    public static void startAct(Context context, OrderdetailEntity.Good goods) {
        Intent intent = new Intent(context, SelectServiceActivity.class);
        intent.putExtra("goods", goods);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_service;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.select_service_type));
        rl_title_more.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initListener() {
        rl_money_only.setOnClickListener(this);
        rl_money_goods.setOnClickListener(this);
        rl_change_goods.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_money_only:
                break;
            case R.id.rl_money_goods:
                break;
            case R.id.rl_change_goods:
                break;
        }
    }
}
