package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CouponAdapter;
import com.shunlian.app.adapter.CouponsAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PGetCoupon;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IGetCoupon;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/7.
 */

public class GetCouponAct extends BaseActivity implements View.OnClickListener, IGetCoupon, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_remen)
    MyTextView mtv_remen;

    @BindView(R.id.mtv_zuixin)
    MyTextView mtv_zuixin;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.rv_pingtai)
    RecyclerView rv_pingtai;

    @BindView(R.id.rv_dianpu)
    RecyclerView rv_dianpu;

    @BindView(R.id.miv_search)
    MyImageView miv_search;

    private PGetCoupon pGetCoupon;
    private CouponsAdapter couponsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String type = "All";
    private MessageCountManager messageCountManager;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, GetCouponAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_get_coupon;
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
                    pGetCoupon.resetBaby(type, "");
                }
                break;
            case R.id.mtv_remen:
                if (rv_dianpu.getScrollState() == 0) {
                    type = "All";
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
    protected void onResume() {
        if (messageCountManager.isLoad()) {
            messageCountManager.setTextCount(tv_msg_count);
        } else {
            messageCountManager.initData();
        }
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        messageCountManager.setTextCount(tv_msg_count);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setpingData(VouchercenterplEntity vouchercenterplEntity) {
        rv_pingtai.setNestedScrollingEnabled(false);
        rv_pingtai.setAdapter(new CouponAdapter(this, false, vouchercenterplEntity.seller_voucher));
        rv_pingtai.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rv_pingtai.addItemDecoration(new MVerticalItemDecoration(this,10,0,0));
    }

    @Override
    public void setdianData(List<VouchercenterplEntity.MData> mData, String page, String total) {
        if (couponsAdapter == null) {
            couponsAdapter = new CouponsAdapter(getBaseContext(), false, mData);
            linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
            rv_dianpu.setLayoutManager(linearLayoutManager);
            rv_dianpu.setAdapter(couponsAdapter);
        } else {
            couponsAdapter.notifyDataSetChanged();
        }
        couponsAdapter.setPageLoading(Integer.parseInt(page), Integer.parseInt(total));
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        messageCountManager.setTextCount(tv_msg_count);
    }

    @Override
    public void OnLoadFail() {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
