package com.shunlian.app.ui.goods_detail;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.GoodsDetailAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.widget.FiveStarBar;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.RecyclerDialog;
import com.shunlian.app.widget.banner.Kanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDeatilFrag extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.recy_view_root)
    RecyclerView recy_view_root;
    private LinearLayoutManager manager;
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

    @BindView(R.id.mtv_attribute)
    MyTextView mtv_attribute;

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

    @BindView(R.id.mtv_combo)
    MyTextView mtv_combo;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.recy_cardview)
    RecyclerView recy_cardview;

    @BindView(R.id.mtv_recommend_goods)
    MyTextView mtv_recommend_goods;

    @BindView(R.id.ratingBar1)
    FiveStarBar ratingBar1;

    @BindView(R.id.mtv_quality_goods)
    MyTextView mtv_quality_goods;

    @BindView(R.id.mtv_collection)
    MyTextView mtv_collection;

    @BindView(R.id.mll_Coupon)
    MyLinearLayout mll_Coupon;

    @BindView(R.id.mll_ling_Coupon)
    MyLinearLayout mll_ling_Coupon;

    @BindView(R.id.miv_shop_head)
    MyImageView miv_shop_head;

    @BindView(R.id.mtv_store_name)
    MyTextView mtv_store_name;

    @BindView(R.id.mtv_goods_count)
    MyTextView mtv_goods_count;

    @BindView(R.id.mtv_attention_count)
    MyTextView mtv_attention_count;

    @BindView(R.id.mtv_description_consistency)
    MyTextView mtv_description_consistency;

    @BindView(R.id.mtv_quality_satisfy)
    MyTextView mtv_quality_satisfy;

    @BindView(R.id.mll_self_hot)
    MyLinearLayout mll_self_hot;

    @BindView(R.id.mll_self_push)
    MyLinearLayout mll_self_push;

    @BindView(R.id.view_self_push)
    View view_self_push;

    @BindView(R.id.view_self_hot)
    View view_self_hot;

    @BindView(R.id.mtv_self_push)
    MyTextView mtv_self_push;

    @BindView(R.id.mtv_self_hot)
    MyTextView mtv_self_hot;

    @BindView(R.id.tv_select_param)
    MyTextView tv_select_param;

    @BindView(R.id.miv_footprint)
    MyImageView miv_footprint;

    @BindView(R.id.mtv_comment_num)
    MyTextView mtv_comment_num;

    @BindView(R.id.mtv_haopinglv)
    MyTextView mtv_haopinglv;

    private boolean isAttentionShop;

    private List<GoodsDeatilEntity.StoreInfo.Item> storeItems = new ArrayList<>();
    private GoodsDeatilEntity.StoreInfo infos;
    private ParamDialog paramDialog;
    private RecyclerDialog recyclerDialog;
    private List<GoodsDeatilEntity.Combo> mCurrentCombos;
    private List<GoodsDeatilEntity.Attrs> mCurrentAttributes;
    private List<GoodsDeatilEntity.Voucher> mCurrentVouchers;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_goods_details, null);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mtv_combo.setOnClickListener(this);
        mtv_attribute.setOnClickListener(this);
        tv_select_param.setOnClickListener(this);

        recy_view_root.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                    System.out.println("firstVisibleItemPosition="+firstVisibleItemPosition);
                }
            }
        });
    }

    @Override
    protected void initData() {
        recyclerDialog = new RecyclerDialog(getActivity());
    }

    /**
     * 轮播
     * @param pics
     */
    public void setBanner(ArrayList<String> pics) {

    }

    /**
     * 商品的标题和价格
     * 是否包邮
     * 销售量
     * 发货地点
     */
    public void goodsInfo(String title, String price, String market_price, String free_shipping, String sales, String address) {
    }
    /**
     * 商品详情数据
     */
    public void setGoodsDetailData(GoodsDeatilEntity goodsDeatilEntity) {


        manager = new LinearLayoutManager(baseActivity);
        recy_view_root.setLayoutManager(manager);
        recy_view_root.setNestedScrollingEnabled(false);
        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        recy_view_root.setRecycledViewPool(pool);
        pool.setMaxRecycledViews(0,5);

        ArrayList<String> pics = goodsDeatilEntity.detail.pics;
        recy_view_root.setAdapter(new GoodsDetailAdapter(baseActivity, false,goodsDeatilEntity,pics));

    }
    /**
     *
     * @param is_new 是否新品
     * @param is_explosion 是否爆款
     * @param is_hot 是否热卖
     * @param is_recommend 是否推荐
     */
    public void smallLabel(String is_new, String is_explosion, String is_hot, String is_recommend) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_param:
                if (paramDialog != null && !paramDialog.isShowing()) {
                    paramDialog.show();
                }
                break;
            case R.id.mtv_combo:
                if (recyclerDialog != null && !recyclerDialog.isShowing()) {
                    recyclerDialog.setCombos(mCurrentCombos);
                    recyclerDialog.show();
                }
                break;
            case R.id.mtv_attribute:
                if (recyclerDialog != null && !recyclerDialog.isShowing()) {
                    recyclerDialog.setAttributes(mCurrentAttributes);
                    recyclerDialog.show();
                }
                break;
            case R.id.mll_ling_Coupon:
                if (recyclerDialog != null && !recyclerDialog.isShowing()) {
                    recyclerDialog.setVoucheres(mCurrentVouchers);
                    recyclerDialog.show();
                }
                break;
        }
    }


    private void setState(int state){
    }



    /**
     * 优惠券
     * @param vouchers
     */
    public void setVoucher(ArrayList<GoodsDeatilEntity.Voucher> vouchers) {
        mCurrentVouchers = vouchers;
    }

    /**
     * 店铺信息
     * @param infos
     */
    public void setShopInfo(GoodsDeatilEntity.StoreInfo infos) {
    }

    /**
     * 套餐列表
     * @param combos
     */
    public void setComboDetail(List<GoodsDeatilEntity.Combo> combos) {
        this.mCurrentCombos = combos;
    }

    /**
     * 商品参数列表
     * @param attrses
     */
    public void setGoodsParameter(List<GoodsDeatilEntity.Attrs> attrses) {
        this.mCurrentAttributes = attrses;
    }

    /**
     * 评价列表
     * @param commentses
     */
    public void setCommentList(List<GoodsDeatilEntity.Comments> commentses) {
    }

    /**
     * 商品详情
     * @param detail
     */
    public void setDetail(GoodsDeatilEntity.Detail detail) {
    }



}
