package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ProbablyLikeAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.ProbablyLikeEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PaySuccessPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.ui.plus.PlusOrderDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPaySuccessView;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySuccessAct extends BaseActivity implements IPaySuccessView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.mtv_price)
    MyTextView mtv_price;

    @BindView(R.id.mtv_name)
    MyTextView mtv_name;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private MessageCountManager messageCountManager;

    private String orderId,plus_order_id="";
    private String pay_sn;
    private boolean isPlus;
    private PaySuccessPresenter presenter;

    public static void startAct(Context context, String orderId, String price, String pay_sn,boolean isPlus) {
        Intent intent = new Intent(context, PaySuccessAct.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("price", price);
        intent.putExtra("pay_sn", pay_sn);
        intent.putExtra("isPlus", isPlus);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_pay_success;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        pay_sn = intent.getStringExtra("pay_sn");
        String price = intent.getStringExtra("price");
        isPlus=intent.getBooleanExtra("isPlus",false);
        mtv_price.setText(price);

        GridLayoutManager manager = new GridLayoutManager(this,2);
        rv_goods.setLayoutManager(manager);
        rv_goods.addItemDecoration(new GridSpacingItemDecoration
                (TransformUtil.dip2px(this, 5), false));
        presenter = new PaySuccessPresenter(this,this,pay_sn,isPlus);
    }

    @OnClick(R.id.mtv_order)
    public void seeOrderDetail(){
        if (isPlus){
            PlusOrderDetailAct.startAct(this,plus_order_id);
        }else {
            if (TextUtils.isEmpty(orderId)){
                MyOrderAct.startAct(this,0);
            }else {
                OrderDetailAct.startAct(this,orderId);
            }
        }
    }

    @OnClick(R.id.mtv_firstPage)
    public void backFirstPage(){
        Common.goGoGo(this,"");
    }

    @OnClick(R.id.rl_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.paySuccess();
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
    public void setData(ProbablyLikeEntity probablyLikeEntity) {
        plus_order_id=probablyLikeEntity.order_id;
        if (!isEmpty(probablyLikeEntity.may_be_buy_list)){
            ProbablyLikeAdapter adapter = new ProbablyLikeAdapter
                    (this,probablyLikeEntity.may_be_buy_list,isPlus);
            rv_goods.setAdapter(adapter);
            adapter.setOnItemClickListener((v,p)->{
                ProbablyLikeEntity.MayBuyList mayBuyList = probablyLikeEntity.may_be_buy_list.get(p);
                Common.goGoGo(this,"goods",mayBuyList.id);
            });
            if (isPlus){
                mtv_name.setText(getStringResouce(R.string.pay_plusdianzhu));
            }else {
                mtv_name.setText(getStringResouce(R.string.pay_nikeneng));
            }
            mtv_name.setVisibility(View.VISIBLE);
        }else {
            mtv_name.setVisibility(View.GONE);
        }
    }
}
