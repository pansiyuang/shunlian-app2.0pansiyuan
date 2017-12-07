package com.shunlian.app.ui.receive_adress;

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
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IOrderAddressView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/6.
 */

public class AddressManageActivity extends BaseActivity implements View.OnClickListener, IOrderAddressView, AddressManageAdapter.OnAddressDelListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recycler_address)
    RecyclerView recycler_address;

    @BindView(R.id.btn_select_address)
    Button btn_select_address;

    private OrderAddressPresenter orderAddressPresenter;
    private AddressManageAdapter manageAdapter;
    private List<ConfirmOrderEntity.Address> addList;


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
        tv_title.setText(getString(R.string.address_manage));
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
        addList = addressList;
        manageAdapter = new AddressManageAdapter(this, false, addList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_address.setLayoutManager(layoutManager);
        recycler_address.setAdapter(manageAdapter);
        recycler_address.setNestedScrollingEnabled(false);
        recycler_address.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(this, 10), 0, 0, getColorResouce(R.color.bg_gray)));

        manageAdapter.setOnAddressDelListener(this);
    }

    @Override
    public void delAddressSuccess(String addressId) {
        if (addList == null && addList.size() == 0) {
            return;
        }
        for (int i = 0; i < addList.size(); i++) {
            if (addressId.equals(addList.get(i).id)) {
                addList.remove(i);
                manageAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void delAddressFail() {
        Common.staticToast(getString(R.string.del_fail));
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

    @Override
    public void addressDel(int position) {
        ConfirmOrderEntity.Address address = addList.get(position);
        String addressId = address.id;
        orderAddressPresenter.delAddress(addressId);
    }

    @Override
    public void addressEdit(int position) {
            AddAdressAct.startAct(this,addList.get(position));
    }
}