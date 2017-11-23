package com.shunlian.app.ui.fragment;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ShopCarStoreAdapter;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.presenter.ShopCarPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IShoppingCarView;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 购物车界面
 */

public class ShoppingCarFrag extends BaseFragment implements IShoppingCarView, View.OnClickListener {
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

    private View rootView;
    private ShopCarPresenter shopCarPresenter;
    private ShopCarStoreAdapter shopCarStoreAdapter;
    private ShoppingCarEntity mCarEntity;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_shoppingcar, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        miv_close.setVisibility(View.GONE);
        tv_title.setText(baseContext.getResources().getText(R.string.shopping_car));
        tv_title_right.setText(baseContext.getResources().getText(R.string.edit));
        tv_title_right.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        shopCarPresenter = new ShopCarPresenter(baseContext, this);
        shopCarPresenter.initShopData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_title_right.setOnClickListener(this);
        btn_total_complete.setOnClickListener(this);
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
        Resources resources = baseContext.getResources();
        String totalPrice = resources.getString(R.string.total);
        tv_total_price.setText(String.format(totalPrice, shoppingCarEntity.total_amount));

        String discountStr = resources.getString(R.string.already_discount);
        tv_total_discount.setText(String.format(discountStr, shoppingCarEntity.total_reduce));

        String totalCount = resources.getString(R.string.balance_accounts);
        btn_total_complete.setText(String.format(totalCount, shoppingCarEntity.total_count));

        shopCarStoreAdapter = new ShopCarStoreAdapter(baseContext, shoppingCarEntity.enabled);
        expand_shoppingcar.setAdapter(shopCarStoreAdapter);

        //默认展开
        if (shoppingCarEntity.enabled != null && shoppingCarEntity.enabled.size() != 0) {
            for (int i = 0; i < shoppingCarEntity.enabled.size(); i++) {
                expand_shoppingcar.expandGroup(i);
            }
        }

        //屏蔽父布局点击事件
        expand_shoppingcar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_right:
                Resources resources = baseContext.getResources();
                if (rl_price.getVisibility() == View.VISIBLE) {
                    rl_price.setVisibility(View.GONE);
                    ll_total_edit.setVisibility(View.VISIBLE);
                    tv_title_right.setText(resources.getString(R.string.RegisterTwoAct_finish));
                    setEditMode(true);
                } else {
                    rl_price.setVisibility(View.VISIBLE);
                    ll_total_edit.setVisibility(View.GONE);
                    tv_title_right.setText(resources.getString(R.string.edit));
                    setEditMode(false);
                }
                shopCarStoreAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_total_complete:
                break;
        }
    }

    public void setEditMode(boolean edit) {
        if (mCarEntity.enabled == null || mCarEntity.enabled.size() == 0) {
            return;
        }
        for (ShoppingCarEntity.Enabled enable : mCarEntity.enabled) {
            enable.isEdit = edit;
        }
    }
}
