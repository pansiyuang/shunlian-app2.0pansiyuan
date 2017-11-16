package com.shunlian.app.ui.goods_detail;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.CommentCardViewAdapter;
import com.shunlian.app.adapter.GoodsDetailShopAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HorItemDecoration;
import com.shunlian.app.utils.TransformUtil;
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

public class GoodsDeatilFrag extends BaseFragment implements View.OnClickListener, ParamDialog.OnSelectCallBack {

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

    private GoodsDetailShopAdapter goodsDetailShopAdapter;
    private List<GoodsDeatilEntity.StoreInfo.Item> storeItems = new ArrayList<>();
    private GoodsDeatilEntity.StoreInfo infos;
    private CommentCardViewAdapter commentCardViewAdapter;
    private ParamDialog paramDialog;
    private RecyclerDialog recyclerDialog;
    private List<GoodsDeatilEntity.Combo> mCurrentCombos;
    private List<GoodsDeatilEntity.Attrs> mCurrentAttributes;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_goods_detail, null);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_collection.setOnClickListener(this);
        mll_self_push.setOnClickListener(this);
        mll_self_hot.setOnClickListener(this);
        mtv_combo.setOnClickListener(this);
        mtv_attribute.setOnClickListener(this);
        tv_select_param.setOnClickListener(this);
        miv_footprint.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        GradientDrawable infoDrawable = (GradientDrawable) mtv_discount_info.getBackground();
        infoDrawable.setColor(Color.parseColor("#FB0036"));
        recyclerDialog = new RecyclerDialog(getActivity());
    }

    private void valueAnimator(final View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(16, 8, 3);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if (view instanceof CardView) {
                    CardView cardView = (CardView) view;
                    cardView.setCardElevation(TransformUtil.dip2px(baseActivity, animatedValue));
                }
            }
        });
        valueAnimator.start();
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
    public void goodsInfo(String title, String price, String market_price, String free_shipping, String sales, String address) {
        mtv_title.setText(Common.getPlaceholder(4) + title);
        mtv_discount_info.setText("店铺优惠");
        mtv_price.setText(price);
        mtv_marketPrice.setStrikethrough().setText(getString(R.string.rmb) + market_price);
        if ("0".equals(free_shipping)) {
            mtv_free_shipping.setText(getString(R.string.no_mail));
        } else {
            mtv_free_shipping.setText(getString(R.string.free_shipping));
        }
        mtv_sales.setText(getString(R.string.sold) + sales + getString(R.string.piece));
        mtv_address.setText(address);
    }

    public void setParamDialog(GoodsDeatilEntity goodsDeatilEntity) {
        paramDialog = new ParamDialog(getActivity(), goodsDeatilEntity);
        paramDialog.setOnSelectCallBack(this);
    }

    @Override
    public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
        Common.staticToast("skuid:" + sku.name + "\n" + "count:" + count);
    }
    /**
     *
     * @param is_new 是否新品
     * @param is_explosion 是否爆款
     * @param is_hot 是否热卖
     * @param is_recommend 是否推荐
     */
    public void smallLabel(String is_new, String is_explosion, String is_hot, String is_recommend) {
        if ("1".equals(is_new)){
            mtv_new_goods.setVisibility(View.VISIBLE);
            GradientDrawable newDrawable = (GradientDrawable) mtv_new_goods.getBackground();
            newDrawable.setColor(Color.parseColor("#FBB700"));
        }else {
            mtv_new_goods.setVisibility(View.GONE);
        }

        if ("1".equals(is_explosion)) {
            mtv_explosion_goods.setVisibility(View.VISIBLE);
            GradientDrawable explosionDrawable = (GradientDrawable) mtv_explosion_goods.getBackground();
            explosionDrawable.setColor(Color.parseColor("#FB6400"));
        }else {
            mtv_explosion_goods.setVisibility(View.GONE);
        }

        if ("1".equals(is_hot)) {
            mtv_hot_goods.setVisibility(View.VISIBLE);
            GradientDrawable hotDrawable = (GradientDrawable) mtv_hot_goods.getBackground();
            hotDrawable.setColor(Color.parseColor("#FB3500"));
        }else {
            mtv_hot_goods.setVisibility(View.GONE);
        }

        if ("1".equals(is_recommend)){
            mtv_recommend_goods.setVisibility(View.VISIBLE);
            GradientDrawable recommedDrawable = (GradientDrawable) mtv_recommend_goods.getBackground();
            recommedDrawable.setColor(Color.parseColor("#FB0000"));
        }else {
            mtv_recommend_goods.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mtv_collection:
                GoodsDetailAct detailAct = (GoodsDetailAct) baseActivity;
                if (isAttentionShop){
                    detailAct.delFollowStore();
                    setCollectionState(0);
                }else {
                    detailAct.followStore();
                    setCollectionState(1);
                }
                isAttentionShop = !isAttentionShop;
                break;
            case R.id.mll_self_hot:
                setState(1);
                setStoreOtherGoods(infos.hot);
                break;
            case R.id.mll_self_push:
                setState(2);
                setStoreOtherGoods(infos.push);
                break;
            case R.id.tv_select_param:
                if (paramDialog != null && !paramDialog.isShowing()) {
                    paramDialog.show();
                }
                break;
            case R.id.miv_footprint:
                GoodsDetailAct detailAct1 = (GoodsDetailAct) baseActivity;
                detailAct1.showFootprintList();
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
        }
    }


    private void setState(int state){
        mtv_self_hot.setTextColor(state == 1 ? getResources().getColor(R.color.new_text)
        : getResources().getColor(R.color.value_88));
        view_self_hot.setBackgroundColor(state == 1 ? getResources().getColor(R.color.pink_color)
        :getResources().getColor(R.color.bg_gray_two));

        mtv_self_push.setTextColor(state == 2 ? getResources().getColor(R.color.new_text)
                : getResources().getColor(R.color.value_88));
        view_self_push.setBackgroundColor(state == 2 ? getResources().getColor(R.color.pink_color)
                :getResources().getColor(R.color.bg_gray_two));

        ViewGroup.LayoutParams hotlayoutParams = view_self_hot.getLayoutParams();
        hotlayoutParams.height = state == 1 ? TransformUtil.dip2px(baseActivity,1.0f)
                : TransformUtil.dip2px(baseActivity,0.5f);
        view_self_hot.setLayoutParams(hotlayoutParams);

        ViewGroup.LayoutParams pushlayoutParams = view_self_push.getLayoutParams();
        pushlayoutParams.height = state == 2 ? TransformUtil.dip2px(baseActivity,1.0f)
                :TransformUtil.dip2px(baseActivity,0.5f);
        view_self_push.setLayoutParams(pushlayoutParams);
    }

    private void setCollectionState(int state){
        GradientDrawable background = (GradientDrawable) mtv_collection.getBackground();
        if (state == 1){
            background.setColor(getResources().getColor(R.color.pink_color));
            mtv_collection.setTextColor(Color.WHITE);
            mtv_collection.setText(getString(R.string.already_collected));
        }else {
            background.setColor(getResources().getColor(R.color.transparent));
            mtv_collection.setTextColor(getResources().getColor(R.color.pink_color));
            mtv_collection.setText(getString(R.string.collection));
        }
    }
    /**
     * 优惠券
     * @param vouchers
     */
    public void setVoucher(ArrayList<GoodsDeatilEntity.Voucher> vouchers) {
        if (vouchers != null && vouchers.size() > 0){
            mll_ling_Coupon.setVisibility(View.VISIBLE);
            for (int i = 0; i < vouchers.size(); i++) {
                if (i > 2){
                    break;
                }
                GoodsDeatilEntity.Voucher voucher = vouchers.get(i);
                MyTextView textView = new MyTextView(baseActivity);
                textView.setTextSize(11);
                textView.setTextColor(getResources().getColor(R.color.value_FC2F57));
                textView.setBackgroundResource(R.drawable.edge_frame_r3);
                GradientDrawable background = (GradientDrawable) textView.getBackground();
                background.setColor(Color.WHITE);
                int padding = TransformUtil.dip2px(baseActivity, 4);
                textView.setPadding(padding,padding,padding,padding);
                textView.setText(getString(R.string.pull)+voucher.use_condition+getString(R.string.reduce)+voucher.denomination);
                mll_Coupon.addView(textView);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
                layoutParams.leftMargin = TransformUtil.dip2px(baseActivity,5.5f);
                textView.setLayoutParams(layoutParams);
            }

        }else {
            mll_ling_Coupon.setVisibility(View.GONE);
        }
    }

    /**
     * 店铺信息
     * @param infos
     */
    public void setShopInfo(GoodsDeatilEntity.StoreInfo infos) {
        this.infos = infos;
        GlideUtils.getInstance().loadImage(baseActivity,miv_shop_head,infos.decoration_banner);
        mtv_store_name.setText(infos.decoration_name);
        mtv_goods_count.setText(infos.goods_count);
        mtv_attention_count.setText(infos.attention_count);
        mtv_description_consistency.setText(infos.description_consistency);
        mtv_quality_satisfy.setText(infos.quality_satisfy);

        ratingBar1.setEnabled(false);
        ratingBar1.setNumberOfStars(Integer.valueOf(infos.star));
        ratingBar1.setRating(Integer.valueOf(infos.star));
        if ("1".equals(infos.is_attention)){
            setCollectionState(1);
            isAttentionShop = true;
        }else {
            setCollectionState(0);
            isAttentionShop = false;
        }

        if ("1".equals(infos.quality_logo)){
            mtv_quality_goods.setVisibility(View.VISIBLE);
            GradientDrawable qualityDrawable = (GradientDrawable) mtv_quality_goods.getBackground();
            qualityDrawable.setColor(Color.parseColor("#9414FD"));
        }else {
            mtv_quality_goods.setVisibility(View.GONE);
        }

        setStoreOtherGoods(infos.hot);
    }

    private void setStoreOtherGoods(List<GoodsDeatilEntity.StoreInfo.Item> item){
        storeItems.clear();
        storeItems.addAll(item);
        if (goodsDetailShopAdapter == null) {
            LinearLayoutManager storeManager = new LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false);
            recy_view.setLayoutManager(storeManager);
            recy_view.addItemDecoration(new HorItemDecoration(TransformUtil.dip2px(baseActivity, 12)));
            goodsDetailShopAdapter = new GoodsDetailShopAdapter(baseActivity, false, item);
            recy_view.setAdapter(goodsDetailShopAdapter);
        }else {
            goodsDetailShopAdapter.notifyDataSetChanged();
        }
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
        mtv_comment_num.setText("好评(100)");
        mtv_haopinglv.setText("好评率98.6%");
        LinearLayoutManager manager1 = new LinearLayoutManager(baseActivity,LinearLayoutManager.HORIZONTAL,false);
        recy_cardview.setLayoutManager(manager1);
        commentCardViewAdapter = new CommentCardViewAdapter(baseActivity,false,commentses);
        recy_cardview.setAdapter(commentCardViewAdapter);
        commentCardViewAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                valueAnimator(view);
            }
        });
    }
}
