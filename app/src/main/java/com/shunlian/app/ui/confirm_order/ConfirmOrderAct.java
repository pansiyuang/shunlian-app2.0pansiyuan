package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ConfirmOrderAdapter;
import com.shunlian.app.presenter.ConfirmOrderPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IConfirmOrderView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/25.
 * 确认订单
 */

public class ConfirmOrderAct extends BaseActivity implements IConfirmOrderView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;
    private LinearLayoutManager manager;

    public static void startAct(Context context,String cart_ids){
        Intent intent = new Intent(context, ConfirmOrderAct.class);
        intent.putExtra("cart_ids",cart_ids);
        context.startActivity(intent);
    }

    public static void startAct(Context context,String goods_id,String qty,String sku_id){
        Intent intent = new Intent(context, ConfirmOrderAct.class);
        intent.putExtra("goods_id",goods_id);
        intent.putExtra("qty",qty);
        intent.putExtra("sku_id",sku_id);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_confirm_order;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int firstPosition = manager.findFirstVisibleItemPosition();
                    if (firstPosition == 0){
                        mtv_address.setVisibility(View.GONE);
                    }else {
                        mtv_address.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        Intent intent = getIntent();
        String cart_ids = intent.getStringExtra("cart_ids");
        String goods_id = intent.getStringExtra("goods_id");
        String qty = intent.getStringExtra("qty");
        String sku_id = intent.getStringExtra("sku_id");
        ConfirmOrderPresenter confirmOrderPresenter = new ConfirmOrderPresenter(this,this);
        if (!TextUtils.isEmpty(cart_ids)){

        }else {
            confirmOrderPresenter.orderBuy(goods_id,qty,sku_id);
        }



        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(this, 10);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getResources().getColor(R.color.white_ash)));
        recy_view.setAdapter(new ConfirmOrderAdapter(this,false, DataUtil.getListString(3,"df")));
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }
}
