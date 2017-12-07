package com.shunlian.app.ui.receive_adress;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AddressAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
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

public class AddressListActivity extends BaseActivity implements View.OnClickListener, IOrderAddressView, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.recycler_address)
    RecyclerView recycler_address;

    @BindView(R.id.btn_select_address)
    Button btn_select_address;

    private OrderAddressPresenter orderAddressPresenter;
    private AddressAdapter addressAdapter;

    public static void startAct(Context context,String addressId) {
        Intent intent = new Intent(context, AddressListActivity.class);
        intent.putExtra("addressId",addressId);
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
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText(getString(R.string.manage));
        orderAddressPresenter = new OrderAddressPresenter(this, this);
        orderAddressPresenter.getAddressList();
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_select_address.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_address:
                AddAdressAct.startAct(this,null);
                break;
            case R.id.tv_title_right:
                AddressManageActivity.startAct(this);
                break;
        }
    }

    @Override
    public void orderList(List<ConfirmOrderEntity.Address> addressList) {
        addressAdapter = new AddressAdapter(this, false, addressList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycler_address.setLayoutManager(manager);
        recycler_address.setAdapter(addressAdapter);
        recycler_address.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(this, 0.5f), 0, 0, getColorResouce(R.color.bg_gray_two)));
        addressAdapter.setOnItemClickListener(this);
    }

    @Override
    public void delAddressSuccess(String addressId) {

    }

    @Override
    public void delAddressFail() {

    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void onItemClick(View view, int position) {
        addressAdapter.OnItemSelect(position);
    }
}
