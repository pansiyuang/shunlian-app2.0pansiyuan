package com.shunlian.app.ui.order_address;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AddressManageAdapter;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.presenter.OrderAddressPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IOrderAddressView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/6.
 */

public class AddressManageActivity extends BaseActivity implements View.OnClickListener, IOrderAddressView {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recycler_address)
    RecyclerView recycler_address;

    @BindView(R.id.btn_select_address)
    Button btn_select_address;

    private OrderAddressPresenter orderAddressPresenter;
    private AddressManageAdapter manageAdapter;


    public static void startAct(Context context) {
        Intent intent = new Intent(context, AddressManageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_order_address;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        tv_title.setText(getString(R.string.select_order_address));
        orderAddressPresenter = new OrderAddressPresenter(this, this);
        orderAddressPresenter.getAddressList();
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_select_address.setOnClickListener(this);
    }

    @Override
    public void orderList(List<ConfirmOrderEntity.Address> addressList) {
        manageAdapter = new AddressManageAdapter(this, false, addressList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_address.setLayoutManager(layoutManager);
        recycler_address.setAdapter(manageAdapter);
        recycler_address.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(this, 10), 0, 0,getColorResouce(R.color.bg_gray)));
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_address:
                break;
        }
    }
}