package com.shunlian.app.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
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

public class ShoppingCarFrag extends BaseFragment implements IShoppingCarView {
    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    private View rootView;
    private ShopCarPresenter shopCarPresenter;

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
        LogUtil.httpLogW("initData()");
    }

    public void getShoppingCarData() {
        if (shopCarPresenter != null) {
            shopCarPresenter.initShopData();
            LogUtil.httpLogW("initShopData()");
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
        LogUtil.httpLogW("购物车");
    }
}
