package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.NewCouponListAdapter;
import com.shunlian.app.adapter.NewCouponsAdapter;
import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PGetCoupon;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IGetCoupon;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/7.
 */

public class NewGetCouponAct extends BaseActivity implements View.OnClickListener, IGetCoupon, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_remen)
    MyTextView mtv_remen;

    @BindView(R.id.view_remen)
    View view_remen;

    @BindView(R.id.view_zuixin)
    View view_zuixin;

    @BindView(R.id.mllayout_remen)
    MyLinearLayout mllayout_remen;

    @BindView(R.id.mllayout_zuixin)
    MyLinearLayout mllayout_zuixin;

    @BindView(R.id.mtv_zuixin)
    MyTextView mtv_zuixin;

    @BindView(R.id.rv_pingtai)
    RecyclerView rv_pingtai;

    @BindView(R.id.rv_dianpu)
    RecyclerView rv_dianpu;

    @BindView(R.id.msv_out)
    MyScrollView msv_out;

    @BindView(R.id.miv_search)
    MyImageView miv_search;

    @BindView(R.id.miv_logo)
    MyImageView miv_logo;

    @BindView(R.id.miv_entry)
    MyImageView miv_entry;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.ntv_pintai)
    NewTextView ntv_pintai;

    @BindView(R.id.ntv_dianpu)
    NewTextView ntv_dianpu;

    private MessageCountManager messageCountManager;

    private PGetCoupon pGetCoupon;
    private NewCouponsAdapter couponsAdapter;
    private NewCouponListAdapter couponAdapter;
    private GridLayoutManager gridLayoutManager;
    private String type = "All";
    private List<VouchercenterplEntity.MData> mDatas;
    private boolean isHide = false;
    private AdEntity.Suspension.Link link;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NewGetCouponAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_get_coupon_new;
    }

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.channel();
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(baseAct);
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
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_search.setOnClickListener(this);
        mllayout_remen.setOnClickListener(this);
        mllayout_zuixin.setOnClickListener(this);
        miv_entry.setOnClickListener(this);
//        rv_dianpu.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (linearLayoutManager != null) {
//                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
//                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
//                        if (pGetCoupon != null) {
//                            pGetCoupon.refreshBaby(type, "");
//                        }
//                    }
//                }
//            }
//        });
        msv_out.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom) {
                    if (pGetCoupon != null) {
                        pGetCoupon.refreshBaby(type, "");
                    }
                }
                int dy=y-oldy;
                if (dy != 0) {
                    int value = TransformUtil.dip2px(baseAct, 80);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(value, value);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    if (dy > 0) {
                        layoutParams.setMargins(0, 0, -value / 2, value);
                        isHide = true;
                    } else {
                        layoutParams.setMargins(0, 0, 0, value);
                        isHide = false;
                    }
                    miv_entry.setLayoutParams(layoutParams);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.miv_entry:
                if (isHide) {
                    int value = TransformUtil.dip2px(baseAct, 80);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(value, value);
                    layoutParams.setMargins(0, 0, 0, value);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    miv_entry.setLayoutParams(layoutParams);
                    isHide = false;
                } else if (link != null) {
                    Common.goGoGo(baseAct, link.type, link.item_id);
                }
                break;
            case R.id.miv_search:
                SearchCouponAct.startAct(baseAct);
                break;
            case R.id.mllayout_zuixin:
                if (rv_dianpu.getScrollState() == 0) {
                    type = "NEW";
                    mtv_zuixin.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    mtv_remen.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                    view_zuixin.setVisibility(View.VISIBLE);
                    view_remen.setVisibility(View.INVISIBLE);
                    pGetCoupon.resetBaby(type, "");
                    ntv_pintai.setVisibility(View.GONE);
                    rv_pingtai.setVisibility(View.GONE);
                }
                break;
            case R.id.mllayout_remen:
                if (rv_dianpu.getScrollState() == 0) {
                    type = "All";
                    mtv_remen.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    mtv_zuixin.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                    view_remen.setVisibility(View.VISIBLE);
                    view_zuixin.setVisibility(View.INVISIBLE);
                    rv_pingtai.setVisibility(View.VISIBLE);
                    pGetCoupon.resetBaby(type, "");
                }
                break;
        }
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.pink_color);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
        pGetCoupon = new PGetCoupon(this, this);
        pGetCoupon.getPing();
        pGetCoupon.resetBaby(type, "");
        pGetCoupon.adpush();

        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setpingData(VouchercenterplEntity vouchercenterplEntity) {
        if (!isEmpty(vouchercenterplEntity.banner)) {
            int width;
            int height;
            int screenWidth = Common.getScreenWidth(baseAct);
            try {
                width = Integer.parseInt(vouchercenterplEntity.width);
                height = Integer.parseInt(vouchercenterplEntity.height);
            } catch (Exception e) {
                width = 750;
                height = 200;
                LogUtil.augusLogW(vouchercenterplEntity.width + "---h：" + vouchercenterplEntity.height);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, ((screenWidth * height) / width));
            miv_logo.setLayoutParams(params);
            GlideUtils.getInstance().loadImageChang(baseAct, miv_logo, vouchercenterplEntity.banner);
        }
        if (isEmpty(vouchercenterplEntity.seller_voucher)) {
            ntv_pintai.setVisibility(View.GONE);
            return;
        }
        ntv_pintai.setVisibility(View.VISIBLE);
        mDatas = new ArrayList<>();
        mDatas.addAll(vouchercenterplEntity.seller_voucher);
        rv_pingtai.setNestedScrollingEnabled(false);
        couponAdapter = new NewCouponListAdapter(this, mDatas, pGetCoupon);
        rv_pingtai.setAdapter(couponAdapter);
        rv_pingtai.setNestedScrollingEnabled(false);
        rv_pingtai.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rv_pingtai.addItemDecoration(new MVerticalItemDecoration(this,10,0,0));
    }

    @Override
    public void setAd(AdEntity adEntity) {
        if (adEntity != null && adEntity.suspension != null && !isEmpty(adEntity.suspension.image)) {
            miv_entry.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImageZheng(this, miv_entry, adEntity.suspension.image);
        }else {
            miv_entry.setVisibility(View.GONE);
        }
    }

    @Override
    public void setdianData(List<VouchercenterplEntity.MData> mData, String page, String total) {
        if (isEmpty(mData)) {
            ntv_dianpu.setVisibility(View.GONE);
        } else {
            ntv_dianpu.setVisibility(View.VISIBLE);
        }
        if (couponsAdapter == null) {
            couponsAdapter = new NewCouponsAdapter(this, true, mData, pGetCoupon);
            gridLayoutManager = new GridLayoutManager(baseAct, 1, LinearLayoutManager.VERTICAL, false);
            rv_dianpu.setLayoutManager(gridLayoutManager);
            rv_dianpu.setAdapter(couponsAdapter);
            rv_dianpu.setNestedScrollingEnabled(false);
        } else {
            couponsAdapter.notifyDataSetChanged();
        }
        couponsAdapter.setPageLoading(Integer.parseInt(page), Integer.parseInt(total));
    }


    @Override
    public void OnLoadFail() {

    }

    @Override
    public void getCouponCallBack(boolean isCommon, int position, String isGet) {
        if ("1".equals(isGet)) {
            if (isCommon) {
                mDatas.get(position).if_get = "1";
                couponAdapter.notifyItemChanged(position);
            } else {
                pGetCoupon.mDatas.get(position).if_get = "1";
                couponsAdapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void getCouponCallBacks(int position, String isGet, int positions) {
        if ("1".equals(isGet)) {
            pGetCoupon.mDatas.get(position).goods_data.get(positions).if_get = "1";
            couponsAdapter.notifyItemChanged(position);
        }
    }

}
