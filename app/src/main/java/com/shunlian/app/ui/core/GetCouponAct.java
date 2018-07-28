package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CouponAdapter;
import com.shunlian.app.adapter.CouponsAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PGetCoupon;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IGetCoupon;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

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

public class GetCouponAct extends BaseActivity implements View.OnClickListener, IGetCoupon, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_remen)
    MyTextView mtv_remen;

    @BindView(R.id.mtv_zuixin)
    MyTextView mtv_zuixin;

    @BindView(R.id.rv_pingtai)
    RecyclerView rv_pingtai;

    @BindView(R.id.rv_dianpu)
    RecyclerView rv_dianpu;

    @BindView(R.id.miv_search)
    MyImageView miv_search;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private MessageCountManager messageCountManager;

    private PGetCoupon pGetCoupon;
    private CouponsAdapter couponsAdapter;
    private CouponAdapter couponAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String type = "All";
    private List<VouchercenterplEntity.MData> mDatas;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, GetCouponAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_get_coupon;
    }

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.channel();
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
        mtv_remen.setOnClickListener(this);
        mtv_zuixin.setOnClickListener(this);
        rv_dianpu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pGetCoupon != null) {
                            pGetCoupon.refreshBaby(type, "");
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.miv_search:
                SearchCouponAct.startAct(getBaseContext());
                break;
            case R.id.mtv_zuixin:
                if (rv_dianpu.getScrollState() == 0) {
                    type = "NEW";
                    mtv_zuixin.setBackgroundResource(R.drawable.bg_hot_top);
                    mtv_remen.setBackgroundResource(R.drawable.bg_new_top);
                    pGetCoupon.resetBaby(type, "");
                }
                break;
            case R.id.mtv_remen:
                if (rv_dianpu.getScrollState() == 0) {
                    type = "All";
                    mtv_remen.setBackgroundResource(R.drawable.bg_hot_top);
                    mtv_zuixin.setBackgroundResource(R.drawable.bg_new_top);
                    pGetCoupon.resetBaby(type, "");
                }
                break;
        }
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
        pGetCoupon = new PGetCoupon(this, this);
        pGetCoupon.getPing();
        pGetCoupon.resetBaby(type, "");

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
        mDatas = new ArrayList<>();
        mDatas.addAll(vouchercenterplEntity.seller_voucher);
        rv_pingtai.setNestedScrollingEnabled(false);
        couponAdapter = new CouponAdapter(this, false, mDatas, pGetCoupon);
        rv_pingtai.setAdapter(couponAdapter);
        rv_pingtai.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rv_pingtai.addItemDecoration(new MVerticalItemDecoration(this,10,0,0));
    }

    @Override
    public void setdianData(List<VouchercenterplEntity.MData> mData, String page, String total) {
        if (couponsAdapter == null) {
            couponsAdapter = new CouponsAdapter(this, true, mData, pGetCoupon);
            linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
            rv_dianpu.setLayoutManager(linearLayoutManager);
            rv_dianpu.setAdapter(couponsAdapter);
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
        if ("1".equals(isGet)){
            if (isCommon) {
                mDatas.get(position).if_get = "1";
                couponAdapter.notifyItemChanged(position);
            } else {
                pGetCoupon.mDatas.get(position).if_get = "1";
                couponsAdapter.notifyItemChanged(position);
            }
        }
    }

}
