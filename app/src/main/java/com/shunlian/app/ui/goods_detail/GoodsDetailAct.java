package com.shunlian.app.ui.goods_detail;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.CommentAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.presenter.GoodsDetailPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IGoodsDetailView;
import com.shunlian.app.widget.FootprintDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.RollNumView;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDetailAct extends SideslipBaseActivity implements IGoodsDetailView, View.OnClickListener {

    public static final String FRAG_GOODS = GoodsDeatilFrag.class.getName();
    public static final String FRAG_COMMENT = CommentFrag.class.getName();
    public static final int GOODS_ID = 0;//商品
    public static final int COMMENT_ID = 1;//评论
    public static final int DETAIL_ID = 2;//详情
    public static int CURRENT_ID = GOODS_ID;
    public int toolbarHeight;//toolbar高
    public int bottomListHeight;//底部导航高度


    @BindView(R.id.rnview)
    RollNumView rnview;

    @BindView(R.id.mtv_add_car)
    MyTextView mtv_add_car;

    @BindView(R.id.rl_rootview)
    RelativeLayout rl_rootview;

    @BindView(R.id.miv_is_fav)
    MyImageView miv_is_fav;

    @BindView(R.id.mtv_buy_immediately)
    MyTextView mtv_buy_immediately;

    @BindView(R.id.mll_share)
    MyLinearLayout mll_share;

    @BindView(R.id.miv_more)
    MyImageView miv_more;

    @BindView(R.id.miv_close_share)
    MyImageView miv_close_share;

    @BindView(R.id.mll_goods)
    MyLinearLayout mll_goods;

    @BindView(R.id.mtv_goods)
    MyTextView mtv_goods;

    @BindView(R.id.view_goods)
    View view_goods;

    @BindView(R.id.mll_detail)
    MyLinearLayout mll_detail;

    @BindView(R.id.mtv_detail)
    MyTextView mtv_detail;

    @BindView(R.id.view_detail)
    View view_detail;

    @BindView(R.id.mll_comment)
    MyLinearLayout mll_comment;

    @BindView(R.id.mtv_comment)
    MyTextView mtv_comment;

    @BindView(R.id.view_comment)
    View view_comment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mll_item)
    MyLinearLayout mll_item;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.mll_bottom)
    MyLinearLayout mll_bottom;

    @BindView(R.id.mllayout_car)
    MyLinearLayout mllayout_car;

    @BindView(R.id.mll_chat)
    MyLinearLayout mll_chat;

    @BindView(R.id.line)
    View line;

    @BindView(R.id.mtv_off_shelf)
    MyTextView mtv_off_shelf;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.mtv_want)
    MyTextView mtv_want;

    private GoodsDeatilFrag goodsDeatilFrag;
    private MyImageView myImageView;
    private GoodsDetailPresenter goodsDetailPresenter;
    private String store_id;
    //private GoodsDeatilEntity.Sku sku;
    private CommentFrag commentFrag;
    private GoodsDeatilEntity.StoreInfo store_info;
    private GoodsDeatilEntity mGoodsDeatilEntity;
    private FootprintEntity mFootprintEntity;
    private FootprintDialog footprintDialog;
    private String goodsId;
    private Map<String, BaseFragment> fragments;
    private float[] mCurrentPosition = new float[2];
    private PathMeasure mPathMeasure;
    private boolean isStopAnimation;
    private int goodsCount;//商品数量
    private int offset;//导航栏先从透明到完全显示的偏移量
    private boolean isAddcart = false;//是否加入购物车
    private boolean isNowBuy = false;//是否立即购买
    private String favId;//收藏商品id
    private int num;//加入购物车的商品数量
    private int currentQuickAction = -1;//当前快速点击位置
    private ShareInfoParam mShareInfoParam;
    private float mStatusBarAlpha;

    public static void startAct(Context context, String goodsId) {
        Intent intent = new Intent(context, GoodsDetailAct.class);
        intent.putExtra("goodsId", goodsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_goods_detail;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_add_car.setOnClickListener(this);
        miv_more.setOnClickListener(this);
        miv_close_share.setOnClickListener(this);
        mll_goods.setOnClickListener(this);
        mll_detail.setOnClickListener(this);
        mll_comment.setOnClickListener(this);
        miv_close.setOnClickListener(this);
        mtv_buy_immediately.setOnClickListener(this);
        miv_is_fav.setOnClickListener(this);
        mllayout_car.setOnClickListener(this);
        mll_chat.setOnClickListener(this);
        mtv_want.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        goodsId = getIntent().getStringExtra("goodsId");

        initConstant();
        defToolbar();
        goodsFrag();
        goodsDetailPresenter = new GoodsDetailPresenter(this, this, goodsId);
        carNum();

        //猜你喜欢列表初始化
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recy_view.setLayoutManager(manager);
        recy_view.addItemDecoration(new GridSpacingItemDecoration
                (TransformUtil.dip2px(this, 5), false));
        ViewGroup.LayoutParams recyLayoutParams = recy_view.getLayoutParams();
        recyLayoutParams.height = TransformUtil.countRealHeight(this, 678);
        EventBus.getDefault().register(this);
    }

    /*
    初始化常量
     */
    private void initConstant() {
        fragments = new HashMap();
        int statusBarHeight = ImmersionBar.getStatusBarHeight(this);
        int deviceWidth = DeviceInfoUtil.getDeviceWidth(this);
        //偏移量
        offset = (deviceWidth - toolbarHeight - statusBarHeight) / 2;
        //底部按钮高
        mll_bottom.post(() -> {
            if (mll_bottom == null) {
                unbinder = ButterKnife.bind(this);
                bottomListHeight = mll_bottom.getMeasuredHeight();
            }
        });
        RelativeLayout.LayoutParams
                shareLayoutParams = (RelativeLayout.LayoutParams) mll_share.getLayoutParams();
        shareLayoutParams.topMargin = statusBarHeight;
    }

    private void carNum() {
        rnview.setMode(RollNumView.Mode.UP);
        rnview.setTextColor(Color.WHITE);
        rnview.setTextSize(10);
        rnview.setVisibility(View.GONE);
    }

    public void defToolbar() {
        if (immersionBar == null) {
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.titleBar(toolbar, false)
                .statusBarDarkFont(true, 0.2f)
                .addTag(GoodsDetailAct.class.getName())
                .init();

        ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
        toolbarHeight = toolbarParams.height;
    }

    @Override
    protected void onStart() {
        super.onStart();
        resetStatusBar();
    }

    private void resetStatusBar() {
        if (immersionBar == null)
            immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarAlpha(mStatusBarAlpha)
                .addTag(GoodsDetailAct.class.getName())
                .init();
    }

    /*
        商品
         */
    public void goodsFrag() {
        if (goodsDeatilFrag == null) {
            goodsDeatilFrag = new GoodsDeatilFrag();
            fragments.put(FRAG_GOODS, goodsDeatilFrag);
        } else {
            goodsDeatilFrag = (GoodsDeatilFrag) fragments.get(FRAG_GOODS);
        }
        visible(mll_goods, mll_detail);
        mll_goods.setEnabled(true);
        mll_detail.setEnabled(true);
        switchContent(goodsDeatilFrag);
    }

    /*
    评价
     */
    public void commentFrag(String id) {
        if (commentFrag == null) {
            commentFrag = new CommentFrag();
            Bundle bundle = new Bundle();
            bundle.putString("goodsId", goodsId);
            commentFrag.setArguments(bundle);
            fragments.put(FRAG_COMMENT, commentFrag);
        } else {
            commentFrag = (CommentFrag) fragments.get(FRAG_COMMENT);
        }
        mll_goods.setVisibility(View.INVISIBLE);
        mll_goods.setEnabled(false);
        mll_detail.setVisibility(View.INVISIBLE);
        mll_detail.setEnabled(false);
        mtv_comment.setTextColor(getColorResouce(R.color.new_text));
        view_comment.setVisibility(View.INVISIBLE);
        switchContent(commentFrag);
        commentFrag.setPresenter(goodsDetailPresenter, id);
        goodsDetailPresenter.commentList(GoodsDetailPresenter.COMMENT_EMPTY_CODE,
                GoodsDetailPresenter.COMMENT_FAILURE_CODE, true, goodsId, "ALL", "1", id);
        setToolbar();
    }

    /**
     * 导航栏渐变
     *
     * @param position
     * @param totalDy
     */
    public void setBgColor(int position, int totalDy) {
        ImmersionBar immersionBar = ImmersionBar.with(this)
                .addViewSupportTransformColor(toolbar, R.color.white);
        if (totalDy <= offset) {
            if (totalDy <= 0) totalDy = 0;

            float alpha = (float) totalDy / offset;
            immersionBar.statusBarAlpha(alpha)
                    .addTag(GoodsDetailAct.class.getName())
                    .init();
            mll_item.setAlpha(alpha);

            float v = 1.0f - alpha * 2;
            if (v <= 0) {
                v = alpha * 2 - 1;
                setImg(2, 1);
            } else {
                setImg(1, 2);
            }
            miv_close.setAlpha(v);
            miv_is_fav.setAlpha(v);
            miv_more.setAlpha(v);
            if (alpha < 1.0f)
                line.setVisibility(View.INVISIBLE);
            else
                visible(line);
        } else {
            setToolbar();
        }
    }

    public void setToolbar() {
        setImg(2, 1);
        immersionBar.statusBarAlpha(1.0f)
                .addTag(GoodsDetailAct.class.getName())
                .init();
        mll_item.setAlpha(1.0f);
        miv_close.setAlpha(1.0f);
        miv_is_fav.setAlpha(1.0f);
        miv_more.setAlpha(1.0f);
        line.setVisibility(View.VISIBLE);
    }

    /*
    替换fragment内容
     */
    public void switchContent(Fragment show) {
        if (show != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!show.isAdded()) {
                ft.add(R.id.mfl_content, show);
            } else {
                ft.show(show);
            }

            if (fragments != null && fragments.size() > 0) {
                Iterator<String> keys = fragments.keySet().iterator();
                while (keys.hasNext()) {
                    String next = keys.next();
                    BaseFragment baseFragment = fragments.get(next);
                    if (show != baseFragment) {
                        if (baseFragment != null && baseFragment.isVisible()) {
                            ft.hide(baseFragment);
                        }
                    }
                }
            }
            ft.commit();
        }
    }

    /*
    滑动过程中顶部图片改变状态
     */
    private void setImg(int status, int oldStatus) {
        if (status != oldStatus) {
            if (status == 1) {
                miv_close.setImageResource(R.mipmap.icon_more_fanhui);
                if (TextUtils.isEmpty(favId) || "0".equals(favId)) {
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_n);
                } else {
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_h);
                }
                miv_more.setImageResource(R.mipmap.icon_more_gengduo);
            } else {
                miv_close.setImageResource(R.mipmap.img_more_fanhui_n);
                if (TextUtils.isEmpty(favId) || "0".equals(favId)) {
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_n);
                } else {
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_h);
                }
                miv_more.setImageResource(R.mipmap.icon_more_n);
            }
        }
    }


    @Override
    public void showFailureView(int rquest_code) {
        if (goodsDeatilFrag != null && rquest_code == 100)
            goodsDeatilFrag.onFailure();
    }

    @Override
    public void showDataEmptyView(int rquest_code) {
    }

    @Override
    public void getUserId(String userId) {
        if (isEmpty(userId) || "0".equals(userId)) {
            Common.staticToast("该商家未开通客服");
            return;
        }
        ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();
        chatMember.shop_id = store_id;
        chatMember.nickname = store_info.decoration_name;
        chatMember.type = "3";
        chatMember.m_user_id = userId;

        ChatManager.getInstance(this).init().MemberChatToStore(chatMember, mGoodsDeatilEntity);
    }


    /**
     * 商品详情数据
     */
    @Override
    public void goodsDetailData(GoodsDeatilEntity goodsDeatilEntity) {
        mGoodsDeatilEntity = goodsDeatilEntity;
        goodsDeatilFrag.setGoodsDetailData(goodsDeatilEntity);
        store_info = goodsDeatilEntity.store_info;
        //购物车角标数字
        String member_cart_count = isEmpty(goodsDeatilEntity.member_cart_count) ?
                "0" : goodsDeatilEntity.member_cart_count;
        num = Integer.parseInt(member_cart_count);
        if (num == 0) {
            rnview.setVisibility(View.GONE);
        } else {
            rnview.setVisibility(View.VISIBLE);
            rnview.setNumber(num);
            rnview.setTargetNumber(num);
        }
        if (store_info != null) {
            store_id = store_info.store_id;
        }
        mShareInfoParam = goodsDetailPresenter.getShareInfoParam();
    }


    /**
     * 是否喜爱该商品
     *
     * @param is_fav
     */
    @Override
    public void isFavorite(String is_fav) {
        favId = is_fav;
        if (goodsDeatilFrag != null) {
            int item = goodsDeatilFrag.currentFirstItem;
            if (item > 0) {
                if (TextUtils.isEmpty(is_fav) || "0".equals(is_fav)) {
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_n);
                } else {
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_h);
                }
            } else {
                if (TextUtils.isEmpty(is_fav) || "0".equals(is_fav)) {
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_n);
                } else {
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_h);
                }
            }
        } else {
            if (TextUtils.isEmpty(is_fav) || "0".equals(is_fav)) {
                miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_n);
            } else {
                miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_h);
            }
        }

    }

    /**
     * 添加购物车
     *
     * @param msg
     */
    @Override
    public void addCart(String msg) {
        addCartAnim();
    }

    /**
     * 足迹列表
     *
     * @param footprintEntity
     */
    @Override
    public void footprintList(FootprintEntity footprintEntity) {
        mFootprintEntity = footprintEntity;
        if (footprintDialog == null) {
            footprintDialog = new FootprintDialog(this, mFootprintEntity);
        }
        footprintDialog.show();
    }

    /**
     * 刷新优惠券状态
     *
     * @param voucher
     */
    @Override
    public void refreshVoucherState(GoodsDeatilEntity.Voucher voucher) {
        if (goodsDeatilFrag != null) {
            goodsDeatilFrag.refreshVoucherState(voucher);
        }
    }

    /**
     * 商品是否下架
     *
     * @param status
     */
    @Override
    public void goodsOffShelf(String status) {
        if ("0".equals(status)) {//下架
            visible(mtv_off_shelf, mtv_want);
            gone(mtv_add_car, mtv_buy_immediately);
            /*mtv_add_car.setBackgroundColor(getColorResouce(R.color.value_FDCD22));
            mtv_add_car.setTextColor(getColorResouce(R.color.value_CDA101));
            mtv_add_car.setEnabled(false);
            mtv_buy_immediately.setBackgroundColor(getColorResouce(R.color.value_CACACA));
            mtv_buy_immediately.setTextColor(getColorResouce(R.color.value_A0A0A0));
            mtv_buy_immediately.setEnabled(false);*/
        } else {
            gone(mtv_off_shelf, mtv_want);
            visible(mtv_add_car, mtv_buy_immediately);
            mtv_add_car.setBackgroundColor(getColorResouce(R.color.my_black_one));
            mtv_add_car.setTextColor(getColorResouce(R.color.white));
            mtv_add_car.setEnabled(true);
            mtv_buy_immediately.setBackgroundColor(getColorResouce(R.color.pink_color));
            mtv_buy_immediately.setTextColor(getColorResouce(R.color.white));
            mtv_buy_immediately.setEnabled(true);
        }
    }

    /**
     * 天天特惠活动状态
     */
    @Override
    public void activityState(String status, String remindStatus) {
        //1：活动进行中   0：活动未开始
        if ("0".equals(status)) {
            //1：已设置提醒  0：未设置提醒
            if ("0".equals(remindStatus)) {
                mtv_buy_immediately.setText(getStringResouce(R.string.day_setting_remind));
            } else {
                mtv_buy_immediately.setText(getStringResouce(R.string.day_quxiaotixing));
            }
            mtv_buy_immediately.setBackgroundColor(getColorResouce(R.color.value_2096F2));
        } else {
            mtv_buy_immediately.setText(getStringResouce(R.string.now_buy));
            mtv_buy_immediately.setBackgroundColor(getColorResouce(R.color.pink_color));
        }
        mtv_buy_immediately.setTextColor(getColorResouce(R.color.white));
    }

    /**
     * 专题活动
     */
    @Override
    public void specailAct() {
        mtv_add_car.setBackgroundColor(getColorResouce(R.color.my_black_one));
        mtv_buy_immediately.setBackgroundColor(getColorResouce(R.color.value_FB0236));
    }

    /**
     * 你可能还想买
     *
     * @param adapter
     */
    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }

    @Override
    public void setCommentAdapter(CommentAdapter adapter) {
        if (commentFrag != null) {
            commentFrag.setCommentAdapter(adapter);
        }
    }

    /*
    显示足迹列表
    */
    public void showFootprintList() {
        if (mFootprintEntity == null) {
            goodsDetailPresenter.footprint();
        } else {
            if (footprintDialog == null) {
                footprintDialog = new FootprintDialog(this, mFootprintEntity);
            }
            footprintDialog.show();
        }
    }

    /**
     * 请求关注店铺
     */
    public void followStore() {
        goodsDetailPresenter.followStore(store_id);
    }

    /**
     * 请求取消关注店铺
     */
    public void delFollowStore() {
        goodsDetailPresenter.delFollowStore(store_id);
    }

    @Override
    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_add_car:
                if (!Common.isAlreadyLogin()) {
                    Common.goGoGo(this, "login");
                    return;
                }
                isAddcart = true;
                /*if (goodsCount == 0){//此流程：如果选过商品属性，不需要勾选
                    goodsDeatilFrag.showParamDialog();
                }else {
                    rnview.setVisibility(View.VISIBLE);
                    goodsDetailPresenter.addCart(goodsId,sku.id,String.valueOf(goodsCount));
                }*/
                //需求更改：每次加入购物车都需要选择属性
                goodsDeatilFrag.showParamDialog();
                break;
            case R.id.miv_more:
                moreAnim();
                break;

            case R.id.miv_close_share:
                moreHideAnim();
                break;

            case R.id.mll_goods:
                if (mll_item.getAlpha() <= 0.2f) return;
                listTop();
                break;

            case R.id.mll_detail:
                if (mll_item.getAlpha() <= 0.2f) return;
                setToolbar();
                setTabBarStatue(DETAIL_ID);
                goodsDeatilFrag.setScrollPosition(2, toolbarHeight);
                break;

            case R.id.mll_comment:
                if (mll_item.getAlpha() <= 0.2f) return;
                setToolbar();
                setTabBarStatue(COMMENT_ID);
                goodsDeatilFrag.setScrollPosition(1, toolbarHeight);
                break;

            case R.id.mll_chat:
                if (store_info != null) goodsDetailPresenter.getUserId(store_info.store_id);
                break;

            case R.id.miv_close:
                backOrder();
                break;

            case R.id.mtv_buy_immediately:
                if (!Common.isAlreadyLogin()) {
                    Common.goGoGo(this, "login");
                    return;
                }
                String buyText = mtv_buy_immediately.getText().toString();
                if (getStringResouce(R.string.now_buy).equals(buyText)||getStringResouce(R.string.goods_tuangoupifa).equals(buyText)) {//立刻购买或者团购批发
                    isNowBuy = true;
                    goodsDeatilFrag.showParamDialog();
                    /*if (goodsCount == 0){
                        goodsDeatilFrag.showParamDialog();
                    }else {
                        ConfirmOrderAct.startAct(this,goodsId,String
                        .valueOf(goodsCount),sku==null?"":sku.id);
                    }*/
                } else {
                    //设置提醒
                    if (getStringResouce(R.string.day_setting_remind).equals(buyText)) {//设置提醒
                        if (goodsDetailPresenter != null) goodsDetailPresenter.settingRemind();
                    } else {//取消提醒
                        if (goodsDetailPresenter != null) goodsDetailPresenter.cancleRemind();
                    }
                }
                break;

            case R.id.miv_is_fav:
                if (TextUtils.isEmpty(favId) || "0".equals(favId)) {
                    goodsDetailPresenter.goodsFavAdd(goodsId);
                } else {
                    goodsDetailPresenter.goodsFavRemove(favId);
                }
                break;

            case R.id.mllayout_car:
                MainActivity.startAct(this, "shoppingcar");
                break;
            case R.id.mtv_want:
                if (goodsDetailPresenter != null)
                    goodsDetailPresenter.goodsWant();
                break;
        }
    }

    public void listTop() {
        setTabBarStatue(GOODS_ID);
        mll_item.setAlpha(0);
        immersionBar.statusBarAlpha(0f).addTag(GoodsDetailAct.class.getName()).init();
        goodsDeatilFrag.setScrollPosition(0, toolbarHeight);
    }

    private void backOrder() {
        BaseFragment baseFragment = fragments.get(FRAG_COMMENT);
        if (baseFragment != null) {
            boolean visible = baseFragment.isVisible();
            if (visible) {
                setTabBarStatue(CURRENT_ID);
                goodsFrag();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backOrder();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setTabBarStatue(int statue) {
        //statue == 0 商品
        //statue == 1 评价
        //statue == 2 详情
        CURRENT_ID = statue;
        mtv_goods.setTextColor(statue == 0 ? getResources().getColor(R.color.pink_color)
                : getResources().getColor(R.color.new_text));
        view_goods.setVisibility(statue == 0 ? View.VISIBLE : View.INVISIBLE);

        mtv_comment.setTextColor(statue == 1 ? getResources().getColor(R.color.pink_color)
                : getResources().getColor(R.color.new_text));
        view_comment.setVisibility(statue == 1 ? View.VISIBLE : View.INVISIBLE);

        mtv_detail.setTextColor(statue == 2 ? getResources().getColor(R.color.pink_color)
                : getResources().getColor(R.color.new_text));
        view_detail.setVisibility(statue == 2 ? View.VISIBLE : View.INVISIBLE);

    }

    public void moreHideAnim() {
        immersionBar.getTag(GoodsDetailAct.class.getName()).init();
        gone(mll_share);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1);

        animation.setDuration(250);
        mll_share.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                quickAction();
                if (goodsDetailPresenter != null)
                    goodsDetailPresenter.mayBeBuyGoods();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 显示分享框
     */
    public void moreAnim() {
        float alpha = mll_item.getAlpha();
        if (alpha < 1 && alpha > 0) return;//导航栏没有全部显示的情况下，显示分享框会有透明条
        immersionBar.statusBarColor(R.color.white).init();
        visible(mll_share);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                -1, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(250);
        mll_share.setAnimation(animation);
        mStatusBarAlpha = immersionBar.getBarParams().statusBarAlpha;
    }

    private void addCartAnim() {
        if (isStopAnimation) {
            return;
        }
        isStopAnimation = true;
        myImageView = new MyImageView(this);
        myImageView.setWHProportion(50, 50);
        myImageView.setImageResource(R.mipmap.icon_login_logo);
        rl_rootview.addView(myImageView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myImageView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.bottomMargin = TransformUtil.dip2px(this, 6);
        int[] position = new int[2];
        mtv_add_car.getLocationInWindow(position);
        int measuredWidth = mtv_add_car.getMeasuredWidth();
        layoutParams.leftMargin = position[0] + measuredWidth / 2 - TransformUtil.dip2px(this, 25) / 2;
        myImageView.setLayoutParams(layoutParams);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(100);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                parabolaAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        myImageView.setAnimation(alphaAnimation);
    }


    private void parabolaAnimation() {
        int[] startPoint = new int[2];
        myImageView.getLocationInWindow(startPoint);

        int[] endPoint = new int[2];
        rnview.getLocationInWindow(endPoint);

        // 开始绘制贝塞尔曲线
        Path path = new Path();
        // 移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startPoint[0], startPoint[1] - myImageView.getMeasuredHeight());
        // 使用二阶贝塞尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startPoint[0] - endPoint[0]) / 2 + endPoint[0], startPoint[1] - 250,
                endPoint[0] - rnview.getMeasuredHeight() / 4,
                endPoint[1] - myImageView.getMeasuredHeight() - rnview.getMeasuredHeight() / 2);

        mPathMeasure = new PathMeasure(path, false);
        // 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofFloat("parabola", 0, mPathMeasure.getLength());

        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofFloat("scale", 1.0f, 0.5f);

        ValueAnimator valueAnimator1 = ValueAnimator.ofPropertyValuesHolder(propertyValuesHolder, propertyValuesHolder1);
        valueAnimator1.setDuration(600);

        // 匀速线性插值器
        valueAnimator1.setInterpolator(new LinearInterpolator());
        valueAnimator1.addUpdateListener((animation) -> {
            if (myImageView == null) return;
            // 当插值计算进行时，获取中间的每个值，
            // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
            float value = (Float) animation.getAnimatedValue("parabola");
            // 获取当前点坐标封装到mCurrentPosition
            // boolean getPosTan(float distance, float[] pos, float[] tan) ：
            // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距离的坐标点和切线，
            // pos会自动填充上坐标，这个方法很重要。
            // mCurrentPosition此时就是中间距离点的坐标值
            mPathMeasure.getPosTan(value, mCurrentPosition, null);
            // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
            myImageView.setX(mCurrentPosition[0]);
            myImageView.setY(mCurrentPosition[1]);
            float scale = (float) animation.getAnimatedValue("scale");
            myImageView.setScaleX(scale);
            myImageView.setScaleY(scale);
        });

        // 开始执行动画
        valueAnimator1.start();
        valueAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (rl_rootview != null) rl_rootview.removeView(myImageView);
                myImageView = null;
                isStopAnimation = false;
                numAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void numAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.2f, 0.9f, 1.0f);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(600);
        valueAnimator.start();
        valueAnimator.addUpdateListener((animation) -> {
            float scale = (float) animation.getAnimatedValue();
            if (rnview != null) {
                rnview.setScaleX(scale);
                rnview.setScaleY(scale);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                num += goodsCount;
                if (rnview != null) {
                    rnview.setVisibility(View.VISIBLE);
                    rnview.setNumber(num - goodsCount);
                    rnview.setTargetNumber(num);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myImageView != null) {
            myImageView.clearAnimation();
        }

        if (rnview != null) {
            rnview.clearAnimation();
        }
        mStatusBarAlpha = immersionBar.getBarParams().statusBarAlpha;
    }

    /**
     * 选择商品信息
     *
     * @param sku
     * @param count
     */
    public void selectGoodsInfo(GoodsDeatilEntity.Sku sku, int count) {
        //this.sku = sku;
        goodsCount = count;
        //LogUtil.zhLogW("=goodsCount=========="+goodsCount);
        if (isAddcart) {
            isAddcart = false;
            rnview.setVisibility(View.VISIBLE);
            goodsDetailPresenter.addCart(goodsId, sku == null ? "" : sku.id, String.valueOf(goodsCount));
        }

        if (isNowBuy) {
            isNowBuy = false;
            ConfirmOrderAct.startAct(this, goodsId, String.valueOf(goodsCount), sku == null ? "" : sku.id);
        }
    }

    @OnClick(R.id.mllayout_store)
    public void jumpStore() {
        StoreAct.startAct(this, store_id);
    }

    public void getCouchers(String id) {
        if (goodsDetailPresenter != null)
            goodsDetailPresenter.getVoucher(id);
    }

    public void refreshDetail() {
        if (goodsDetailPresenter != null) {
            goodsDetailPresenter.refreshDetail();
        }
    }

    @OnClick(R.id.mtv_firstPage)
    public void firstPage() {
        currentQuickAction = 1;
        moreHideAnim();
    }

    @OnClick(R.id.mtv_search)
    public void search() {
        currentQuickAction = 2;
        moreHideAnim();
    }

    @OnClick(R.id.mtv_message)
    public void message() {
        currentQuickAction = 3;
        moreHideAnim();
    }

    @OnClick(R.id.mtv_feedback)
    public void feedback() {
        currentQuickAction = 4;
        moreHideAnim();
    }

    @OnClick(R.id.mtv_help)
    public void help() {
        currentQuickAction = 5;
        moreHideAnim();
    }

    @OnClick(R.id.mtv_weixin_share)
    public void weChatShare() {
        if (!Common.isAlreadyLogin()) {
            sharePrompt();
            return;
        }
        currentQuickAction = 6;
        moreHideAnim();
    }

    @OnClick(R.id.mtv_picText_share)
    public void picTextShare() {
        if (!Common.isAlreadyLogin()) {
            sharePrompt();
            return;
        }
        if (goodsDetailPresenter != null) {
            quick_actions.shareInfo(mShareInfoParam);
            goodsDetailPresenter.copyText(false);
            quick_actions.saveshareGoodsPic();
        }
    }

    @OnClick(R.id.mtv_copyLink_share)
    public void copyLinkShare() {
        if (!Common.isAlreadyLogin()) {
            sharePrompt();
            return;
        }
        if (goodsDetailPresenter != null) {
            goodsDetailPresenter.copyText(true);
        }
        moreHideAnim();
    }

    public void sharePrompt() {
        Common.goGoGo(this, "login");
        moreHideAnim();
        /*final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setTvSureColor(R.color.white);
        promptDialog.setTvSureBg(R.color.pink_color);
        promptDialog.setSureAndCancleListener("请先登录顺联APP，参与分享呦~", "确定",
                (view) -> {
                    Common.goGoGo(this,"login");
                    moreHideAnim();
                    promptDialog.dismiss();
                }, "取消", (view) -> promptDialog.dismiss()).show();*/
    }

    private void quickAction() {
        switch (currentQuickAction) {
            case 1:
                Common.goGoGo(this, "");
                break;
            case 2:
                Common.goGoGo(this, "search");
                break;
            case 3:
                Common.goGoGo(this, "message");
                break;
            case 4:
                Common.goGoGo(this, "feedback", goodsId);
                break;
            case 5:
                Common.goGoGo(this, "help");
                break;
            case 6://分享到微信
                if (goodsDetailPresenter != null) {
                    WXEntryActivity.startAct(this, "shareFriend",
                            goodsDetailPresenter.getShareInfoParam());
                }
                break;
        }
        currentQuickAction = -1;
    }

    /**
     * 优品
     */
    @Override
    public void superiorProduct() {
        if (mtv_buy_immediately != null) {
            mtv_buy_immediately.setTextColor(Color.parseColor("#111111"));
            mtv_buy_immediately.setBackgroundColor(Color.parseColor("#F3C262"));
        }
        if (mtv_add_car != null)
            mtv_add_car.setBackgroundColor(Color.parseColor("#111111"));
    }

    @Override
    public void groupBuy() {
        mtv_add_car.setText(getStringResouce(R.string.goods_jiarujinhuo));
        mtv_buy_immediately.setText(getStringResouce(R.string.goods_tuangoupifa));
    }

    /**
     * 库存不足
     */
    @Override
    public void stockDeficiency(String stock) {
        visible(mtv_want);
        gone(mtv_add_car, mtv_buy_immediately);
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        if (goodsDetailPresenter != null) {
            goodsDetailPresenter.detachView();
            goodsDetailPresenter = null;
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginRefresh(DefMessageEvent event) {
        if (event.loginSuccess && goodsDetailPresenter != null) {
            goodsDetailPresenter.getShareInfo(goodsDetailPresenter.goods, goodsId);
        }
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        mShareInfoParam.userName = baseEntity.data.userName;
        mShareInfoParam.userAvatar = baseEntity.data.userAvatar;
        mShareInfoParam.shareLink = baseEntity.data.shareLink;
        mShareInfoParam.desc = baseEntity.data.desc;
        if (goodsDetailPresenter != null){
            goodsDetailPresenter.setShareInfoParam(mShareInfoParam);
        }
    }
}
