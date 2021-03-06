package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.SuccessfulTradePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISuccessfulTradeView;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/20.
 */

public class SuccessfulTradeAct extends BaseActivity implements ISuccessfulTradeView, MessageCountManager.OnGetMessageListener {

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private MessageCountManager messageCountManager;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    private String order_id;
    private String order_sn;

    public static void startAct(Context context, String order_sn, String id){
        Intent intent = new Intent(context, SuccessfulTradeAct.class);
        intent.putExtra("order_id",id);
        intent.putExtra("order_sn",order_sn);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_successful_trade;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        setStatusBarColor(R.color.pink_color);
        GridLayoutManager manager = new GridLayoutManager(this,2);
        recy_view.setLayoutManager(manager);
        recy_view.addItemDecoration(new GridSpacingItemDecoration
                (TransformUtil.dip2px(this, 5), false));

        order_id = getIntent().getStringExtra("order_id");
        order_sn = getIntent().getStringExtra("order_sn");
        SuccessfulTradePresenter presenter = new SuccessfulTradePresenter(this,this, order_id);
    }

    @OnClick(R.id.rl_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.order();
    }

    @OnClick(R.id.mtv_look_order)
    public void lookOrder(){
        OrderDetailAct.startAct(this,order_id);
    }

    @OnClick(R.id.mtv_comment)
    public void comment(){
        ReleaseCommentEntity entity = new ReleaseCommentEntity();
        entity.order_sn = order_sn;
        CreatCommentActivity.startAct(this,entity,CreatCommentActivity.CREAT_COMMENT);
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

    @Override
    protected void onResume() {
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
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
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
}
