package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HorItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.timer.DDPDownTimerView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.RecyclerDialog;
import com.shunlian.app.widget.VideoBannerWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.shunlian.app.utils.Common.getResources;

/**
 * Created by Administrator on 2017/11/17.
 */

public class GoodsDetailAdapter extends BaseRecyclerAdapter<String> implements ParamDialog.OnSelectCallBack {
    /*
        轮播
     */
    public static final int BANNER_LAYOUT = 1 << 1;
    /*
    商品信息
     */
    public static final int TITLE_LAYOUT = 1 << 2;
    /*
    活动和优惠券
     */
    public static final int ACTIVITY_COUPON_LAYOUT = 1 << 3;
    /*
     *参数和属性
     */
    public static final int PARAM_ATTRS_LAYOUT = 1 << 4;
    /*
    评价
     */
    public static final int COMMNT_LAYOUT = 1 << 5;
    /*
    店铺信息
     */
    public static final int STORE_GOODS_LAYOUT = 1 << 6;
    /*
    参看图文详情
     */
    public static final int GOODS_DETAIL_DIVISION = 1 << 7;
    /*
    优惠券
     */
    public static final int COUPON_LAYOUT = 1 << 8;
    /*
    详情富文本
     */
    public static final int RICH_TEXT_LAYOUT = 1 << 9;
    private static final int ITEM_DIFFERENT = (1 << 3) | 1;//不同条目数

    private final LayoutInflater mInflater;
    private final int mDeviceWidth;
    public ParamDialog paramDialog;
    private GoodsDeatilEntity mGoodsEntity;
    private boolean isAttentionShop;
    private List<GoodsDeatilEntity.StoreInfo.Item> storeItems;
    private GoodsDetailShopAdapter goodsDetailShopAdapter;
    private RecyclerDialog recyclerDialog;
    private MyTextView tv_select_param;
    private StringBuilder strLengthMeasure;//字符串长度测量
    private int detailCouponPosi = -1;//详情下的优惠券位置
    private StoreVoucherAdapter couponAdapter;
    private boolean isStartDownTime = false;
    //private int voucherPosition;//点击优惠券位置
    private long day = 0l;
    private String mRichText = null;//富文本内容
    private long act_start;//活动开始显示的时间
    private String re = "(w=|h=)(\\d+)";
    private Pattern p = Pattern.compile(re);
    private Rect mRect;

    public GoodsDetailAdapter(Context context, GoodsDeatilEntity entity, List<String> lists) {
        super(context, false, lists);
        mInflater = LayoutInflater.from(context);
        mGoodsEntity = entity;
        recyclerDialog = new RecyclerDialog(context);
        paramDialog = new ParamDialog(context, mGoodsEntity);
        paramDialog.setOnSelectCallBack(this);
        mDeviceWidth = DeviceInfoUtil.getDeviceWidth(context);
        act_start = System.currentTimeMillis();

        GoodsDeatilEntity.Detail detail = mGoodsEntity.detail;
        if (detail != null) {
            mRichText = detail.text;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BANNER_LAYOUT;
        } else if (position == 1) {
            return TITLE_LAYOUT;
        } else if (position == 1 << 1) {
            return ACTIVITY_COUPON_LAYOUT;
        } else if (position == (1 | (1 << 1))) {
            return PARAM_ATTRS_LAYOUT;
        } else if (position == (1 << 2)) {
            return COMMNT_LAYOUT;
        } else if (position == ((1 << 2) | 1)) {
            return STORE_GOODS_LAYOUT;
        } else if (position == ((1 << 2) | (1 << 1))) {
            return GOODS_DETAIL_DIVISION;
        } else if (position == ((1 << 2) | 3)) {
            return COUPON_LAYOUT;
        } else if (position == (1 << 3)) {
            if (isEmpty(mRichText)) {
                return super.getItemViewType(position);
            } else {
                return RICH_TEXT_LAYOUT;
            }
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER_LAYOUT:
                View banner_layout = mInflater.inflate(R.layout.video_banner_layout, parent, false);
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
                View coupon_layout = mInflater.inflate(R.layout.only_recycler_layout, parent, false);
                return new CouponHolder(coupon_layout);
            case RICH_TEXT_LAYOUT:
                View rich_text_layout = mInflater.inflate(R.layout.rich_text, parent, false);
                return new RichTextHolder(rich_text_layout);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemCount() {
        if (isEmpty(mRichText)) {
            return super.getItemCount() + ITEM_DIFFERENT - 1;
        } else {
            return super.getItemCount() + ITEM_DIFFERENT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        //LogUtil.zhLogW("onBindViewHolder========"+itemViewType);
        switch (itemViewType) {
            case BANNER_LAYOUT:
                handleBannerTitle(holder, position);
                break;
            case TITLE_LAYOUT:
                handlerTitle(holder, position);
                break;
            case ACTIVITY_COUPON_LAYOUT:
                handlerActivityCoupon(holder, position);
                break;
            case PARAM_ATTRS_LAYOUT:
                //handlerParamAttrs(holder,position);
                break;
            case COMMNT_LAYOUT:
                handlerComment(holder, position);
                break;
            case STORE_GOODS_LAYOUT:
                handlerStoreGoods(holder, position);
                break;
            case GOODS_DETAIL_DIVISION:

                break;
            case COUPON_LAYOUT:
                handlerCoupon(holder, position);
                break;
            case RICH_TEXT_LAYOUT:
                handlerRichText(holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    /**
     * 详情富文本
     * @param holder
     * @param position
     */
    private void handlerRichText(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RichTextHolder) {
            RichTextHolder mHolder = (RichTextHolder) holder;
            MyTextView textView = (MyTextView) mHolder.itemView;
            if (isEmpty(mRichText)) {
                gone(textView);
            } else {
                visible(textView);
                textView.setText(mRichText);
            }
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
        if (holder instanceof CouponHolder) {
            CouponHolder mHolder = (CouponHolder) holder;
            RecyclerView recy_view_coupon = (RecyclerView) mHolder.itemView;
            //详情优惠券
            couponAdapter = new StoreVoucherAdapter(context, false, mGoodsEntity.voucher);
            recy_view_coupon.setAdapter(couponAdapter);

            couponAdapter.setOnItemClickListener((view, posi) -> {
                GoodsDeatilEntity.Voucher voucher = mGoodsEntity.voucher.get(posi);
                if (context instanceof GoodsDetailAct) {
                    GoodsDetailAct goodsDetailAct = (GoodsDetailAct) context;
                    goodsDetailAct.getCouchers(voucher.voucher_id);
                    detailCouponPosi = posi;
                }
            });
        }
    }

    /**
     * 店铺信息
     * @param holder
     * @param position
     */
    private void handlerStoreGoods(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoreGoodsHolder) {
            StoreGoodsHolder mHolder = (StoreGoodsHolder) holder;
            GoodsDeatilEntity.StoreInfo store_info = mGoodsEntity.store_info;
            GlideUtils.getInstance().loadOverrideImage(context,
                    mHolder.miv_shop_head, store_info.store_icon, 100, 100);
            mHolder.mtv_store_name.setText(store_info.decoration_name);
            mHolder.mtv_goods_count.setText(store_info.goods_count);
            mHolder.mtv_attention_count.setText(store_info.attention_count);
            mHolder.mtv_description_consistency.setText(store_info.description_consistency);
            mHolder.mtv_quality_satisfy.setText(store_info.quality_satisfy);
            GlideUtils.getInstance().loadImage(context, mHolder.miv_starBar, store_info.star);
            if ("1".equals(store_info.is_attention)) {
                mHolder.setCollectionState(1);
                isAttentionShop = true;
            } else {
                mHolder.setCollectionState(0);
                isAttentionShop = false;
            }

            if ("1".equals(store_info.quality_logo)) {
                mHolder.mtv_quality_goods.setVisibility(View.VISIBLE);
                GradientDrawable qualityDrawable = (GradientDrawable) mHolder.mtv_quality_goods.getBackground();
                qualityDrawable.setColor(Color.parseColor("#9414FD"));
            } else {
                mHolder.mtv_quality_goods.setVisibility(View.GONE);
            }

            if (isEmpty(store_info.hot)) {
                gone(mHolder.mll_self_hot);
            } else {
                visible(mHolder.mll_self_hot);
            }

            if (isEmpty(store_info.push)) {
                gone(mHolder.mll_self_push);
            } else {
                visible(mHolder.mll_self_push);
            }

            setStoreOtherGoods(mHolder.recy_view, store_info.hot);
        }
    }

    private void setStoreOtherGoods(RecyclerView recy_view, List<GoodsDeatilEntity.StoreInfo.Item> item) {
        if (storeItems == null) storeItems = new ArrayList<>();
        storeItems.clear();
        storeItems.addAll(item);
        if (goodsDetailShopAdapter == null) {
            LinearLayoutManager storeManager = new LinearLayoutManager
                    (context, LinearLayoutManager.HORIZONTAL, false);
            recy_view.setLayoutManager(storeManager);
            recy_view.setNestedScrollingEnabled(false);
            int space = TransformUtil.dip2px(context, 10);
            recy_view.addItemDecoration(new HorItemDecoration(space, 0, 0));
            goodsDetailShopAdapter = new GoodsDetailShopAdapter(context, false, storeItems);
            recy_view.setAdapter(goodsDetailShopAdapter);
            goodsDetailShopAdapter.setOnItemClickListener((v, position) ->
                    GoodsDetailAct.startAct(context, storeItems.get(position).id));
        } else {
            goodsDetailShopAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 评价
     * @param holder
     * @param position
     */
    private void handlerComment(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommntHolder) {
            CommntHolder mHolder = (CommntHolder) holder;

            final ArrayList<GoodsDeatilEntity.Comments> comments = mGoodsEntity.comments;
            GoodsDeatilEntity.GoodsData goods_data = mGoodsEntity.goods_data;
            String comments_num = goods_data.comments_num;
            String star_rate = isEmpty(goods_data.star_rate) ? "0" : goods_data.star_rate;
            mHolder.mtv_comment_num.setText(String.format(getString(R.string.good_comment), comments_num));
            mHolder.mtv_haopinglv.setText(String.format(getString(R.string.praise_rate), star_rate));

            if (isEmpty(comments)) {
                mHolder.recy_cardview.setVisibility(View.GONE);
                mHolder.view_line1.setVisibility(View.GONE);
                mHolder.view_line2.setVisibility(View.VISIBLE);
                mHolder.miv_empty.setVisibility(View.VISIBLE);
            } else {
                mHolder.view_line1.setVisibility(View.VISIBLE);
                mHolder.recy_cardview.setVisibility(View.VISIBLE);
                mHolder.miv_empty.setVisibility(View.GONE);
                mHolder.view_line2.setVisibility(View.GONE);
                CommentCardViewAdapter commentCardViewAdapter = new
                        CommentCardViewAdapter(context, false, comments);
                mHolder.recy_cardview.setAdapter(commentCardViewAdapter);
                commentCardViewAdapter.setOnItemClickListener((view, pos) -> {
                    if (pos >= comments.size()) {
                        valueAnimator(view, null);
                    } else {
                        GoodsDeatilEntity.Comments comments1 = comments.get(pos);
                        valueAnimator(view, comments1.id);
                    }
                });
            }
        }
    }

    private void valueAnimator(final View view, final String id) {
        /*ValueAnimator valueAnimator = ValueAnimator.ofFloat(16,8,3);
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
                goodsDetailAct.commentFrag(id);
            }
        });*/
        GoodsDetailAct goodsDetailAct = (GoodsDetailAct) context;
        goodsDetailAct.commentFrag(id);
    }

    /**
     * 活动优惠券
     * @param holder
     * @param position
     */
    private void handlerActivityCoupon(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ActivityCouponHolder) {
            ActivityCouponHolder mHolder = (ActivityCouponHolder) holder;
            ArrayList<GoodsDeatilEntity.Voucher> vouchers = mGoodsEntity.voucher;
            if (!isEmpty(vouchers)) {
                visible(mHolder.view_coupon,mHolder.mll_ling_Coupon);
                mHolder.mll_Coupon.removeAllViews();
                if (strLengthMeasure == null) {
                    strLengthMeasure = new StringBuilder();
                    mRect = new Rect();
                }
                strLengthMeasure.delete(0, strLengthMeasure.length());
                for (int i = 0; i < vouchers.size(); i++) {
                    if (i > 2)break;
                    GoodsDeatilEntity.Voucher voucher = vouchers.get(i);
                    MyTextView textView = new MyTextView(context);
                    textView.setTextSize(11);
                    textView.setMaxLines(1);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(getResources().getColor(R.color.value_FC2F57));
                    textView.setBackgroundResource(R.drawable.edge_frame_r3);
                    GradientDrawable background = (GradientDrawable) textView.getBackground();
                    background.setColor(Color.WHITE);
                    int padding = TransformUtil.dip2px(context, 4);
                    textView.setPadding(padding, padding, padding, padding);
                    if (Float.parseFloat(voucher.use_condition) == 0) {
                        textView.setText(voucher.denomination.concat("元无门槛优惠券"));
                    } else {
                        textView.setText(getString(R.string.pull)+voucher.use_condition
                                .concat(getString(R.string.reduce))+voucher.denomination);
                    }
                    mHolder.mll_Coupon.addView(textView);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
                    layoutParams.leftMargin = TransformUtil.dip2px(context, 5.5f);
                    textView.setLayoutParams(layoutParams);

                    //长度兼容
                    strLengthMeasure.append(textView.getText());
                    textView.getPaint().getTextBounds(strLengthMeasure.toString(),
                            0,strLengthMeasure.length(),mRect);
                    //strLengthMeasure.length() > 22
                    if ((mRect.right+100) > mDeviceWidth) {
                        mHolder.mll_Coupon.removeViewAt(mHolder.mll_Coupon.getChildCount() - 1);
                        break;
                    }
                }

            } else {
                gone(mHolder.view_coupon,mHolder.mll_ling_Coupon);
            }
            mHolder.mll_act_detail.removeAllViews();
            if (isEmpty(mGoodsEntity.full_cut)
                    && isEmpty(mGoodsEntity.full_discount)
                    && isEmpty(mGoodsEntity.buy_gift)) {
                gone(mHolder.mrl_activity,mHolder.view_activity);
            } else {
                visible(mHolder.mrl_activity,mHolder.view_activity);
            }
            if (mGoodsEntity.full_cut != null && mGoodsEntity.full_cut.size() > 0) {
                setActivityInfo(mHolder.mll_act_detail, 0, mGoodsEntity.full_cut);
            }


            if (mGoodsEntity.full_discount != null && mGoodsEntity.full_discount.size() > 0) {
                setActivityInfo(mHolder.mll_act_detail, 1, mGoodsEntity.full_discount);
            }

            if (mGoodsEntity.buy_gift != null && mGoodsEntity.buy_gift.size() > 0) {
                setActivityInfo(mHolder.mll_act_detail, 2, mGoodsEntity.buy_gift);
            }

            if (isEmpty(mGoodsEntity.combo)) {
                gone(mHolder.mtv_combo,mHolder.view_combo);
            } else {
                visible(mHolder.mtv_combo,mHolder.view_combo);
            }
        }
    }

    /**
     *
     * @param parent
     * @param state 0 = 满减   1 = 满折   2 = 买赠
     * @param detailList
     */
    private void setActivityInfo(ViewGroup parent, int state,
                                 List<GoodsDeatilEntity.ActivityDetail> detailList) {
        View subView1 = mInflater.inflate(R.layout.activity_layout, parent, false);
        MyTextView mtv_title = (MyTextView) subView1.findViewById(R.id.mtv_title);
        GradientDrawable background = (GradientDrawable) mtv_title.getBackground();
        background.setColor(getColor(R.color.value_FEF0F3));
        if (state == 0) {
            mtv_title.setText(String.format(getString(R.string.full_cut), "", ""));
        } else if (state == 1) {
            mtv_title.setText(getString(R.string.full_discount));
        } else {
            mtv_title.setText(getString(R.string.buy_gift));
        }
        MyLinearLayout mll_content = (MyLinearLayout) subView1.findViewById(R.id.mll_content);
        MyTextView textView = new MyTextView(context);
        textView.setTextSize(12);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(getResources().getColor(R.color.text_param_value));
        mll_content.addView(textView);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < detailList.size(); i++) {
            if (i > 2)
                break;
            GoodsDeatilEntity.ActivityDetail ad = detailList.get(i);
            if (state == 0) {
                sb.append(ad.prom_title);
            } else if (state == 1) {
                sb.append(ad.prom_title);
            } else {
                sb.append(ad.prom_title);
            }
            sb.append(",");
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        textView.setText(sb);
        parent.addView(subView1);
    }

    /**
     * 商品信息
     * @param holder
     * @param position
     */
    private void handlerTitle(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleHolder) {
            final TitleHolder mHolder = (TitleHolder) holder;

            int pref_length = 0;
            String title = mGoodsEntity.title;
            final String is_preferential = mGoodsEntity.is_preferential;
            if (mGoodsEntity.tt_act != null) {
                pref_length = 5;//显示天天特惠标题
                title = mGoodsEntity.tt_act.title;
                visible(mHolder.miv_pref);
            } else {
                gone(mHolder.miv_pref);
                if (!isEmpty(is_preferential)) {//显示正常标题
                    GradientDrawable infoDrawable = (GradientDrawable) mHolder.mtv_discount_info.getBackground();
                    infoDrawable.setColor(Color.parseColor("#FB0036"));
                    mHolder.mtv_discount_info.setText(is_preferential);
                    visible(mHolder.mtv_discount_info);
                    pref_length = is_preferential.length();
                } else {
                    gone(mHolder.mtv_discount_info);
                    pref_length = 0;
                }
            }

            if (pref_length != 0) {
                mHolder.mtv_title.setText(Common.getPlaceholder(pref_length) + title);
            } else {
                mHolder.mtv_title.setText(title);
            }

            mHolder.mtv_price.setText(mGoodsEntity.price);
            mHolder.mtv_marketPrice.setStrikethrough().setText(getString(R.string.rmb)+mGoodsEntity.market_price);
            String shipping_fee = mGoodsEntity.shipping_fee;
            if (!isEmpty(shipping_fee) && !"0".equals(shipping_fee)) {
                mHolder.mtv_free_shipping.setText(String.format(getString(R.string.not_mail), shipping_fee));
            } else {
                mHolder.mtv_free_shipping.setText(getString(R.string.free_shipping));
            }
            GoodsDeatilEntity.GoodsData goods_data = mGoodsEntity.goods_data;
            if (goods_data != null) {
                mHolder.mtv_sales.setText(isEmpty(goods_data.sales_desc)?"":goods_data.sales_desc);
                /*int stock = Integer.parseInt(isEmpty(mGoodsEntity.stock)?"0":mGoodsEntity.stock);
                if ("0".equals(mGoodsEntity.status) || stock <= 0){//商品下架或者库存为0显示几个人还想要
                    visible(mHolder.mtv_want);
                    mHolder.mtv_want.setText(goods_data.want_num+"人还想要");
                }else {
                    gone(mHolder.mtv_want);
                }*/
            }
            mHolder.mtv_address.setText(mGoodsEntity.area);

            if ("1".equals(mGoodsEntity.is_new)) {
                visible(mHolder.mtv_new_goods);
                GradientDrawable newDrawable = (GradientDrawable) mHolder.mtv_new_goods.getBackground();
                newDrawable.setColor(Color.parseColor("#FBB700"));
            } else {
                gone(mHolder.mtv_new_goods);
            }

            if ("1".equals(mGoodsEntity.is_explosion)) {
                visible(mHolder.mtv_explosion_goods);
                GradientDrawable explosionDrawable = (GradientDrawable) mHolder.mtv_explosion_goods.getBackground();
                explosionDrawable.setColor(Color.parseColor("#FB6400"));
            } else {
                gone(mHolder.mtv_explosion_goods);
            }

            if ("1".equals(mGoodsEntity.is_hot)) {
                visible(mHolder.mtv_hot_goods);
                GradientDrawable hotDrawable = (GradientDrawable) mHolder.mtv_hot_goods.getBackground();
                hotDrawable.setColor(Color.parseColor("#FB3500"));
            } else {
                gone(mHolder.mtv_hot_goods);
            }

            if ("1".equals(mGoodsEntity.is_recommend)) {
                visible(mHolder.mtv_recommend_goods);
                GradientDrawable recommedDrawable = (GradientDrawable) mHolder.mtv_recommend_goods.getBackground();
                recommedDrawable.setColor(Color.parseColor("#FB0000"));
            } else {
                gone(mHolder.mtv_recommend_goods);
            }


            /**********天天特惠******************/
            daydayPreferential(mHolder);
            /**********天天特惠******************/

            /************专题活动****************/
            specialActivity(mHolder);
            /************专题活动****************/


            /************活动连接****************/
            if (mGoodsEntity.activity != null && !isEmpty(mGoodsEntity.activity.desc)) {
                visible(mHolder.mtv_act);
                mHolder.mtv_act.setText(mGoodsEntity.activity.desc);
            } else {
                gone(mHolder.mtv_act);
            }
        }
    }

    /*
    专题活动
     */
    private void specialActivity(final TitleHolder mHolder) {
        GoodsDeatilEntity.SpecailAct common_activity = mGoodsEntity.common_activity;
        if (common_activity == null) {
            gone(mHolder.mllayout_specail_act, mHolder.mllayout_specail_before_act);
        } else {
            visible(mHolder.mllayout_specail_act);
            String activity_status = common_activity.activity_status;
            if ("1".equals(activity_status)) {//活动未开始
                visible(
                        mHolder.miv_special_pic,
                        mHolder.mllayout_specail_before_act,
                        mHolder.mllayout_specail_before_downtime);
                gone(
                        mHolder.mtv_marketPrice,
                        mHolder.mrlayout_special_preBgL,
                        mHolder.mrlayout_special_preBgR);
                //活动未开始，没有上传宣传图不显示
                if (!isEmpty(common_activity.detail_pic)) {
                    visible(mHolder.miv_special_pic,mHolder.mllayout_specail_act);
                    GlideUtils.getInstance().veryLongPicLoadImage(context,
                            mHolder.miv_special_pic, common_activity.detail_pic);
                }else {
                    gone(mHolder.miv_special_pic,mHolder.mllayout_specail_act);
                }

                mHolder.mtv_special_before_original_price.setStrikethrough()
                        .setText(getString(R.string.rmb)+mGoodsEntity.market_price);
                if ("1".equals(common_activity.if_act_price)) {//显示预览价格
                    visible(mHolder.mtv_special_before_price);
                    mHolder.mtv_special_before_price.setText(
                            "活动价:" + getString(R.string.rmb)+common_activity.actprice);
                } else {
                    gone(mHolder.mtv_special_before_price);
                }

                GradientDrawable background = (GradientDrawable) mHolder
                        .mllayout_before_downTime.getBackground();
                background.setColor(getColor(R.color.transparent));
                int i = TransformUtil.dip2px(context, 1);
                background.setStroke(i, getColor(R.color.my_gray_one));

//                GlideUtils.getInstance().loadImage(context,
//                        mHolder.miv_special_before_pic,mGoodsEntity.pics.get(0));
                if ("1".equals(common_activity.if_time)) {//显示预览倒计时
                    visible(mHolder.mllayout_specail_before_downtime);
                    mHolder.ddp_special_before_downTime.setLabelBackgroundColor(getColor(R.color.transparent));
                    mHolder.ddp_special_before_downTime.setTimeUnitTextColor(getColor(R.color.my_gray_one));
                    mHolder.ddp_special_before_downTime.setTimeTextColor(getColor(R.color.my_gray_one));
                    mHolder.ddp_special_before_downTime.setTimeTextSize(11);
                    mHolder.ddp_special_before_downTime.setTimeUnitTextSize(11);
                    mHolder.ddp_special_before_downTime.setTimeUnitPadding(0);

                    mHolder.ddp_special_before_downTime.cancelDownTimer();
                    String time = isEmpty(common_activity.start_remain_seconds) ? "0" :
                            common_activity.start_remain_seconds;
                    //time = "10";
                    int runTime = (int) ((System.currentTimeMillis() - act_start) / 1000);
                    mHolder.ddp_special_before_downTime.setDownTime(Integer.parseInt(time) - runTime);
                    mHolder.ddp_special_before_downTime.startDownTimer();

                    if (!isStartDownTime) {
                        isStartDownTime = true;
                        mHolder.ddp_special_before_downTime.setDownTimerListener(() -> {
                            isStartDownTime = false;
                            if (context instanceof GoodsDetailAct) {
                                GoodsDetailAct act = (GoodsDetailAct) context;
                                if (!act.isFinishing() && mHolder != null
                                        && mHolder.ddp_special_before_downTime != null) {
                                    mHolder.ddp_special_before_downTime.cancelDownTimer();
                                    return;
                                }
                                act.refreshDetail();
                            }
                        });
                    }
                } else {
                    gone(mHolder.mllayout_specail_before_downtime);
                }

            } else {//活动开始
                visible(
                        mHolder.mrlayout_special_preBgL,
                        mHolder.mrlayout_special_preBgR,
                        mHolder.mllayout_specail_act);

                gone(
                        mHolder.miv_special_pic,
                        mHolder.mtv_marketPrice,
                        mHolder.mtv_price_rmb,
                        mHolder.mtv_price,
                        mHolder.mllayout_specail_before_act,
                        mHolder.mllayout_specail_before_downtime);

                mHolder.mtv_special_price.setText(
                        getString(R.string.rmb)+common_activity.actprice);

                mHolder.mtv_special_original_price.setStrikethrough().setText(
                        "原价:" + getString(R.string.rmb)+common_activity.old_price);
                mHolder.ddp_special_downTime.setLabelBackgroundColor(getColor(R.color.white));
                mHolder.ddp_special_downTime.setTimeUnitTextColor(getColor(R.color.pink_color));
                mHolder.ddp_special_downTime.setTimeTextColor(getColor(R.color.pink_color));
                String format = "距结束仅剩%d天";


                String time = isEmpty(common_activity.end_remain_seconds) ? "0" :
                        common_activity.end_remain_seconds;
                //time = "10";
                int runTime = (int) ((System.currentTimeMillis() - act_start) / 1000);
                long down_time = Long.parseLong(time) - runTime;
                day = down_time / (3600 * 24);
                if (day > 0) {
                    mHolder.mtv_special_title.setText(String.format(format, day));
                } else {
                    mHolder.mtv_special_title.setText("距结束");
                }
                long inner_day = down_time % (3600 * 24);
                mHolder.ddp_special_downTime.cancelDownTimer();
                mHolder.ddp_special_downTime.setDownTime((int) inner_day);
                mHolder.ddp_special_downTime.startDownTimer();

                if (!isStartDownTime) {
                    isStartDownTime = true;
                    mHolder.ddp_special_downTime.setDownTimerListener(() -> {
                        try {
                            if (day > 0) {
                                day = day - 1;
                                if (day > 0) {
                                    mHolder.mtv_special_title.setText(String.format(format, day));
                                } else {
                                    mHolder.mtv_special_title.setText("距结束");
                                }
                                if (mHolder.ddp_special_downTime != null) {
                                    mHolder.ddp_special_downTime.cancelDownTimer();
                                    mHolder.ddp_special_downTime.setDownTime(3600);
                                    mHolder.ddp_special_downTime.startDownTimer();
                                }
                            } else {
                                isStartDownTime = false;
                                if (context instanceof GoodsDetailAct) {
                                    GoodsDetailAct act = (GoodsDetailAct) context;
                                    if (!act.isFinishing() && mHolder != null
                                            && mHolder.ddp_special_downTime != null) {
                                        mHolder.ddp_special_downTime.cancelDownTimer();
                                        return;
                                    }
                                    act.refreshDetail();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }

    /*
    天天特惠
     */
    private void daydayPreferential(final TitleHolder mHolder) {
        GoodsDeatilEntity.TTAct tt_act = mGoodsEntity.tt_act;
        if (tt_act != null) {
            visible(mHolder.mllayout_preferential);
            gone(mHolder.mllayout_common_price, mHolder.mtv_sales);

            if ("1".equals(tt_act.sale)) {//1：活动进行中   0：活动未开始
                mHolder.mrlayout_preBgL.setBackgroundColor(getColor(R.color.pink_color));
                mHolder.mrlayout_preBgR.setBackgroundColor(getColor(R.color.value_FEEBE7));
                mHolder.ddp_downTime.setLabelBackgroundColor(getColor(R.color.new_text));
                mHolder.ddp_downTime.setTimeUnitTextColor(getColor(R.color.new_text));
                mHolder.mtv_act_title.setTextColor(getColor(R.color.pink_color));
                mHolder.mtv_pmarketPrice.setTextColor(getColor(R.color.value_BF012A));
                //gone(mHolder.mtv_follow_count);
                //visible(mHolder.seekbar_grow,mHolder.mtv_desc);
                //mHolder.seekbar_grow.setProgress(Integer.parseInt(tt_act.percent));
                //mHolder.mtv_desc.setText(tt_act.str_surplus_stock);
            } else {
                mHolder.mrlayout_preBgL.setBackgroundColor(getColor(R.color.value_2096F2));
                mHolder.mrlayout_preBgR.setBackgroundColor(getColor(R.color.value_DBEFFF));
                mHolder.ddp_downTime.setLabelBackgroundColor(getColor(R.color.value_2096F2));
                mHolder.ddp_downTime.setTimeUnitTextColor(getColor(R.color.value_2096F2));
                mHolder.mtv_act_title.setTextColor(getColor(R.color.new_text));
                mHolder.mtv_pmarketPrice.setTextColor(getColor(R.color.value_1B78C1));
                //String remind_count = isEmpty(tt_act.remind_count) ? "0" : tt_act.remind_count;
                //mHolder.mtv_follow_count.setText(remind_count.concat("人关注"));
                //visible(mHolder.mtv_follow_count);
                //gone(mHolder.seekbar_grow,mHolder.mtv_desc);
            }

            mHolder.mtv_pPrice.setText(tt_act.act_price);
            mHolder.mtv_pmarketPrice.setStrikethrough()
                    .setText(getString(R.string.rmb)+tt_act.market_price);
            mHolder.mtv_act_title.setText(tt_act.content);

            mHolder.ddp_downTime.cancelDownTimer();
            String time = isEmpty(tt_act.time) ? "0" : tt_act.time;
            //time = "10";
            int runTime = (int) ((System.currentTimeMillis() - act_start) / 1000);
            mHolder.ddp_downTime.setDownTime(Integer.parseInt(time) - runTime);
            mHolder.ddp_downTime.startDownTimer();

            if (!isStartDownTime) {
                isStartDownTime = true;
                mHolder.ddp_downTime.setDownTimerListener(() -> {
                    isStartDownTime = false;
                    if (context instanceof GoodsDetailAct) {
                        GoodsDetailAct act = (GoodsDetailAct) context;
                        if (!act.isFinishing() && mHolder != null && mHolder.ddp_downTime != null) {
                            mHolder.ddp_downTime.cancelDownTimer();
                            return;
                        }
                        act.refreshDetail();
                    }
                });
            }
        } else {
            visible(mHolder.mtv_sales, mHolder.mllayout_common_price);
            gone(mHolder.mllayout_preferential);
        }
    }

    /**
     * 轮播和顶部商品信息
     * @param holder
     * @param position
     */
    private void handleBannerTitle(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerHolder) {
            BannerHolder mHolder = (BannerHolder) holder;
            if (mGoodsEntity.pics != null) {
                String type = mGoodsEntity.type;
               /*switch (type){
                    case "1":
                        mHolder.kanner.setBanner(mGoodsEntity.pics, 1);
                        break;
                    case "2":
                        mHolder.kanner.setBanner(mGoodsEntity.pics, 2);
                        break;
                    default:
                        mHolder.kanner.setBanner(mGoodsEntity.pics);
                        break;
                }
                 mHolder.kanner.setOnItemClickL(pos -> {
                    BigImgEntity entity = new BigImgEntity();
                    entity.itemList = mGoodsEntity.pics;
                    entity.index = pos;
                    LookBigImgAct.startAct(context, entity);
                });*/
                //String path = "http://img.v2.shunliandongli.com/msgFile/20180725152719_847.mp4";
                mHolder.vbw.setBanner(mGoodsEntity.video,mGoodsEntity.pics,isEmpty(type)?0:Integer.parseInt(type));
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
        if (holder instanceof PicListHolder) {
            final PicListHolder mHolder = (PicListHolder) holder;
            String s = null;
            if (isEmpty(mRichText)) {
                s = lists.get(position - ITEM_DIFFERENT + 1);
            } else {
                s = lists.get(position - ITEM_DIFFERENT);
            }

            //LogUtil.zhLogW("====s: "+s);

            if (Pattern.matches(".*(w=\\d+&h=\\d+).*", s)) {
                Matcher m = p.matcher(s);
                int w = 0;
                int h = 0;
                if (m.find()) {
                    w = Integer.parseInt(m.group(2));
                } else {
                    w = 720;
                }
                if (m.find()) {
                    h = Integer.parseInt(m.group(2));
                } else {
                    h = 330;
                }
                //LogUtil.zhLogW("===w="+w+"  h="+h);
                int i = (int) (mDeviceWidth * h * 1.0f / w);
                GlideUtils.getInstance()
                        .loadOverrideImage(context, mHolder.miv_pic, s, mDeviceWidth, i);
            } else {
                int i = (int) (mDeviceWidth * 330 * 1.0f / 720);
                GlideUtils.getInstance()
                        .loadOverrideImage(context, mHolder.miv_pic, s, mDeviceWidth, i);
            }
        }
    }

    @Override
    public void onSelectComplete(GoodsDeatilEntity.Sku sku, int count) {
//        Common.staticToast("skuid:" + sku.name + "\n" + "count:" + count);
        if (tv_select_param != null && sku != null)
            tv_select_param.setText(getString(R.string.selection)+"  "+sku.name);

        if (context instanceof GoodsDetailAct) {
            GoodsDetailAct goodsDetailAct = (GoodsDetailAct) context;
            goodsDetailAct.selectGoodsInfo(sku, count);
        }
    }

    /**
     * 刷新领取优惠券状态
     * @param voucher
     */
    public void refreshVoucherState(GoodsDeatilEntity.Voucher voucher) {
        if (detailCouponPosi != -1) {
            if (couponAdapter != null) {
                mGoodsEntity.voucher.remove(detailCouponPosi);
                mGoodsEntity.voucher.add(detailCouponPosi, voucher);
                couponAdapter.notifyItemChanged(detailCouponPosi);
            }
        } else {
            if (recyclerDialog != null) {//领取成功之后id == voucher_id
                recyclerDialog.getVoucherSuccess(voucher.voucher_id, voucher.is_get);
            }
        }
        detailCouponPosi = -1;
    }


    public class PicListHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        public PicListHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            itemView.setOnClickListener((v) -> {
                String s = null;
                BigImgEntity entity = new BigImgEntity();
                entity.itemList = (ArrayList<String>) lists;
                if (isEmpty(mRichText)) {
                    entity.index = getAdapterPosition() - ITEM_DIFFERENT + 1;
                } else {
                    entity.index = getAdapterPosition() - ITEM_DIFFERENT;
                }
                LookBigImgAct.startAct(context, entity);
            });
        }
    }

    public class TitleHolder extends BaseRecyclerViewHolder {

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

        @BindView(R.id.ddp_downTime)
        DDPDownTimerView ddp_downTime;

        @BindView(R.id.mllayout_preferential)
        MyLinearLayout mllayout_preferential;

        @BindView(R.id.mtv_pPrice)
        MyTextView mtv_pPrice;

        @BindView(R.id.mtv_pmarketPrice)
        MyTextView mtv_pmarketPrice;

        @BindView(R.id.mtv_act_title)
        MyTextView mtv_act_title;

        @BindView(R.id.mllayout_common_price)
        MyLinearLayout mllayout_common_price;

        @BindView(R.id.miv_pref)
        MyImageView miv_pref;

        @BindView(R.id.mrlayout_preBgL)
        MyRelativeLayout mrlayout_preBgL;

        @BindView(R.id.mrlayout_preBgR)
        MyRelativeLayout mrlayout_preBgR;

        /*@BindView(R.id.mtv_follow_count)
        MyTextView mtv_follow_count;*/

        /*@BindView(R.id.seekbar_grow)
        ProgressBar seekbar_grow;*/

        /*@BindView(R.id.mtv_desc)
        MyTextView mtv_desc;*/

        @BindView(R.id.mtv_act)
        MyTextView mtv_act;

        @BindView(R.id.mtv_price_rmb)
        MyTextView mtv_price_rmb;

        /*专题活动*/
        @BindView(R.id.mllayout_specail_act)
        MyLinearLayout mllayout_specail_act;

        @BindView(R.id.miv_special_pic)
        MyImageView miv_special_pic;

        @BindView(R.id.mrlayout_special_preBgL)
        MyRelativeLayout mrlayout_special_preBgL;

        @BindView(R.id.mtv_special_price)
        MyTextView mtv_special_price;

        @BindView(R.id.mtv_special_original_price)
        MyTextView mtv_special_original_price;

        @BindView(R.id.mrlayout_special_preBgR)
        MyRelativeLayout mrlayout_special_preBgR;

        @BindView(R.id.ddp_special_downTime)
        DDPDownTimerView ddp_special_downTime;

        @BindView(R.id.mllayout_specail_before_act)
        MyLinearLayout mllayout_specail_before_act;

        @BindView(R.id.mtv_special_before_original_price)
        MyTextView mtv_special_before_original_price;

        @BindView(R.id.mtv_special_before_price)
        MyTextView mtv_special_before_price;

        @BindView(R.id.mllayout_before_downTime)
        MyLinearLayout mllayout_before_downTime;

        @BindView(R.id.mllayout_specail_before_downtime)
        MyLinearLayout mllayout_specail_before_downtime;

        @BindView(R.id.ddp_special_before_downTime)
        DDPDownTimerView ddp_special_before_downTime;

        @BindView(R.id.miv_special_before_pic)
        MyImageView miv_special_before_pic;

        @BindView(R.id.mtv_special_title)
        MyTextView mtv_special_title;

        @BindView(R.id.llayout_plus)
        LinearLayout llayout_plus;

        @BindView(R.id.mtv_plus_prefPrice)
        MyTextView mtv_plus_prefPrice;

        @BindView(R.id.mtv_want)
        MyTextView mtv_want;

        public TitleHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            if (isEmpty(mGoodsEntity.self_buy_earn)) {
                gone(llayout_plus);
            } else {
                visible(llayout_plus);
                mtv_plus_prefPrice.setText(getString(R.string.rmb) + mGoodsEntity.self_buy_earn);
            }
        }

        @OnClick(R.id.mtv_act)
        public void actTitle() {
            GoodsDeatilEntity.Act activity = mGoodsEntity.activity;
            if (activity.url != null) {
                Common.goGoGo(context, activity.url.type, activity.url.item_id);
            }
        }

        @OnClick({R.id.miv_share, R.id.mtv_share})
        public void share() {
            ((GoodsDetailAct) context).moreAnim();
        }
    }

    public class BannerHolder extends BaseRecyclerViewHolder {
        /*@BindView(R.id.kanner)
        Kanner kanner;*/

        @BindView(R.id.vbw)
        VideoBannerWrapper vbw;

        public BannerHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            ViewGroup.LayoutParams layoutParams = vbw.getLayoutParams();
            layoutParams.width = mDeviceWidth;
            layoutParams.height = mDeviceWidth;
            vbw.setLayoutParams(layoutParams);
        }
    }

    public class ActivityCouponHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mll_Coupon)
        MyLinearLayout mll_Coupon;

        @BindView(R.id.mll_ling_Coupon)
        MyLinearLayout mll_ling_Coupon;

        @BindView(R.id.mll_act_detail)
        MyLinearLayout mll_act_detail;

        @BindView(R.id.mrl_activity)
        MyRelativeLayout mrl_activity;

        @BindView(R.id.mtv_combo)
        MyTextView mtv_combo;

        @BindView(R.id.view_coupon)
        View view_coupon;

        @BindView(R.id.view_activity)
        View view_activity;

        @BindView(R.id.view_combo)
        View view_combo;

        public ActivityCouponHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            mtv_combo.setOnClickListener(this);
            mll_ling_Coupon.setOnClickListener(this);
            mrl_activity.setOnClickListener(this);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mll_ling_Coupon:
                    if (recyclerDialog == null) {
                        recyclerDialog = new RecyclerDialog(context);
                    }
                    if (!recyclerDialog.isShowing()) {
                        recyclerDialog.setVoucheres(mGoodsEntity.voucher);
                        recyclerDialog.show();
                        recyclerDialog.setOnVoucherCallBack(new RecyclerDialog.OnVoucherCallBack() {
                            @Override
                            public void itemVoucher(GoodsDeatilEntity.Voucher voucher, int position) {
                                if (context instanceof GoodsDetailAct) {
                                    //voucherPosition = position;
                                    GoodsDetailAct goodsDetailAct = (GoodsDetailAct) context;
                                    goodsDetailAct.getCouchers(voucher.voucher_id);
                                }
                            }
                        });
                    }
                    break;
                case R.id.mtv_combo:
                    if (recyclerDialog == null) {
                        recyclerDialog = new RecyclerDialog(context);
                    }
                    if (!recyclerDialog.isShowing()) {
                        recyclerDialog.setCombos(mGoodsEntity.combo, mGoodsEntity.id);
                        recyclerDialog.show();
                    }
                    break;
                case R.id.mrl_activity:
                    if (recyclerDialog == null) {
                        recyclerDialog = new RecyclerDialog(context);
                    }
                    if (!recyclerDialog.isShowing()) {
                        String store_id = mGoodsEntity.store_info.store_id;
                        recyclerDialog.setActivity(mGoodsEntity.full_cut,
                                mGoodsEntity.full_discount, mGoodsEntity.buy_gift, store_id);
                        recyclerDialog.show();
                    }
                    break;
            }
        }
    }

    public class ParamAttrsHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_params)
        MyTextView mtv_params;

        @BindView(R.id.tv_select_param)
        MyTextView tv_select_param;

        @BindView(R.id.mtv_reason)
        MyTextView mtv_reason;

        @BindView(R.id.mtv_send_time)
        MyTextView mtv_send_time;

        @BindView(R.id.miv_reason)
        MyImageView miv_reason;

        @BindView(R.id.miv_send_time)
        MyImageView miv_send_time;

        @BindView(R.id.mtv_certified_products)
        MyTextView mtv_certified_products;

        @BindView(R.id.miv_certified_products)
        MyImageView miv_certified_products;

        @BindView(R.id.view_params)
        View view_params;

        public ParamAttrsHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            if (!isEmpty(mGoodsEntity.attrs)) {
                mtv_params.setOnClickListener(this);
                visible(mtv_params, view_params);
            } else {
                gone(mtv_params, view_params);
            }

            GoodsDetailAdapter.this.tv_select_param = tv_select_param;
            tv_select_param.setOnClickListener(this);
            if (!isEmpty(mGoodsEntity.return_7)) {
                mtv_reason.setText(mGoodsEntity.return_7);
                visible(miv_reason, mtv_reason);
            } else {
                gone(miv_reason, mtv_reason);
            }

            if (!isEmpty(mGoodsEntity.send_time)) {
                mtv_send_time.setText(mGoodsEntity.send_time);
                visible(miv_send_time, mtv_send_time);
            } else {
                gone(miv_send_time, mtv_send_time);
            }

            if (!isEmpty(mGoodsEntity.quality_guarantee)) {
                mtv_certified_products.setText(mGoodsEntity.quality_guarantee);
                visible(miv_certified_products, mtv_certified_products);
            } else {
                gone(miv_certified_products, mtv_certified_products);
            }

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_select_param:
                    if (paramDialog == null) {
                        paramDialog = new ParamDialog(context, mGoodsEntity);
                        paramDialog.setOnSelectCallBack(GoodsDetailAdapter.this);
                    }
                    if (paramDialog != null && !paramDialog.isShowing()) {
                        paramDialog.show();
                    }
                    break;
                case R.id.mtv_params:
                    if (recyclerDialog == null) {
                        recyclerDialog = new RecyclerDialog(context);
                    }
                    if (!recyclerDialog.isShowing()) {
                        recyclerDialog.setAttributes(mGoodsEntity.attrs);
                        recyclerDialog.show();
                    }
                    break;
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

        @BindView(R.id.miv_empty)
        MyImageView miv_empty;

        @BindView(R.id.view_line1)
        View view_line1;

        @BindView(R.id.view_line2)
        View view_line2;

        public CommntHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            mtv_comment_num.setOnClickListener(this);
            miv_empty.setOnClickListener(this);
            mtv_haopinglv.setOnClickListener(this);
            LinearLayoutManager manager1 = new LinearLayoutManager
                    (context, LinearLayoutManager.HORIZONTAL, false);
            recy_cardview.setLayoutManager(manager1);
            recy_cardview.setNestedScrollingEnabled(false);
            recy_cardview.setFocusable(false);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            GoodsDetailAct goodsDetailAct = (GoodsDetailAct) context;
            goodsDetailAct.commentFrag(null);
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

//        @BindView(R.id.ratingBar1)
//        FiveStarBar ratingBar1;

        @BindView(R.id.mtv_collection)
        MyTextView mtv_collection;

        @BindView(R.id.recy_view)
        RecyclerView recy_view;

        @BindView(R.id.miv_starBar)
        MyImageView miv_starBar;

        public StoreGoodsHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(true);
            mtv_collection.setOnClickListener(this);
            mll_self_hot.setOnClickListener(this);
            mll_self_push.setOnClickListener(this);
            miv_shop_head.setOnClickListener(this);
            mtv_store_name.setOnClickListener(this);
            mtv_quality_goods.setOnClickListener(this);
            miv_starBar.setOnClickListener(this);
            recy_view.setFocusable(false);
        }

        public void setCollectionState(int state) {
            GradientDrawable background = (GradientDrawable) mtv_collection.getBackground();
            if (state == 1) {
                background.setColor(getResources().getColor(R.color.pink_color));
                mtv_collection.setTextColor(Color.WHITE);
                mtv_collection.setText(getString(R.string.discover_alear_follow));
            } else {
                background.setColor(getResources().getColor(R.color.transparent));
                mtv_collection.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_collection.setText(getString(R.string.discover_follow));
            }
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void followStoreState(DefMessageEvent event) {
            setCollectionState(event.followStoreState);
            EventBus.getDefault().unregister(StoreGoodsHolder.this);
        }


        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            GoodsDeatilEntity.StoreInfo store_info = mGoodsEntity.store_info;
            switch (v.getId()) {
                case R.id.mtv_collection:
                    if (!Common.isAlreadyLogin()){
                        Common.goGoGo(context,"login");
                        return;
                    }
                    try {
                        EventBus.getDefault().register(StoreGoodsHolder.this);
                    }catch (EventBusException e){}
                    GoodsDetailAct detailAct = (GoodsDetailAct) context;
                    if (isAttentionShop) {
                        detailAct.delFollowStore();
                    } else {
                        detailAct.followStore();
                    }
                    isAttentionShop = !isAttentionShop;
                    break;
                case R.id.mll_self_hot:
                    if (!isEmpty(store_info.push)) {
                        setState(1);
                        setStoreOtherGoods(recy_view, store_info.hot);
                    }
                    break;
                case R.id.mll_self_push:
                    setState(2);
                    setStoreOtherGoods(recy_view, store_info.push);
                    break;
                case R.id.miv_starBar:
                case R.id.miv_shop_head:
                case R.id.mtv_store_name:
                case R.id.mtv_quality_goods:
                    StoreAct.startAct(context, store_info.store_id);
                    break;
            }
        }

        private void setState(int state) {
            mtv_self_hot.setTextColor(state == 1 ? getResources().getColor(R.color.new_text)
                    : getResources().getColor(R.color.value_88));

            mtv_self_push.setTextColor(state == 2 ? getResources().getColor(R.color.new_text)
                    : getResources().getColor(R.color.value_88));

            view_self_hot.setVisibility(state == 1 ? View.VISIBLE : View.INVISIBLE);
            view_self_push.setVisibility(state == 2 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public class GoodsDetailDivisionHolder extends BaseRecyclerViewHolder {

        public GoodsDetailDivisionHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
        }
    }

    public class CouponHolder extends BaseRecyclerViewHolder {

        public CouponHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            RecyclerView recy_view_coupon = (RecyclerView) itemView;
            LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recy_view_coupon.setLayoutManager(manager);
            recy_view_coupon.setNestedScrollingEnabled(false);
            recy_view_coupon.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }

    public class RichTextHolder extends BaseRecyclerViewHolder {

        public RichTextHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
        }
    }
}
