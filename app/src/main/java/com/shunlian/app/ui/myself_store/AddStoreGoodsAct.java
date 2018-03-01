package com.shunlian.app.ui.myself_store;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AddGoodsPagerAdapter;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.AddGoodsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.category.CategoryLetterAct;
import com.shunlian.app.view.IAddGoodsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/27.
 */

public class AddStoreGoodsAct extends BaseActivity implements IAddGoodsView, View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_goods_search)
    TextView tv_goods_search;

    @BindView(R.id.tv_brand)
    TextView tv_brand;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.vp_goods)
    ViewPager vp_goods;

    @BindView(R.id.tv_add_goods)
    TextView tv_add_goods;

    @BindView(R.id.tv_selectAll)
    TextView tv_selectAll;

    @BindView(R.id.tv_store_add)
    TextView tv_store_add;

    private String[] titles = {"全部", "推荐", "订单", "收藏", "购物车"};
    private String[] fromList = {"ALL", "RECOMMEND", "ORDERS", "COLLECT", "SPCAR"};
    private List<BaseFragment> goodsFrags;
    private AddGoodsPresenter mPresenter;

    public static List<GoodsDeatilEntity.Goods> currentGoodsList;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, AddStoreGoodsAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_addstoregoods;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.add_goods));

        //设置tablayout标签的显示方式
        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        //循环注入标签
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        for (String tab : titles) {
            tab_layout.addTab(tab_layout.newTab().setText(tab));
        }

        goodsFrags = new ArrayList<>();
        for (String from : fromList) {
            goodsFrags.add(AllGoodsFrag.getInstance(from));
        }
        vp_goods.setAdapter(new AddGoodsPagerAdapter(getSupportFragmentManager(), goodsFrags, titles));
        tab_layout.setupWithViewPager(vp_goods);

        vp_goods.setOffscreenPageLimit(5);
        mPresenter = new AddGoodsPresenter(this, this);
        mPresenter.getFairishNums();

        currentGoodsList = new ArrayList<>();
    }

    @Override
    protected void initListener() {
        tv_brand.setOnClickListener(this);
        tv_store_add.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void getFairishNums(String count) {
        tv_add_goods.setText(String.format(getStringResouce(R.string.add_some_goods), count));
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_brand:
                CategoryLetterAct.startAct(this, new ArrayList<GetListFilterEntity.Brand>(), new ArrayList<String>());
                break;
            case R.id.tv_store_add:
//                mPresenter.addStoreGoods("919,156,137,191");
                break;
        }
        super.onClick(view);
    }
}
