package com.shunlian.app.ui.returns_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.ServiceTypeAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.SelectServicePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ISelectServiceView;
import com.shunlian.app.widget.CustomerGoodsView;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/26.
 */

public class SelectServiceActivity extends BaseActivity implements ISelectServiceView, BaseRecyclerAdapter.OnItemClickListener, MessageCountManager.OnGetMessageListener {

    @BindView(R.id.customer_goods)
    CustomerGoodsView customer_goods;

    @BindView(R.id.recycler_service)
    RecyclerView recycler_service;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mtv_toolbar_msgCount)
    MyTextView mtv_toolbar_msgCount;

    private String currentOgId;
    private SelectServicePresenter presenter;
    private RefundDetailEntity.RefundDetail.Edit mEntity;

    private MessageCountManager messageCountManager;

    public static void startAct(Context context, String ogId) {
        Intent intent = new Intent(context, SelectServiceActivity.class);
        intent.putExtra("ogId", ogId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_service;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        EventBus.getDefault().register(this);

        mtv_toolbar_title.setText(getStringResouce(R.string.select_service_type));

        presenter = new SelectServicePresenter(this, this);
        currentOgId = getIntent().getStringExtra("ogId");
        if (!isEmpty(currentOgId)) {
            presenter.getRefundInfo(currentOgId);
        }
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(baseAct);
            if (messageCountManager.isLoad()) {
                String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
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
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void getRefundInfo(RefundDetailEntity.RefundDetail.Edit infoEntity) {
        mEntity = infoEntity;
        GlideUtils.getInstance().loadImage(this, customer_goods.getGoodsIcon(), infoEntity.thumb);
        customer_goods.setLabelName(infoEntity.store_name, true);
        customer_goods.setGoodsTitle(infoEntity.title);
        customer_goods.setGoodsPrice(getStringResouce(R.string.common_yuan) + infoEntity.price);
        customer_goods.setGoodsCount("x" + infoEntity.qty);
        customer_goods.setGoodsParams(infoEntity.sku_desc);

        if (infoEntity.refund_choice != null) {
            ServiceTypeAdapter serviceTypeAdapter = new ServiceTypeAdapter(this, false, infoEntity.refund_choice);
            serviceTypeAdapter.setOnItemClickListener(this);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recycler_service.setLayoutManager(manager);
            recycler_service.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(this, 0.5f), 0, 0, getColorResouce(R.color.white_ash)));
            recycler_service.setNestedScrollingEnabled(false);
            recycler_service.setAdapter(serviceTypeAdapter);
        }
    }

    @OnClick(R.id.mrlayout_toolbar_more)
    public void more() {
        visible(quick_actions);
        quick_actions.afterSale();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onItemClick(View view, int position) {
        if (mEntity != null && mEntity.refund_choice != null) {
            List<RefundDetailEntity.RefundDetail.Edit.RefundChoice> choices = mEntity.refund_choice;
            RefundDetailEntity.RefundDetail.Edit.RefundChoice choice = choices.get(position);
            mEntity.serviceType = choice.type;
            mEntity.og_Id = currentOgId;
            ReturnRequestActivity.startAct(this, mEntity, false, null);
        }
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }
}
