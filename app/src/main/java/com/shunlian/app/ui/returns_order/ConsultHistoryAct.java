package com.shunlian.app.ui.returns_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ConsultHistoryAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.ConsultHistoryEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.ui.MessageActivity;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.ConsultHistoryPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IConsultHistoryView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ConsultHistoryAct extends BaseActivity implements IConsultHistoryView, MessageCountManager.OnGetMessageListener {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.mllayout_call_business)
    MyLinearLayout mllayout_call_business;

    private ConsultHistoryPresenter presenter;
    private MessageCountManager messageCountManager;

    public static void startAct(Context context, String refund_id) {
        Intent intent = new Intent(context, ConsultHistoryAct.class);
        intent.putExtra("refund_id", refund_id);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_consult_history;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        EventBus.getDefault().register(this);
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);

        String refund_id = getIntent().getStringExtra("refund_id");
        presenter = new ConsultHistoryPresenter(this, this, refund_id);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
    }

    @Override
    protected void initListener() {
        mllayout_call_business.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mllayout_call_business:
                MessageActivity.startAct(this);
                break;
        }
        super.onClick(view);
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

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.afterSale();
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

    @Override
    public void consultHistory(ConsultHistoryEntity entity) {
        ConsultHistoryAdapter adapter = new ConsultHistoryAdapter(this,
                false, entity.history_list);
        recy_view.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        messageCountManager.setTextCount(tv_msg_count);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        messageCountManager.setTextCount(tv_msg_count);
    }

    @Override
    public void OnLoadFail() {

    }
}
