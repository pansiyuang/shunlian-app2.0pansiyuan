package com.shunlian.app.ui.order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ExchangeDetailMsgAdapter;
import com.shunlian.app.adapter.ExchangeDetailOptAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.ExchangeDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.timer.HourNoWhiteDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.view.ExchangeDetailView;
import com.shunlian.app.widget.CustomerGoodsView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class ExchangeDetailAct extends BaseActivity implements View.OnClickListener, ExchangeDetailView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_state)
    MyTextView mtv_state;

    @BindView(R.id.miv_gift)
    MyImageView miv_gift;

    @BindView(R.id.mtv_gift)
    MyTextView mtv_gift;

    @BindView(R.id.mtv_time)
    MyTextView mtv_time;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.mtv_phone)
    MyTextView mtv_phone;

    @BindView(R.id.mtv_name)
    MyTextView mtv_name;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_phones)
    MyTextView mtv_phones;

    @BindView(R.id.mtv_names)
    MyTextView mtv_names;

    @BindView(R.id.mtv_addresss)
    MyTextView mtv_addresss;

    @BindView(R.id.mtv_contact)
    MyTextView mtv_contact;

    @BindView(R.id.ctgv_goods)
    CustomerGoodsView ctgv_goods;

    @BindView(R.id.mtv_reason)
    MyTextView mtv_reason;

    @BindView(R.id.mtv_money)
    MyTextView mtv_money;

    @BindView(R.id.mtv_applyTime)
    MyTextView mtv_applyTime;

    @BindView(R.id.mtv_order)
    MyTextView mtv_order;

    @BindView(R.id.mtv_amount)
    MyTextView mtv_amount;

    @BindView(R.id.mtv_shouhuo)
    MyTextView mtv_shouhuo;

    @BindView(R.id.mrlayout_shouhuo)
    MyRelativeLayout mrlayout_shouhuo;

    @BindView(R.id.view_shouhuo)
    View view_shouhuo;

    @BindView(R.id.view_shouhuos)
    View view_shouhuos;

    @BindView(R.id.view_wulius)
    View view_wulius;

    @BindView(R.id.view_wuliu)
    View view_wuliu;

    @BindView(R.id.mtv_tuihuo)
    MyTextView mtv_tuihuo;

    @BindView(R.id.mrlayout_tuihuo)
    MyRelativeLayout mrlayout_tuihuo;

    @BindView(R.id.mrlayout_wuliu)
    MyRelativeLayout mrlayout_wuliu;

    @BindView(R.id.mlLayout_gift)
    MyLinearLayout mlLayout_gift;

    @BindView(R.id.mtv_wuliu)
    MyTextView mtv_wuliu;

    @BindView(R.id.mrlayout_wulius)
    MyRelativeLayout mrlayout_wulius;

    @BindView(R.id.mtv_wulius)
    MyTextView mtv_wulius;

    @BindView(R.id.view_tuihuo)
    View view_tuihuo;

    @BindView(R.id.view_tuihuos)
    View view_tuihuos;

    @BindView(R.id.rv_msg)
    RecyclerView rv_msg;

    @BindView(R.id.rv_opt)
    RecyclerView rv_opt;

    @BindView(R.id.mllayout_time)
    MyLinearLayout mllayout_time;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.downTime_order)
    HourNoWhiteDownTimerView downTime_order;

    private ExchangeDetailPresenter exchangeDetailPresenter;
    private String refund_id = "53";
    private MessageCountManager messageCountManager;
    private RefundDetailEntity.RefundDetail refundDetail;
    private boolean isFirst=true;

    public static void startAct(Context context, String refund_id) {
        Intent intent = new Intent(context, ExchangeDetailAct.class);
        intent.putExtra("refund_id", refund_id);
        context.startActivity(intent);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (exchangeDetailPresenter!=null){
            exchangeDetailPresenter.initApiData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_exchange_detail;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("refund_id"))) {
            refund_id = getIntent().getStringExtra("refund_id");
        }

        exchangeDetailPresenter = new ExchangeDetailPresenter(this, this, refund_id);
        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);
    }

    @Override
    protected void onResume() {
        if (messageCountManager.isLoad()) {
            String s = messageCountManager.setTextCount(tv_msg_count);
            if (quick_actions != null)
                quick_actions.setMessageCount(s);
        } else {
            messageCountManager.initData();
        }
        super.onResume();
    }

    @OnClick(R.id.rl_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.afterSale();
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    @Override
    protected void initListener() {
        super.initListener();
        mtv_contact.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (MyOnClickListener.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.mtv_contact:
                exchangeDetailPresenter.getUserId(refundDetail.store_id);
                break;

        }
    }

    /**
     * 换货确认收货
     * @param refund_id
     */
    public void confirmreceipt(String refund_id){
        if (exchangeDetailPresenter != null){
            exchangeDetailPresenter.confirmReceive(refund_id);
        }
    }

    @Override
    public void setData(RefundDetailEntity refundDetailEntity) {
        refundDetail = refundDetailEntity.refund_detail;
        int time=Integer.parseInt(refundDetail.rest_second);
        if (!TextUtils.isEmpty(refundDetail.rest_second)&&time>0){
            downTime_order.setDownTime(time);
            downTime_order.setDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onFinish() {
                    downTime_order.cancelDownTimer();
                    exchangeDetailPresenter.initApiData();
                }

            });
            downTime_order.startDownTimer();
            mtv_time.setVisibility(View.GONE);
            mllayout_time.setVisibility(View.VISIBLE);
        }else {
            mtv_time.setVisibility(View.VISIBLE);
            mllayout_time.setVisibility(View.GONE);
            mtv_time.setText(refundDetail.time_desc);
        }
        GlideUtils.getInstance().loadImage(this, ctgv_goods.getGoodsIcon(), refundDetail.thumb);
        ctgv_goods.setLabelName(refundDetail.store_name, true);
        ctgv_goods.setGoodsTitle(refundDetail.title);
        ctgv_goods.setGoodsParams(refundDetail.sku_desc);
        ctgv_goods.setGoodsCount("x" + refundDetail.qty);
        ctgv_goods.setGoodsPrice(getStringResouce(R.string.common_yuan) + refundDetail.price);

        LinearLayoutManager managerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_msg.setLayoutManager(managerV);
        rv_msg.setNestedScrollingEnabled(false);
        rv_msg.setAdapter(new ExchangeDetailMsgAdapter(this, false, refundDetail.msg_list));
        LinearLayoutManager managerH = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_opt.setLayoutManager(managerH);
        rv_opt.setNestedScrollingEnabled(false);
        refundDetail.edit.store_name = refundDetail.store_name;
        rv_opt.setAdapter(new ExchangeDetailOptAdapter(this, false, refundDetail.opt_list,refundDetail.refund_id,refundDetail.edit));

        mtv_state.setText(refundDetail.status_desc);
        if (refundDetail.return_address != null
                &&!TextUtils.isEmpty(refundDetail.return_address.address)) {
            mtv_tuihuo.setVisibility(View.VISIBLE);
            view_tuihuo.setVisibility(View.VISIBLE);
            view_tuihuos.setVisibility(View.VISIBLE);
            mrlayout_tuihuo.setVisibility(View.VISIBLE);
            mtv_phone.setText("联系电话：" + refundDetail.return_address.phone);
            mtv_name.setText("收件人：" + refundDetail.return_address.name);
            mtv_address.setText(refundDetail.return_address.address);
        } else {
            mtv_tuihuo.setVisibility(View.GONE);
            view_tuihuo.setVisibility(View.GONE);
            view_tuihuos.setVisibility(View.GONE);
            mrlayout_tuihuo.setVisibility(View.GONE);
        }
        if (refundDetail.member_address != null
                &&!TextUtils.isEmpty(refundDetail.member_address.address)) {
            mtv_shouhuo.setVisibility(View.VISIBLE);
            view_shouhuo.setVisibility(View.VISIBLE);
            view_shouhuos.setVisibility(View.VISIBLE);
            mrlayout_shouhuo.setVisibility(View.VISIBLE);
            mtv_phones.setText("联系电话：" + refundDetail.member_address.phone);
            mtv_names.setText("收件人：" + refundDetail.member_address.name);
            mtv_addresss.setText(refundDetail.member_address.address);
        } else {
            mtv_shouhuo.setVisibility(View.GONE);
            view_shouhuo.setVisibility(View.GONE);
            view_shouhuos.setVisibility(View.GONE);
            mrlayout_shouhuo.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(refundDetail.express)) {
            mrlayout_wuliu.setVisibility(View.GONE);
            mtv_wuliu.setVisibility(View.GONE);
            view_wuliu.setVisibility(View.GONE);
        } else {
            mrlayout_wuliu.setVisibility(View.VISIBLE);
            mtv_wuliu.setVisibility(View.VISIBLE);
            view_wuliu.setVisibility(View.VISIBLE);
            mtv_wuliu.setText(refundDetail.express);
        }

        if (TextUtils.isEmpty(refundDetail.s_express)) {
            mrlayout_wulius.setVisibility(View.GONE);
            mtv_wulius.setVisibility(View.GONE);
            view_wulius.setVisibility(View.GONE);
        } else {
            mrlayout_wulius.setVisibility(View.VISIBLE);
            mtv_wulius.setVisibility(View.VISIBLE);
            view_wulius.setVisibility(View.VISIBLE);
            mtv_wulius.setText(refundDetail.express);
        }

        if (refundDetail.gift!=null&&!TextUtils.isEmpty(refundDetail.gift.thumb)){
            mlLayout_gift.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImage(this,miv_gift,refundDetail.gift.thumb);
            mtv_gift.setText(refundDetail.gift.title);
        }else {
            mlLayout_gift.setVisibility(View.GONE);
        }
//        web_desc.loadData("测试", "text/html; charset=UTF-8", null);//解决加载html代码乱码
        String key;
        if ("4".equals(refundDetail.refund_type)) {
            mtv_title.setText("换货详情");
            key="换货";
        } else {
            mtv_title.setText("退款详情");
            key="退款";
        }
        if (TextUtils.isEmpty(refundDetail.buyer_message)){
            mtv_reason.setVisibility(View.GONE);
        }else {
            mtv_reason.setText(key+"原因：" + refundDetail.buyer_message);
            mtv_reason.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(refundDetail.refund_amount)){
            mtv_money.setVisibility(View.GONE);
        }else {
            mtv_money.setText("退款金额：" +getStringResouce(R.string.common_yuan)+ refundDetail.refund_amount);
            mtv_money.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(refundDetail.goods_num)){
            mtv_amount.setVisibility(View.GONE);
        }else {
            mtv_amount.setText(key+"数量：" + refundDetail.goods_num);
            mtv_amount.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(refundDetail.add_time)){
            mtv_applyTime.setVisibility(View.GONE);
        }else {
            mtv_applyTime.setText("申请时间：" + refundDetail.add_time);
            mtv_applyTime.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(refundDetail.refund_sn)){
            mtv_order.setVisibility(View.GONE);
        }else {
            mtv_order.setText(key+"编号：" + refundDetail.refund_sn);
            mtv_order.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void confirmReceive() {
        exchangeDetailPresenter.initApiData();
    }

    @Override
    public void getUserId(String userId) {
        ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();
        chatMember.nickname = refundDetail.store_name;
        chatMember.type = "3";
        chatMember.m_user_id = userId;
        chatMember.shop_id = refundDetail.store_id;
        ChatManager.getInstance(this).init().MemberChatToStore(chatMember);
    }

    @Override
    public void onDestroy() {
        if (downTime_order!=null){
            downTime_order.cancelDownTimer();
            downTime_order=null;
        }
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
