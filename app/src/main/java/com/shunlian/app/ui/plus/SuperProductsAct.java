package com.shunlian.app.ui.plus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SuperProductAdapter;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.SuperProductEntity;
import com.shunlian.app.presenter.SuperproductPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.ISuperProductView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/24.
 */

public class SuperProductsAct extends BaseActivity implements ISuperProductView, SuperProductAdapter.OnShareClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    public List<SuperProductEntity.SuperProduct> mData;
    private SuperproductPresenter mPresenter;
    private LinearLayoutManager manager;
    private SuperProductAdapter mAdapter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SuperProductsAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_superior_products;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.super_product));

        mPresenter = new SuperproductPresenter(this, this);
        mPresenter.getProductList();

        mData = new ArrayList<>();
        manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
        recycler_list.setNestedScrollingEnabled(false);

    }

    @Override
    public void getProductList(List<SuperProductEntity.SuperProduct> list) {
        if (!isEmpty(list)) {
            mData.clear();
            mData.addAll(list);
        }

        if (mAdapter == null) {
            mAdapter = new SuperProductAdapter(this, mData);
            mAdapter.setOnShareClickListener(this);
            recycler_list.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onShare(ShareInfoParam infoParam) {
        if (quick_actions != null) {
            visible(quick_actions);
            quick_actions.shareInfo(infoParam);
            quick_actions.shareStyle2Dialog(true, 3);
        }
    }
}
