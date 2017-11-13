package com.shunlian.app.ui.goods_detail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.GoodsDetailPresenter;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.view.IGoodsDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDetailAct extends SideslipBaseActivity implements IGoodsDetailView {

    private FragmentManager supportFragmentManager;
    private GoodsDeatilFrag goodsDeatilFrag;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, GoodsDetailAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_goods_detail;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        GoodsDetailPresenter goodsDetailPresenter = new GoodsDetailPresenter(this, this, "148");
        supportFragmentManager = getSupportFragmentManager();
        goodsDeatilFrag = new GoodsDeatilFrag();
        supportFragmentManager.beginTransaction().add(R.id.mfl_content, goodsDeatilFrag).commit();
    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

    }

    /**
     * 轮播
     *
     * @param pics
     */
    @Override
    public void banner(ArrayList<String> pics) {
        goodsDeatilFrag.setBanner(pics);
    }

    /**
     * 商品的标题和价格
     * 是否包邮
     * 销售量
     * 发货地点
     */
    @Override
    public void goodsInfo(String title, String price, String market_price, String free_shipping, String sales, String address) {
        goodsDeatilFrag.goodsInfo(title, price, market_price, free_shipping, sales, address);
    }

    @Override
    public void paramDialog(List<GoodsDeatilEntity.Specs> specs, List<GoodsDeatilEntity.Sku> skus) {
        goodsDeatilFrag.setParamDialog(specs, skus);
    }

    @Override
    public void attributeDialog() {

    }
}
