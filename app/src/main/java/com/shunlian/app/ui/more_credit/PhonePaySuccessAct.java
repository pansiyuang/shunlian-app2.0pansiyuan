package com.shunlian.app.ui.more_credit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.PhoneSuccessAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.PhoneOrderDetailEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PPhoneOrder;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPhoneOrder;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class PhonePaySuccessAct extends BaseActivity implements IPhoneOrder, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.ntv_phone)
    NewTextView ntv_phone;

    @BindView(R.id.ntv_before)
    NewTextView ntv_before;

    @BindView(R.id.ntv_after)
    NewTextView ntv_after;

    @BindView(R.id.ntv_hint)
    NewTextView ntv_hint;

    @BindView(R.id.mtv_order)
    MyTextView mtv_order;

    @BindView(R.id.mtv_name)
    MyTextView mtv_name;

    @BindView(R.id.mtv_firstPage)
    MyTextView mtv_firstPage;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;


    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    private MessageCountManager messageCountManager;
    private String orderId;

    public static void startAct(Context context, String orderId) {
        Intent intent = new Intent(context, PhonePaySuccessAct.class);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_phone_pay_success;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        orderId = getIntent().getStringExtra("orderId");

        GridLayoutManager manager = new GridLayoutManager(this,2);
        rv_goods.setLayoutManager(manager);
        rv_goods.addItemDecoration(new GridSpacingItemDecoration
                (TransformUtil.dip2px(this, 5), false));
        PPhoneOrder pPhoneOrder = new PPhoneOrder(this, this, orderId);
        pPhoneOrder.result();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_order.setOnClickListener(this);
        mtv_firstPage.setOnClickListener(this);
        rl_more.setOnClickListener(this);
    }

    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.mtv_order:
                PhoneOrderDetailAct.startAct(this,orderId);
                break;
            case R.id.mtv_firstPage:
                Common.goGoGo(this, "");
                break;
            case R.id.rl_more:
                quick_actions.setVisibility(View.VISIBLE);
                quick_actions.paySuccess();
                break;
        }
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


    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

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
    public void setApiData(PhoneOrderDetailEntity phoneOrderDetailEntity) {
        PhoneSuccessAdapter adapter = new PhoneSuccessAdapter
                (this, false,phoneOrderDetailEntity.recommend_goods.goods_list);
        rv_goods.setAdapter(adapter);
        adapter.setOnItemClickListener((v, p) -> {
            PhoneOrderDetailEntity.Goods.Good mayBuyList = phoneOrderDetailEntity.recommend_goods.goods_list.get(p);
            Common.goGoGo(this, "goods", mayBuyList.id);
        });
        ntv_phone.setText(phoneOrderDetailEntity.card_number);
        ntv_before.setText(phoneOrderDetailEntity.face_price);
        ntv_after.setText(phoneOrderDetailEntity.payment_money);
        mtv_name.setText(phoneOrderDetailEntity.recommend_goods.head_title);
        GradientDrawable background = (GradientDrawable) ntv_hint.getBackground();
        background.setColor(getColorResouce(R.color.value_FFF1D5));
        ntv_hint.setText(phoneOrderDetailEntity.desc_name);
    }
}
