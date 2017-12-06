package com.shunlian.app.ui.order_address;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AddressAdapter;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.presenter.OrderAddressPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IOrderAddressView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/5.
 */

public class OrderListActivity extends BaseActivity implements View.OnClickListener, IOrderAddressView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recycler_address)
    RecyclerView recycler_address;

    @BindView(R.id.btn_select_address)
    Button btn_select_address;

    private OrderAddressPresenter orderAddressPresenter;
    private List<ConfirmOrderEntity.Address> addressList;
    private AddressAdapter addressAdapter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, OrderListActivity.class);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_address:
                break;
        }
    }

    @Override
    public void orderList(List<ConfirmOrderEntity.Address> addressList) {
        this.addressList = addressList;
        addressAdapter = new AddressAdapter(this, false, addressList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycler_address.setLayoutManager(manager);
        recycler_address.setAdapter(addressAdapter);
        recycler_address.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(this, 0.5f), 0, 0, getColorResouce(R.color.bg_gray_two)));
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }
}
