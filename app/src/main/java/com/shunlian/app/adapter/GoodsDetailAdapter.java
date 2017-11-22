package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HorItemDecoration;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.FiveStarBar;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.banner.Kanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.Common.getResources;

/**
 * Created by Administrator on 2017/11/17.
 */

public class GoodsDetailAdapter extends BaseRecyclerAdapter<String> {
    /*
        轮播
     */
    public static final int BANNER_LAYOUT = 2;
    /*
    商品信息
     */
    public static final int TITLE_LAYOUT = 3;
    /*
    活动和优惠券
     */
    public static final int ACTIVITY_COUPON_LAYOUT = 4;
    /*
     *参数和属性
     */
    public static final int PARAM_ATTRS_LAYOUT = 5;
    /*
    评价
     */
    public static final int COMMNT_LAYOUT = 6;
    /*
    店铺信息
     */
    public static final int STORE_GOODS_LAYOUT = 7;
    /*
    参看图文详情
     */
    public static final int GOODS_DETAIL_DIVISION = 8;
    /*
    优惠券
     */
    public static final int COUPON_LAYOUT = 9;
    private final LayoutInflater mInflater;
    private GoodsDeatilEntity mGoodsEntity;
    private boolean isAttentionShop;
    private List<GoodsDeatilEntity.StoreInfo.Item> storeItems = new ArrayList<>();
    private GoodsDetailShopAdapter goodsDetailShopAdapter;
    private static final int ITEM_DIFFERENT = 8;//不同条目数
    private ParamDialog paramDialog;

    public GoodsDetailAdapter(Context context, boolean isShowFooter, GoodsDeatilEntity entity, List<String> lists) {
        super(context, isShowFooter, lists);
        mInflater = LayoutInflater.from(context);
        mGoodsEntity = entity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return BANNER_LAYOUT;
        }else if (position == 1){
            return TITLE_LAYOUT;
        }else if (position == 2){
            return ACTIVITY_COUPON_LAYOUT;
        }else if (position == 3){
            return PARAM_ATTRS_LAYOUT;
        }else if (position == 4){
            return COMMNT_LAYOUT;
        }else if (position == 5){
            return STORE_GOODS_LAYOUT;
        }else if (position == 6){
            return GOODS_DETAIL_DIVISION;
        }else if (position == 7){
            return COUPON_LAYOUT;
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case BANNER_LAYOUT:
                View banner_layout = mInflater.inflate(R.layout.banner, parent, false);
                return new BannerHolder(banner_layout);
            case TITLE_LAYOUT:
                View title_layout = mInflater.inflate(R.layout.title_layout, parent, false);
                return new TitleHolder(title_layout);
            case ACTIVITY_COUPON_LAYOUT:
                View activity_coupon_layout = mInflater.inflate(R.layout.activity_coupon_layout, parent, false);
                return new ActivityCouponHolder(activity_coupon_layout);
            case PARAM_ATTRS_LAYOUT:
                View param_attrs_layout = mInflater.inflate(R.layout.param_attrs_layout, parent, false);
                return new ParamAttrsHolder(param_attrs_layout);
            case COMMNT_LAYOUT:
                View commnt_layout = mInflater.inflate(R.layout.commnt_layout, parent, false);
                return new CommntHolder(commnt_layout);
            case STORE_GOODS_LAYOUT:
                View store_goods_layout = mInflater.inflate(R.layout.store_goods_layout, parent, false);
                return new StoreGoodsHolder(store_goods_layout);
            case GOODS_DETAIL_DIVISION:
                View goods_detail_division = mInflater.inflate(R.layout.goods_detail_division, parent, false);
                return new GoodsDetailDivisionHolder(goods_detail_division);
            case COUPON_LAYOUT:
                View coupon_layout = mInflater.inflate(R.layout.coupon_layout, parent, false);
                return new CouponHolder(coupon_layout);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + ITEM_DIFFERENT;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case BANNER_LAYOUT:
                handleBannerTitle(holder,position);
                break;
            case TITLE_LAYOUT:
                handlerTitle(holder,position);
                break;
            case ACTIVITY_COUPON_LAYOUT:
                handlerActivityCoupon(holder,position);
                break;
            case PARAM_ATTRS_LAYOUT:
                handlerParamAttrs(holder,position);
                break;
            case COMMNT_LAYOUT:
                handlerComment(holder,position);
                break;
            case STORE_GOODS_LAYOUT:
                handlerStoreGoods(holder,position);
                break;
            case GOODS_DETAIL_DIVISION:

                break;
            case COUPON_LAYOUT:
                handlerCoupon(holder,position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    /**
     * 商品参数和属性
     * @param holder
     * @param position
     */
    private void handlerParamAttrs(RecyclerView.ViewHolder holder, int position) {

    }

    /**
     * 详情优惠券
     * @param holder
     * @param position
     */
    private void handlerCoupon(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CouponHolder){
            CouponHolder mHolder = (CouponHolder) holder;
            RecyclerView recy_view_coupon = (RecyclerView) mHolder.itemView;
            ArrayList<GoodsDeatilEntity.Voucher> vouchers = mGoodsEntity.voucher;
            //详情优惠券

            SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter<GoodsDeatilEntity.Voucher>
                    (context,R.layout.item_coupon,vouchers){

                @Override
                public void convert(SimpleViewHolder holder, GoodsDeatilEntity.Voucher voucher, int position) {
                    MyTextView mtv_price = holder.getView(R.id.mtv_price);
                    MyTextView mtv_name = holder.getView(R.id.mtv_name);
                    MyTextView mtv_full_cut = holder.getView(R.id.mtv_full_cut);
                    MyRelativeLayout mrl_bg = holder.getView(R.id.mrl_bg);

                    mtv_price.setText(voucher.denomination);
                    mtv_name.setText(voucher.title);
                    String format = "满%s元可用";
                    mtv_full_cut.setText(String.format(format,voucher.use_condition));
                    if("1".equals(voucher.is_get)){
                        mrl_bg.setBackgroundResource(R.mipmap.img_youhuiquan_2);
                    }else {
                        mrl_bg.setBackgroundResource(R.mipmap.img_youhuiquan_1);
                    }
                }
            };
            recy_view_coupon.setAdapter(adapter);
        }
    }

    /**
     * 店铺信息
     * @param holder
     * @param position
     */
    private void handlerStoreGoods(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoreGoodsHolder){
            StoreGoodsHolder mHolder = (StoreGoodsHolder) holder;
            GoodsDeatilEntity.StoreInfo store_info = mGoodsEntity.store_info;
            GlideUtils.getInstance().loadImage(context,mHolder.miv_shop_head,store_info.decoration_banner);
            mHolder.mtv_store_name.setText(store_info.decoration_name);
            mHolder.mtv_goods_count.setText(store_info.goods_count);
            mHolder.mtv_attention_count.setText(store_info.attention_count);
            mHolder.mtv_description_consistency.setText(store_info.description_consistency);
            mHolder.mtv_quality_satisfy.setText(store_info.quality_satisfy);

            mHolder.ratingBar1.setEnabled(false);
            mHolder.ratingBar1.setNumberOfStars(Integer.valueOf(store_info.star));
            mHolder.ratingBar1.setRating(Integer.valueOf(store_info.star));
            if ("1".equals(store_info.is_attention)){
                mHolder.setCollectionState(1);
                isAttentionShop = true;
            }else {
                mHolder.setCollectionState(0);
                isAttentionShop = false;
            }

            if ("1".equals(store_info.quality_logo)){
                mHolder.mtv_quality_goods.setVisibility(View.VISIBLE);
                GradientDrawable qualityDrawable = (GradientDrawable) mHolder.mtv_quality_goods.getBackground();
                qualityDrawable.setColor(Color.parseColor("#9414FD"));
            }else {
                mHolder.mtv_quality_goods.setVisibility(View.GONE);
            }

            setStoreOtherGoods(mHolder.recy_view,store_info.hot);
        }
    }

    private void setStoreOtherGoods(RecyclerView recy_view, List<GoodsDeatilEntity.StoreInfo.Item> item){
        storeItems.clear();
        storeItems.addAll(item);
        if (goodsDetailShopAdapter == null) {
            LinearLayoutManager storeManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recy_view.setLayoutManager(storeManager);
            recy_view.setNestedScrollingEnabled(false);
            recy_view.addItemDecoration(new HorItemDecoration(TransformUtil.dip2px(context, 12),0,0));
            goodsDetailShopAdapter = new GoodsDetailShopAdapter(context, false, item);
            recy_view.setAdapter(goodsDetailShopAdapter);
        }else {
            goodsDetailShopAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 评价
     * @param holder
     * @param position
     */
    private void handlerComment(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommntHolder){
            CommntHolder mHolder = (CommntHolder) holder;

            ArrayList<GoodsDeatilEntity.Comments> comments = mGoodsEntity.comments;
            mHolder.mtv_comment_num.setText("好评(100)");
            mHolder.mtv_haopinglv.setText("好评率98.6%");
            CommentCardViewAdapter commentCardViewAdapter = new CommentCardViewAdapter(context,false,comments);
            mHolder.recy_cardview.setAdapter(commentCardViewAdapter);
            commentCardViewAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    valueAnimator(view);
                }
            });
        }
    }

    private void valueAnimator(final View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(16,8,3);
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if (view instanceof CardView){
                    CardView cardView = (CardView) view;
                    cardView.setCardElevation(TransformUtil.dip2px(context, animatedValue));
                }
            }
        });

        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                super.onAnimationEnd(animation);
                GoodsDetailAct goodsDetailAct = (GoodsDetailAct) context;
                goodsDetailAct.commentFrag();
            }
        });
    }

    /**
     * 活动优惠券
     * @param holder
     * @param position
     */
    private void handlerActivityCoupon(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ActivityCouponHolder){
            ActivityCouponHolder mHolder = (ActivityCouponHolder) holder;
            ArrayList<GoodsDeatilEntity.Voucher> vouchers = mGoodsEntity.voucher;
            if (vouchers != null && vouchers.size() > 0){
                mHolder.mll_ling_Coupon.setVisibility(View.VISIBLE);
                mHolder.mll_Coupon.removeAllViews();
                for (int i = 0; i < vouchers.size(); i++) {
                    if (i > 2){
                        break;
                    }else {
                        GoodsDeatilEntity.Voucher voucher = vouchers.get(i);
                        MyTextView textView = new MyTextView(context);
                        textView.setTextSize(11);
                        textView.setTextColor(getResources().getColor(R.color.value_FC2F57));
                        textView.setBackgroundResource(R.drawable.edge_frame_r3);
                        GradientDrawable background = (GradientDrawable) textView.getBackground();
                        background.setColor(Color.WHITE);
                        int padding = TransformUtil.dip2px(context, 4);
                        textView.setPadding(padding, padding, padding, padding);
                        textView.setText(getString(R.string.pull) + voucher.use_condition + getString(R.string.reduce) + voucher.denomination);
                        mHolder.mll_Coupon.addView(textView);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
                        layoutParams.leftMargin = TransformUtil.dip2px(context, 5.5f);
                        textView.setLayoutParams(layoutParams);
                    }
                }

            }else {
                mHolder.mll_ling_Coupon.setVisibility(View.GONE);
            }
            mHolder.mll_act_detail.removeAllViews();
            if (isEmpty(mGoodsEntity.full_cut)
                    && isEmpty(mGoodsEntity.full_discount)
                    && isEmpty(mGoodsEntity.buy_gift)){
                mHolder.mrl_activity.setVisibility(View.GONE);
            }else {
                mHolder.mrl_activity.setVisibility(View.VISIBLE);
            }
            if (mGoodsEntity.full_cut != null && mGoodsEntity.full_cut.size() > 0){
               setActivityInfo(mHolder.mll_act_detail,0,mGoodsEntity.full_cut);
            }


            if (mGoodsEntity.full_discount != null && mGoodsEntity.full_discount.size() > 0){
                setActivityInfo(mHolder.mll_act_detail,1,mGoodsEntity.full_discount);
            }

            if (mGoodsEntity.buy_gift != null && mGoodsEntity.buy_gift.size() > 0){
                setActivityInfo(mHolder.mll_act_detail,2,mGoodsEntity.buy_gift);
            }

        }
    }

    private boolean isEmpty(List list){
        if (list == null){
            return true;
        }

        if (list.size() == 0){
            return true;
        }else {
            return false;
        }
    }
    /**
     *
     * @param parent
     * @param state 0 = 满减   1 = 满折   2 = 买赠
     * @param detailList
     */
    private void setActivityInfo(ViewGroup parent,int state,
                                 List<GoodsDeatilEntity.ActivityDetail> detailList){
        View subView1 = mInflater.inflate(R.layout.activity_layout, parent, false);
        MyTextView mtv_title = (MyTextView) subView1.findViewById(R.id.mtv_title);
        GradientDrawable background = (GradientDrawable) mtv_title.getBackground();
        background.setColor(getResources().getColor(R.color.value_FEF0F3));
        if (state == 0) {
            mtv_title.setText("满减");
        }else if (state == 1){
            mtv_title.setText("满折");
        }else {
            mtv_title.setText("买赠");
        }
        MyLinearLayout mll_content = (MyLinearLayout) subView1.findViewById(R.id.mll_content);
        MyTextView textView = new MyTextView(context);
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.text_param_value));
        int padding = TransformUtil.dip2px(context, 4);
        textView.setPadding(padding, padding, padding, padding);
        mll_content.addView(textView);
        StringBuilder sb = new StringBuilder();
        String format = null;
        if (state == 0) {
            format = "满%S减%S";
        }else if (state == 1){
            format = "满%s打%s折";
        }else {
            format = "";
        }

        for (int i = 0; i < detailList.size(); i++) {
            if (i > 1)
                break;
            GoodsDeatilEntity.ActivityDetail ad = detailList.get(i);
            if (state == 0) {
                sb.append(String.format(format,ad.money_type_condition,ad.money_type_discount));
            }else if (state == 1){
                sb.append(String.format(format,ad.qty_type_condition,ad.qty_type_discount));
            }else {
                sb.append(ad.promotion_title);
            }

            if (i < 1){
                sb.append(",");
            }
        }
        textView.setText(sb);
        parent.addView(subView1);
    }
    /**
     * 商品信息
     * @param holder
     * @param position
     */
    private void handlerTitle(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleHolder){
            TitleHolder mHolder = (TitleHolder) holder;

            GradientDrawable infoDrawable = (GradientDrawable) mHolder.mtv_discount_info.getBackground();
            infoDrawable.setColor(Color.parseColor("#FB0036"));
            mHolder.mtv_title.setText(Common.getPlaceholder(4) + mGoodsEntity.title);
            mHolder.mtv_discount_info.setText("店铺优惠");
            mHolder.mtv_price.setText(mGoodsEntity.price);
            mHolder.mtv_marketPrice.setStrikethrough().setText(getString(R.string.rmb) + mGoodsEntity.market_price);
            if ("0".equals(mGoodsEntity.free_shipping)) {
                mHolder.mtv_free_shipping.setText(getString(R.string.no_mail));
            } else {
                mHolder.mtv_free_shipping.setText(getString(R.string.free_shipping));
            }
            GoodsDeatilEntity.GoodsData goods_data = mGoodsEntity.goods_data;
            if (goods_data != null)
                mHolder.mtv_sales.setText(getString(R.string.sold) +
                        goods_data.sales + getString(R.string.piece));
            mHolder.mtv_address.setText(mGoodsEntity.area);

            if ("1".equals(mGoodsEntity.is_new)){
                mHolder.mtv_new_goods.setVisibility(View.VISIBLE);
                GradientDrawable newDrawable = (GradientDrawable) mHolder.mtv_new_goods.getBackground();
                newDrawable.setColor(Color.parseColor("#FBB700"));
            }else {
                mHolder.mtv_new_goods.setVisibility(View.GONE);
            }

            if ("1".equals(mGoodsEntity.is_explosion)) {
                mHolder.mtv_explosion_goods.setVisibility(View.VISIBLE);
                GradientDrawable explosionDrawable = (GradientDrawable) mHolder.mtv_explosion_goods.getBackground();
                explosionDrawable.setColor(Color.parseColor("#FB6400"));
            }else {
                mHolder.mtv_explosion_goods.setVisibility(View.GONE);
            }

            if ("1".equals(mGoodsEntity.is_hot)) {
                mHolder.mtv_hot_goods.setVisibility(View.VISIBLE);
                GradientDrawable hotDrawable = (GradientDrawable) mHolder.mtv_hot_goods.getBackground();
                hotDrawable.setColor(Color.parseColor("#FB3500"));
            }else {
                mHolder.mtv_hot_goods.setVisibility(View.GONE);
            }

            if ("1".equals(mGoodsEntity.is_recommend)){
                mHolder.mtv_recommend_goods.setVisibility(View.VISIBLE);
                GradientDrawable recommedDrawable = (GradientDrawable) mHolder.mtv_recommend_goods.getBackground();
                recommedDrawable.setColor(Color.parseColor("#FB0000"));
            }else {
                mHolder.mtv_recommend_goods.setVisibility(View.GONE);
            }
        }
    }
    /**
     * 轮播和顶部商品信息
     * @param holder
     * @param position
     */
    private void handleBannerTitle(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerHolder){
            BannerHolder mHolder = (BannerHolder) holder;
            if (mGoodsEntity.pics != null) {
                mHolder.kanner.setBanner(mGoodsEntity.pics);
            }
        }
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View detailView = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false);
        return new PicListHolder(detailView);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PicListHolder){
            PicListHolder mHolder = (PicListHolder) holder;
            String s = lists.get(position - ITEM_DIFFERENT);
            GlideUtils.getInstance().loadImage(context,mHolder.miv_pic,s);
        }
    }


    public class PicListHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;
        public PicListHolder(View itemView) {
            super(itemView);
            miv_pic.setWHProportion(720,332);
        }
    }

    public class TitleHolder extends BaseRecyclerViewHolder{

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

        @BindView(R.id.mtv_recommend_goods)
        MyTextView mtv_recommend_goods;

        public TitleHolder(View itemView) {
            super(itemView);
        }
    }

    public class BannerHolder extends BaseRecyclerViewHolder{
        @BindView(R.id.kanner)
        Kanner kanner;

        public BannerHolder(View itemView) {
            super(itemView);
        }
    }

    public class ActivityCouponHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mll_Coupon)
        MyLinearLayout mll_Coupon;

        @BindView(R.id.mll_ling_Coupon)
        MyLinearLayout mll_ling_Coupon;

        @BindView(R.id.mll_act_detail)
        MyLinearLayout mll_act_detail;

        @BindView(R.id.mrl_activity)
        MyRelativeLayout mrl_activity;
        public ActivityCouponHolder(View itemView) {
            super(itemView);
        }
    }

    public class ParamAttrsHolder extends BaseRecyclerViewHolder implements View.OnClickListener, ParamDialog.OnSelectCallBack {

        @BindView(R.id.tv_select_param)
        MyTextView tv_select_param;
        private ParamDialog paramDialog;

        public ParamAttrsHolder(View itemView) {
            super(itemView);
            tv_select_param.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_select_param:
                    if (paramDialog == null) {
                        paramDialog = new ParamDialog(context, mGoodsEntity);
                        paramDialog.setOnSelectCallBack(this);
                    }
                    if (paramDialog != null && !paramDialog.isShowing()) {
                        paramDialog.show();
                    }
                    break;
            }
        }

        @Override
        public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
            Common.staticToast("skuid:" + sku.name + "\n" + "count:" + count);
            if (context instanceof GoodsDetailAct){
                GoodsDetailAct goodsDetailAct = (GoodsDetailAct) context;
                goodsDetailAct.selectGoodsInfo(sku,count);
            }
        }
    }


    public class CommntHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_comment_num)
        MyTextView mtv_comment_num;

        @BindView(R.id.mtv_haopinglv)
        MyTextView mtv_haopinglv;

        @BindView(R.id.recy_cardview)
        RecyclerView recy_cardview;
        public CommntHolder(View itemView) {
            super(itemView);
            LinearLayoutManager manager1 = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            recy_cardview.setLayoutManager(manager1);
            recy_cardview.setNestedScrollingEnabled(false);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

        }
    }

    public class StoreGoodsHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

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

        @BindView(R.id.mtv_quality_goods)
        MyTextView mtv_quality_goods;

        @BindView(R.id.ratingBar1)
        FiveStarBar ratingBar1;

        @BindView(R.id.mtv_collection)
        MyTextView mtv_collection;

        @BindView(R.id.recy_view)
        RecyclerView recy_view;
        public StoreGoodsHolder(View itemView) {
            super(itemView);
            mtv_collection.setOnClickListener(this);
            mll_self_hot.setOnClickListener(this);
            mll_self_push.setOnClickListener(this);
        }

        public void setCollectionState(int state){
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
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mtv_collection:
                    if (TextUtils.isEmpty(SharedPrefUtil.getSharedPrfString("token",""))){
                        Common.staticToast("尚未登录");
                        return;
                    }
                    GoodsDetailAct detailAct = (GoodsDetailAct) context;
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
                    GoodsDeatilEntity.StoreInfo store_info = mGoodsEntity.store_info;
                    setStoreOtherGoods(recy_view,store_info.hot);
                    break;
                case R.id.mll_self_push:
                    GoodsDeatilEntity.StoreInfo store_info1 = mGoodsEntity.store_info;
                    setState(2);
                    setStoreOtherGoods(recy_view,store_info1.push);
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
            hotlayoutParams.height = state == 1 ? TransformUtil.dip2px(context,1.0f)
                    : TransformUtil.dip2px(context,0.5f);
            view_self_hot.setLayoutParams(hotlayoutParams);

            ViewGroup.LayoutParams pushlayoutParams = view_self_push.getLayoutParams();
            pushlayoutParams.height = state == 2 ? TransformUtil.dip2px(context,1.0f)
                    :TransformUtil.dip2px(context,0.5f);
            view_self_push.setLayoutParams(pushlayoutParams);
        }
    }

    public class GoodsDetailDivisionHolder extends BaseRecyclerViewHolder{

        public GoodsDetailDivisionHolder(View itemView) {
            super(itemView);
        }
    }

    public class CouponHolder extends BaseRecyclerViewHolder{

        public CouponHolder(View itemView) {
            super(itemView);
            RecyclerView recy_view_coupon = (RecyclerView) itemView;
            LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            recy_view_coupon.setLayoutManager(manager);
            recy_view_coupon.setNestedScrollingEnabled(false);
            int space = TransformUtil.dip2px(context, 10);
            recy_view_coupon.addItemDecoration(new HorItemDecoration(space,space,space));
        }
    }
}