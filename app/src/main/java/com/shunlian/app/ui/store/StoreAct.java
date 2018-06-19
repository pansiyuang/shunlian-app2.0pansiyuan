package com.shunlian.app.ui.store;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.StoreBabyAdapter;
import com.shunlian.app.adapter.StoreDiscountMenuAdapter;
import com.shunlian.app.adapter.StoreDiscountOneAdapter;
import com.shunlian.app.adapter.StoreDiscountTwoAdapter;
import com.shunlian.app.adapter.StoreFirstAdapter;
import com.shunlian.app.adapter.StoreNewAdapter;
import com.shunlian.app.adapter.StoreVoucherAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListOneEntity;
import com.shunlian.app.bean.StorePromotionGoodsListTwoEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.StorePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.ComboDetailAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.StoreView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/7.
 */

public class StoreAct extends BaseActivity implements View.OnClickListener, StoreView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.store_ctLayout)
    CollapsingToolbarLayout store_ctLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mrlayout_stores)
    MyRelativeLayout mrlayout_stores;

    @BindView(R.id.store_abLayout)
    AppBarLayout store_abLayout;

    @BindView(R.id.miv_store)
    MyImageView miv_store;

    @BindView(R.id.mtv_store)
    MyTextView mtv_store;

    @BindView(R.id.line_first)
    View line_first;

    @BindView(R.id.mtv_baby)
    MyTextView mtv_baby;

    @BindView(R.id.mrlayout_baby)
    MyRelativeLayout mrlayout_baby;

    @BindView(R.id.mrlayout_discount)
    MyRelativeLayout mrlayout_discount;

    @BindView(R.id.mtv_discount)
    MyTextView mtv_discount;

    @BindView(R.id.line_baby)
    View line_baby;

    @BindView(R.id.line_discount)
    View line_discount;

    @BindView(R.id.mrlayout_new)
    MyRelativeLayout mrlayout_new;

    @BindView(R.id.mtv_new)
    MyTextView mtv_new;

    @BindView(R.id.line_new)
    View line_new;

    @BindView(R.id.rv_firstVouch)
    RecyclerView rv_firstVouch;

    @BindView(R.id.mrlayout_store)
    MyRelativeLayout mrlayout_store;

    @BindView(R.id.mllayout_first)
    MyLinearLayout mllayout_first;

    @BindView(R.id.mllayout_baby)
    MyLinearLayout mllayout_baby;

    @BindView(R.id.mllayout_discount)
    MyLinearLayout mllayout_discount;

    @BindView(R.id.rv_new)
    RecyclerView rv_new;

    @BindView(R.id.rv_baby)
    RecyclerView rv_baby;

    @BindView(R.id.rv_discount)
    RecyclerView rv_discount;

    @BindView(R.id.rv_discounts)
    RecyclerView rv_discounts;

    @BindView(R.id.rv_discountMenu)
    RecyclerView rv_discountMenu;

    @BindView(R.id.rv_first)
    RecyclerView rv_first;
    @BindView(R.id.miv_storeLogo)
    MyImageView miv_storeLogo;

    @BindView(R.id.mtv_attention)
    public MyTextView mtv_attention;

    @BindView(R.id.mtv_storeName)
    MyTextView mtv_storeName;

    @BindView(R.id.miv_star)
    MyImageView miv_star;

    @BindView(R.id.mtv_number)
    public MyTextView mtv_number;

    @BindView(R.id.mtv_babyNum)
    MyTextView mtv_babyNum;

    @BindView(R.id.mtv_discountNum)
    MyTextView mtv_discountNum;

    @BindView(R.id.mtv_newNum)
    MyTextView mtv_newNum;

    @BindView(R.id.mrlayout_bg)
    MyRelativeLayout mrlayout_bg;

    @BindView(R.id.tv_zonghe)
    MyTextView tv_zonghe;

    @BindView(R.id.tv_xiaoliang)
    MyTextView tv_xiaoliang;

    @BindView(R.id.tv_shangxin)
    MyTextView tv_shangxin;

    @BindView(R.id.tv_price)
    MyTextView tv_price;

    @BindView(R.id.iv_price)
    MyImageView iv_price;

    @BindView(R.id.mrLayout_price)
    MyRelativeLayout mrLayout_price;

    @BindView(R.id.mrlayout_jianjie)
    MyRelativeLayout mrlayout_jianjie;

    @BindView(R.id.mrlayout_sort)
    MyRelativeLayout mrlayout_sort;

    @BindView(R.id.mrlayout_sorts)
    MyRelativeLayout mrlayout_sorts;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.mllayout_search)
    MyLinearLayout mllayout_search;
    @BindView(R.id.mll_chat)
    MyLinearLayout mll_chat;
    private MessageCountManager messageCountManager;
    private StorePresenter storePresenter;
    private boolean isPriceUp, initBaby, initDiscount, initNew, initFirst;
    private String storeId = "26", star,logo;
    private StoreFirstAdapter storeFirstAdapter;
    private StoreBabyAdapter storeBabyAdapter;
    private StoreNewAdapter storeNewAdapter;
    private StoreDiscountTwoAdapter storeDiscountTwoAdapter;
    private StoreDiscountOneAdapter storeDiscountOneAdapter;
    private StoreDiscountMenuAdapter storeDiscountMenuAdapter;
    private StoreVoucherAdapter storeVoucherAdapter;
    private GridLayoutManager babyManager, discountManager;
    public boolean isFocus;
    private ShareInfoParam shareInfoParam;
    public int focusNum=0;
    private List<GoodsDeatilEntity.Voucher> vouchers;

    public static void startAct(Context context, String storeId) {
        Intent intent = new Intent(context, StoreAct.class);
        intent.putExtra("storeId", storeId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.shop();
        quick_actions.shareInfo(shareInfoParam);
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(getBaseContext());
            if (messageCountManager.isLoad()) {
                String s = messageCountManager.setTextCount(tv_msg_count);
                if (quick_actions != null)
                    quick_actions.setMessageCount(s);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_store;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_attention.setOnClickListener(this);
        mrlayout_stores.setOnClickListener(this);
        mrlayout_baby.setOnClickListener(this);
        mrlayout_discount.setOnClickListener(this);
        mllayout_search.setOnClickListener(this);
        mrlayout_new.setOnClickListener(this);
        tv_zonghe.setOnClickListener(this);
        tv_xiaoliang.setOnClickListener(this);
        tv_shangxin.setOnClickListener(this);
        mrLayout_price.setOnClickListener(this);
        mrlayout_jianjie.setOnClickListener(this);
        mrlayout_sort.setOnClickListener(this);
        mrlayout_sorts.setOnClickListener(this);
        mll_chat.setOnClickListener(this);
        store_abLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                float alpha = -((float) verticalOffset) / ((float) (store_ctLayout.getHeight() - toolbar.getHeight()));
                mrlayout_store.setAlpha(1 - alpha);
                miv_store.setAlpha(1 - alpha);
                mtv_babyNum.setAlpha(1 - alpha);
                mtv_discountNum.setAlpha(1 - alpha);
                mtv_newNum.setAlpha(1 - alpha);

                if (verticalOffset <= -(store_ctLayout.getHeight() - toolbar.getHeight())) {
                    miv_store.setVisibility(View.GONE);
                    mtv_babyNum.setVisibility(View.GONE);
                    mtv_discountNum.setVisibility(View.GONE);
                    mtv_newNum.setVisibility(View.GONE);
                } else {
                    miv_store.setVisibility(View.VISIBLE);
                    mtv_babyNum.setVisibility(View.VISIBLE);
                    mtv_discountNum.setVisibility(View.VISIBLE);
                    mtv_newNum.setVisibility(View.VISIBLE);
                }
            }
        });
        rv_baby.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (babyManager != null) {
                    int lastPosition = babyManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == babyManager.getItemCount()) {
                        if (storePresenter != null) {
                            storePresenter.refreshBaby();
                        }
                    }
                }
            }
        });
        rv_discount.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (discountManager != null) {
                    int lastPosition = discountManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == discountManager.getItemCount()) {
                        if (storePresenter != null) {
                            storePresenter.refreshDiscountOne();
                        }
                    }
                }
            }
        });
    }


    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("storeId"))) {
            storeId = getIntent().getStringExtra("storeId");
        }
        storePresenter = new StorePresenter(this, this, storeId);
        if (getIntent().getBooleanExtra("discount", false)) {
            discountClick();
        } else {
            firstClick();
        }
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);
    }

    public void firstClick() {
        miv_store.setImageResource(R.mipmap.icon_shop_shop_h);
        mtv_store.setTextColor(getResources().getColor(R.color.pink_color));
        mtv_baby.setTextColor(getResources().getColor(R.color.my_gray_three));
        mtv_babyNum.setTextColor(getResources().getColor(R.color.my_gray_three));
        mtv_discount.setTextColor(getResources().getColor(R.color.my_gray_three));
        mtv_discountNum.setTextColor(getResources().getColor(R.color.my_gray_three));
        mtv_new.setTextColor(getResources().getColor(R.color.my_gray_three));
        mtv_newNum.setTextColor(getResources().getColor(R.color.my_gray_three));
        line_first.setVisibility(View.VISIBLE);
        line_baby.setVisibility(View.GONE);
        line_discount.setVisibility(View.GONE);
        line_new.setVisibility(View.GONE);
        mllayout_first.setVisibility(View.VISIBLE);
        mllayout_baby.setVisibility(View.GONE);
        mllayout_discount.setVisibility(View.GONE);
        rv_new.setVisibility(View.GONE);
        if (!initFirst) {
            storePresenter.initFirst(storeId);
            initFirst = true;
        }
    }

    public void discountClick() {
        miv_store.setImageResource(R.mipmap.icon_shop_shop_n);
        mtv_store.setTextColor(getResources().getColor(R.color.my_gray_three));
        mtv_baby.setTextColor(getResources().getColor(R.color.my_gray_three));
        mtv_babyNum.setTextColor(getResources().getColor(R.color.my_gray_three));
        mtv_discount.setTextColor(getResources().getColor(R.color.pink_color));
        mtv_discountNum.setTextColor(getResources().getColor(R.color.pink_color));
        mtv_new.setTextColor(getResources().getColor(R.color.my_gray_three));
        mtv_newNum.setTextColor(getResources().getColor(R.color.my_gray_three));
        line_first.setVisibility(View.GONE);
        line_baby.setVisibility(View.GONE);
        line_discount.setVisibility(View.VISIBLE);
        line_new.setVisibility(View.GONE);
        mllayout_first.setVisibility(View.GONE);
        mllayout_baby.setVisibility(View.GONE);
        mllayout_discount.setVisibility(View.VISIBLE);
        rv_new.setVisibility(View.GONE);
        if (!initDiscount) {
            storePresenter.initDiscount(storeId);
            initDiscount = true;
        }
    }

    public void storeMenu(View v) {
        switch (v.getId()) {
            case R.id.mrlayout_stores:
                firstClick();
                break;
            case R.id.mrlayout_baby:
                miv_store.setImageResource(R.mipmap.icon_shop_shop_n);
                mtv_store.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_baby.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_babyNum.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_discount.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_discountNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_new.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_newNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                line_first.setVisibility(View.GONE);
                line_baby.setVisibility(View.VISIBLE);
                line_discount.setVisibility(View.GONE);
                line_new.setVisibility(View.GONE);
                mllayout_first.setVisibility(View.GONE);
                mllayout_baby.setVisibility(View.VISIBLE);
                mllayout_discount.setVisibility(View.GONE);
                rv_new.setVisibility(View.GONE);
                if (!initBaby) {
                    storePresenter.resetBaby("default");
                    initBaby = true;
                }
                break;
            case R.id.mrlayout_discount:
                discountClick();
                break;
            case R.id.mrlayout_new:
                miv_store.setImageResource(R.mipmap.icon_shop_shop_n);
                mtv_store.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_baby.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_babyNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_discount.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_discountNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_new.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_newNum.setTextColor(getResources().getColor(R.color.pink_color));
                line_first.setVisibility(View.GONE);
                line_baby.setVisibility(View.GONE);
                line_discount.setVisibility(View.GONE);
                line_new.setVisibility(View.VISIBLE);
                mllayout_first.setVisibility(View.GONE);
                mllayout_baby.setVisibility(View.GONE);
                mllayout_discount.setVisibility(View.GONE);
                rv_new.setVisibility(View.VISIBLE);
                if (!initNew) {
                    storePresenter.initNew(storeId);
                    initNew = true;
                }
                break;
        }
    }

    public void babyMenu(View v) {
        switch (v.getId()) {
            case R.id.tv_zonghe:
                tv_zonghe.setTextColor(getResources().getColor(R.color.pink_color));
                tv_xiaoliang.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_shangxin.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_price.setTextColor(getResources().getColor(R.color.my_gray_three));
                iv_price.setVisibility(View.GONE);
                storePresenter.resetBaby("default");
                break;
            case R.id.tv_xiaoliang:
                tv_zonghe.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_xiaoliang.setTextColor(getResources().getColor(R.color.pink_color));
                tv_shangxin.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_price.setTextColor(getResources().getColor(R.color.my_gray_three));
                iv_price.setVisibility(View.GONE);
                storePresenter.resetBaby("sales");
                break;
            case R.id.tv_shangxin:
                tv_zonghe.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_xiaoliang.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_shangxin.setTextColor(getResources().getColor(R.color.pink_color));
                tv_price.setTextColor(getResources().getColor(R.color.my_gray_three));
                iv_price.setVisibility(View.GONE);
                storePresenter.resetBaby("new");
                break;
            case R.id.mrLayout_price:
                tv_zonghe.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_xiaoliang.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_shangxin.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_price.setTextColor(getResources().getColor(R.color.pink_color));
                iv_price.setVisibility(View.VISIBLE);
                if (isPriceUp) {
                    iv_price.setImageResource(R.mipmap.icon_common_arrowdown);
                    storePresenter.resetBaby("pricedown");
                    isPriceUp = false;
                } else {
                    iv_price.setImageResource(R.mipmap.icon_common_arrowup);
                    storePresenter.resetBaby("priceup");
                    isPriceUp = true;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0&&resultCode==1){
            isFocus(data.getBooleanExtra("isFocus",false),data.getIntExtra("focusNum",0));
        }
    }

    @Override
    public void onClick(View v) {
        storeMenu(v);
        if (rv_baby.getScrollState() == 0) {
            babyMenu(v);
        }
        switch (v.getId()) {
            case R.id.mrlayout_jianjie:
                StoreIntroduceAct.startActForResult(this, storeId, star,logo, isFocus);
                break;
            case R.id.mllayout_search:
                StoreSearchAct.startAct(this,storeId,"","","");
                break;
            case R.id.mrlayout_sort:
                StoreSortAct.startAct(this, storeId,true);
                break;
            case R.id.mrlayout_sorts:
                StoreSortAct.startAct(this, storeId,true);
                break;
            case R.id.mtv_attention:
                if (isFocus) {
                    storePresenter.delFollowStore(storeId);
                } else {
                    storePresenter.followStore(storeId);
                }
                break;
            case R.id.mll_chat:
                storePresenter.getUserId(storeId);
                break;
        }
    }

    @Override
    public void storeFirst(List<StoreIndexEntity.Body> bodies) {
        if (storeFirstAdapter == null) {
            storeFirstAdapter = new StoreFirstAdapter(this, false, bodies);
            LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rv_first.setLayoutManager(firstManager);
            rv_first.setAdapter(storeFirstAdapter);
            rv_first.setNestedScrollingEnabled(false);
        } else {
            storeFirstAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void storeBaby(final List<StoreGoodsListEntity.MData> mDataList, int allPage, final int page) {
        if (storeBabyAdapter == null) {
            storeBabyAdapter = new StoreBabyAdapter(this, true, mDataList);
            babyManager = new GridLayoutManager(this, 2);
            rv_baby.setLayoutManager(babyManager);
            rv_baby.addItemDecoration(new GrideItemDecoration(0, 0, TransformUtil.dip2px(this, 5), TransformUtil.dip2px(this, 5), true));
            rv_baby.setAdapter(storeBabyAdapter);
            storeBabyAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    GoodsDetailAct.startAct(StoreAct.this, mDataList.get(position).id);
                }
            });
        } else {
            storeBabyAdapter.notifyDataSetChanged();
        }
        storeBabyAdapter.setPageLoading(page, allPage);
    }

    @Override
    public void storeDiscountOne(List<StorePromotionGoodsListOneEntity.MData> mDatas, int allPage, int page) {
        rv_discount.setVisibility(View.VISIBLE);
        rv_discounts.setVisibility(View.GONE);
        if (storeDiscountOneAdapter == null) {
            storeDiscountOneAdapter = new StoreDiscountOneAdapter(this, true, mDatas);
            discountManager = new GridLayoutManager(this, 1);
            rv_discount.setLayoutManager(discountManager);
            rv_discount.setAdapter(storeDiscountOneAdapter);
        } else {
            storeDiscountOneAdapter.notifyDataSetChanged();
        }
        storeDiscountOneAdapter.setPageLoading(page, allPage);
    }

    @Override
    public void storeNew(List<StoreNewGoodsListEntity.Data> datas) {
        if (storeNewAdapter == null) {
            storeNewAdapter = new StoreNewAdapter(this, false, datas);
            LinearLayoutManager newManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rv_new.setLayoutManager(newManager);
            rv_new.setAdapter(storeNewAdapter);
        } else {
            storeNewAdapter.notifyDataSetChanged();
        }
    }

    public void isFocus(boolean isFocus,int focusNum){
        if (!isFocus) {
            mtv_attention.setTextColor(getResources().getColor(R.color.white));
            mtv_attention.setText(getStringResouce(R.string.discover_follow));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_n);
            this.isFocus = false;
        } else {
            mtv_attention.setTextColor(getResources().getColor(R.color.pink_color));
            mtv_attention.setText(getStringResouce(R.string.discover_alear_follow));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_h);
            this.isFocus = true;
        }
        this.focusNum=focusNum;
        mtv_number.setText(focusNum + "人");
    }
    @Override
    public void storeHeader(StoreIndexEntity.Head head) {
        logo = head.decoration_logo;
        GlideUtils.getInstance().loadImage(this, miv_storeLogo, logo);
        GlideUtils.getInstance().loadBgImage(this, mrlayout_bg, head.decoration_banner);
        if ("false".equals(head.is_mark)) {
            mtv_attention.setTextColor(getResources().getColor(R.color.white));
            mtv_attention.setText(getStringResouce(R.string.discover_follow));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_n);
            isFocus = false;
        } else {
            mtv_attention.setTextColor(getResources().getColor(R.color.pink_color));
            mtv_attention.setText(getStringResouce(R.string.discover_alear_follow));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_h);
            isFocus = true;
        }
        star = head.star;
        mtv_storeName.setText(head.decoration_name);
        GlideUtils.getInstance().loadImage(this, miv_star, star);
        if ("false".equals(head.is_mark)){
            isFocus(false,!isEmpty(head.mark_count)?Integer.parseInt(head.mark_count):0);
        }else {
            isFocus(true,!isEmpty(head.mark_count)?Integer.parseInt(head.mark_count):0);
        }
        mtv_babyNum.setText(head.goods_count);
        mtv_discountNum.setText(head.promotion_count);
        mtv_newNum.setText(head.new_count);
        shareInfoParam = new ShareInfoParam();
        shareInfoParam.shareLink = head.share_url;
        shareInfoParam.shop_logo = head.decoration_logo;
        shareInfoParam.shop_name = head.decoration_name;
        shareInfoParam.shop_star = head.star;
        shareInfoParam.userName = head.nickname;
        shareInfoParam.userAvatar = head.avatar;
    }

    @Override
    public void storeDiscountMenu(List<StorePromotionGoodsListEntity.Lable> datas) {
        rv_discountMenu.setVisibility(View.VISIBLE);
        if (storeDiscountMenuAdapter == null) {
            storeDiscountMenuAdapter = new StoreDiscountMenuAdapter(this, false, datas);
            LinearLayoutManager newManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rv_discountMenu.setLayoutManager(newManager);
            rv_discountMenu.setAdapter(storeDiscountMenuAdapter);
            rv_discountMenu.setNestedScrollingEnabled(false);
            storeDiscountMenuAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (rv_discount.getScrollState() == 0) {
                        storeDiscountMenuAdapter.selectPosition = position;
                        storeDiscountMenuAdapter.notifyDataSetChanged();
                        if ("combo".equals(view.getTag(R.id.tag_store_discount_menu_type).toString())) {
                            storePresenter.initDiscountTwo(storeId, Integer.parseInt(view.getTag(R.id.tag_store_discount_menu_id).toString()), view.getTag(R.id.tag_store_discount_menu_type).toString());
                        } else {
                            storePresenter.resetDiscountOne(Integer.parseInt(view.getTag(R.id.tag_store_discount_menu_id).toString()), view.getTag(R.id.tag_store_discount_menu_type).toString());
                        }
                    }
                }
            });
        } else {
            storeDiscountMenuAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void storeVoucher( List<GoodsDeatilEntity.Voucher> voucherList) {
        if (!isEmpty(voucherList)) {
            rv_firstVouch.setVisibility(View.VISIBLE);
            vouchers=voucherList;
            if (storeVoucherAdapter == null) {
                storeVoucherAdapter = new StoreVoucherAdapter(this, false, vouchers);
                LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rv_firstVouch.setLayoutManager(firstManager);
                rv_firstVouch.setAdapter(storeVoucherAdapter);
                rv_firstVouch.setNestedScrollingEnabled(false);

                storeVoucherAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        storePresenter.getVoucher(vouchers.get(position).voucher_id);
                        storeVoucherAdapter.detailBottomCouponPosition = position;
                    }
                });
            } else {
                storeVoucherAdapter.notifyDataSetChanged();
            }
        } else {
            rv_firstVouch.setVisibility(View.GONE);
        }
    }

    @Override
    public void storeFocus() {
        if (isFocus) {
            mtv_attention.setTextColor(getResources().getColor(R.color.white));
            mtv_attention.setText(getStringResouce(R.string.discover_follow));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_n);
            focusNum--;
            isFocus = false;
        } else {
            mtv_attention.setText(getStringResouce(R.string.discover_alear_follow));
            mtv_attention.setTextColor(getResources().getColor(R.color.pink_color));
            mtv_attention.setBackgroundResource(R.mipmap.bg_shop_attention_h);
            focusNum++;
            isFocus = true;
        }
        mtv_number.setText(focusNum+ "人");
    }

    @Override
    public void storeDiscountTwo(List<StorePromotionGoodsListTwoEntity.Lists.Good> mDatas) {
        rv_discount.setVisibility(View.GONE);
        rv_discounts.setVisibility(View.VISIBLE);
        if (storeDiscountTwoAdapter == null) {
            storeDiscountTwoAdapter = new StoreDiscountTwoAdapter(this, false, mDatas);
            rv_discounts.setLayoutManager(new LinearLayoutManager(this));
            rv_discounts.setAdapter(storeDiscountTwoAdapter);
        } else {
            storeDiscountTwoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshVoucherState(GoodsDeatilEntity.Voucher voucher) {
        if (storeVoucherAdapter != null) {
            if (storeVoucherAdapter.detailBottomCouponPosition != -1) {
                vouchers.get(storeVoucherAdapter.detailBottomCouponPosition).is_get = voucher.is_get;
                storeVoucherAdapter.notifyItemChanged(storeVoucherAdapter.detailBottomCouponPosition);
            }
            storeVoucherAdapter.detailBottomCouponPosition = -1;
        }
    }

    @Override
    public void getUserId(String userId) {
        if (isEmpty(userId) || "0".equals(userId)) {
            Common.staticToast("该商家未开通客服");
            return;
        }
        ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();
        chatMember.shop_id = storeId;
        chatMember.m_user_id = userId;
        chatMember.type = "3";
        chatMember.nickname = mtv_storeName.getText().toString();

        ChatManager.getInstance(this).init().MemberChatToStore(chatMember);
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

}
