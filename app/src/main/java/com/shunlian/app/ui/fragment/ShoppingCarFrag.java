package com.shunlian.app.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DisabledGoodsAdapter;
import com.shunlian.app.adapter.ShopCarStoreAdapter;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.presenter.CarEditPresenter;
import com.shunlian.app.presenter.ShopCarPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IEditCarView;
import com.shunlian.app.view.IShoppingCarView;
import com.shunlian.app.widget.MyImageView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 购物车界面
 */

public class ShoppingCarFrag extends BaseFragment implements IShoppingCarView, IEditCarView, View.OnClickListener, ShopCarStoreAdapter.OnEnableChangeListener {
    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.rl_price)
    RelativeLayout rl_price;

    @BindView(R.id.ll_total_edit)
    LinearLayout ll_total_edit;

    @BindView(R.id.miv_total_select)
    MyImageView miv_total_select;

    @BindView(R.id.tv_total_price)
    TextView tv_total_price;

    @BindView(R.id.tv_total_discount)
    TextView tv_total_discount;

    @BindView(R.id.tv_edit_freight)
    TextView tv_edit_freight;

    @BindView(R.id.btn_total_complete)
    Button btn_total_complete;

    @BindView(R.id.expand_shoppingcar)
    ExpandableListView expand_shoppingcar;

    private HashMap<String, Boolean> editMap; //用来记录店铺分组编辑状态
    private View rootView;
    private View footView;
    private ShopCarPresenter shopCarPresenter;
    private CarEditPresenter carEditPresenter;
    private ShopCarStoreAdapter shopCarStoreAdapter;
    private ShoppingCarEntity mCarEntity;
    private Unbinder mUnbinder;
    private FooterHolderView footerHolderView;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_shoppingcar, container, false);
        footView = inflater.inflate(R.layout.foot_shoppingcar_disable, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        miv_close.setVisibility(View.GONE);
        tv_title.setText(baseContext.getResources().getText(R.string.shopping_car));
        tv_title_right.setText(baseContext.getResources().getText(R.string.edit));
        tv_title_right.setVisibility(View.VISIBLE);
        footerHolderView = new FooterHolderView(footView);
        footView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT)); //添加这句话 防止报错
        expand_shoppingcar.addFooterView(footView);
    }

    @Override
    protected void initData() {
        editMap = new HashMap<>();
        shopCarPresenter = new ShopCarPresenter(baseContext, this);
        carEditPresenter = new CarEditPresenter(baseContext, this);
        shopCarPresenter.initShopData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_title_right.setOnClickListener(this);
        btn_total_complete.setOnClickListener(this);
        footerHolderView.tv_clear_disable.setOnClickListener(this);
    }

    public void getShoppingCarData() {
        if (shopCarPresenter != null) {
            shopCarPresenter.initShopData();
        }
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void OnShoppingCarEntity(ShoppingCarEntity shoppingCarEntity) {
        this.mCarEntity = shoppingCarEntity;
        String totalPrice = getString(R.string.total);
        tv_total_price.setText(String.format(totalPrice, mCarEntity.total_amount));

        String discountStr = getString(R.string.already_discount);
        tv_total_discount.setText(String.format(discountStr, mCarEntity.total_reduce));

        String totalCount = getString(R.string.balance_accounts);
        btn_total_complete.setText(String.format(totalCount, mCarEntity.total_count));

        shopCarStoreAdapter = new ShopCarStoreAdapter(baseContext, mCarEntity.enabled);
        expand_shoppingcar.setAdapter(shopCarStoreAdapter);
        shopCarStoreAdapter.setOnEnableChangeListener(this);

        //默认展开
        if (mCarEntity.enabled != null && mCarEntity.enabled.size() != 0) {
            for (int i = 0; i < mCarEntity.enabled.size(); i++) {
                expand_shoppingcar.expandGroup(i);
                editMap.put(mCarEntity.enabled.get(i).store_id, false);
            }
        }

        //屏蔽父布局点击事件
        expand_shoppingcar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        footerHolderView.recycle_disable.setNestedScrollingEnabled(false);
        footerHolderView.recycle_disable.setLayoutManager(linearLayoutManager);

        footerHolderView.recycle_disable.setAdapter(new DisabledGoodsAdapter(baseContext, false, mCarEntity.disabled));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_right:
                if (getString(R.string.edit).equals(tv_title_right.getText())) {
                    rl_price.setVisibility(View.GONE);
                    ll_total_edit.setVisibility(View.VISIBLE);
                    tv_title_right.setText(getString(R.string.RegisterTwoAct_finish));
                    setEditMode(true);
                } else {
                    rl_price.setVisibility(View.VISIBLE);
                    ll_total_edit.setVisibility(View.GONE);
                    tv_title_right.setText(getString(R.string.edit));
                    setEditMode(false);
                }
                shopCarStoreAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_total_complete:
                break;
            case R.id.tv_clear_disable:
                break;
        }
    }

    public void setEditMode(boolean edit) {
        if (mCarEntity.enabled == null || mCarEntity.enabled.size() == 0) {
            return;
        }
        for (ShoppingCarEntity.Enabled enable : mCarEntity.enabled) {
            enable.isEditAll = edit;
            enable.isEditGood = edit;
            editMap.put(enable.store_id, edit); //修改所有店铺状态
        }
    }

    @Override
    public void OnChangeCount(String goodsId, int count) {
        carEditPresenter.editCar(goodsId, String.valueOf(count), null, null, null);
    }

    @Override
    public void OnChangeSku(String goodsId, String skuId) {
        carEditPresenter.editCar(goodsId, null, skuId, null, null);
    }

    @Override
    public void OnChangeCheck(String goodsId, String isCheck) {
        carEditPresenter.editCar(goodsId, null, null, null, isCheck);
    }

    @Override
    public void OnChangeEdit(String storeId, boolean isEdit) {
        try {
            editMap.put(storeId, isEdit);
            shopCarStoreAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnEditEntity(ShoppingCarEntity shoppingCarEntity) {
        LogUtil.httpLogW("OnEditEntity");
        this.mCarEntity = shoppingCarEntity;
        String totalPrice = getString(R.string.total);
        tv_total_price.setText(String.format(totalPrice, mCarEntity.total_amount));

        String discountStr = getString(R.string.already_discount);
        tv_total_discount.setText(String.format(discountStr, mCarEntity.total_reduce));

        String totalCount = getString(R.string.balance_accounts);
        btn_total_complete.setText(String.format(totalCount, mCarEntity.total_count));

        shopCarStoreAdapter.setEnables(mCarEntity.enabled, editMap);
    }

    public class FooterHolderView {
        @BindView(R.id.tv_clear_disable)
        TextView tv_clear_disable;

        @BindView(R.id.recycle_disable)
        RecyclerView recycle_disable;

        public FooterHolderView(View view) {
            mUnbinder = ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }
}
