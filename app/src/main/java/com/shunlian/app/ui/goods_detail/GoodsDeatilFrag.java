package com.shunlian.app.ui.goods_detail;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.Kanner;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDeatilFrag extends BaseFragment {

    @BindView(R.id.kanner)
    Kanner kanner;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_price)
    MyTextView mtv_price;

    @BindView(R.id.mtv_marketPrice)
    MyTextView mtv_marketPrice;

    @BindView(R.id.mtv_free_shipping)
    MyTextView mtv_free_shipping;

    @BindView(R.id.mtv_sales)
    MyTextView mtv_sales;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_new_goods)
    MyTextView mtv_new_goods;

    @BindView(R.id.mtv_explosion_goods)
    MyTextView mtv_explosion_goods;

    @BindView(R.id.mtv_hot_goods)
    MyTextView mtv_hot_goods;

    @BindView(R.id.mtv_discount_info)
    MyTextView mtv_discount_info;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_goods_detail,null);
    }

    @Override
    protected void initData() {
        GradientDrawable newDrawable = (GradientDrawable) mtv_new_goods.getBackground();
        newDrawable.setColor(Color.parseColor("#FBB700"));

        GradientDrawable explosionDrawable = (GradientDrawable) mtv_explosion_goods.getBackground();
        explosionDrawable.setColor(Color.parseColor("#FB6400"));

        GradientDrawable hotDrawable = (GradientDrawable) mtv_hot_goods.getBackground();
        hotDrawable.setColor(Color.parseColor("#FB3500"));

        GradientDrawable infoDrawable = (GradientDrawable) mtv_discount_info.getBackground();
        infoDrawable.setColor(Color.parseColor("#FB0036"));
    }

    /**
     * 轮播
     * @param pics
     */
    public void setBanner(ArrayList<String> pics) {
        kanner.setBanner(pics);
    }

    /**
     * 商品的标题和价格
     * 是否包邮
     * 销售量
     * 发货地点
     */
    public void goodsInfo(String title,String price,String market_price,String free_shipping,String sales,String address) {
        mtv_title.setText(Common.getPlaceholder(4)+title);
        mtv_discount_info.setText("店铺优惠");
        mtv_price.setText(price);
        mtv_marketPrice.setStrikethrough().setText(market_price);
        if ("0".equals(free_shipping)){
            mtv_free_shipping.setText("不包邮");
        }else {
            mtv_free_shipping.setText("包邮");
        }
        mtv_sales.setText("已售"+sales+"件");
        mtv_address.setText(address);
    }

}
