package com.shunlian.app.ui.more_credit;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.PhoneDetailAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.PhoneOrderDetailEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PPhoneOrder;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPhoneOrder;
import com.shunlian.app.view.OrderdetailView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class PhoneOrderDetailAct extends BaseActivity implements IPhoneOrder,MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_state)
    MyTextView mtv_state;

    @BindView(R.id.mtv_number)
    MyTextView mtv_number;

    @BindView(R.id.mtv_copy)
    MyTextView mtv_copy;

    @BindView(R.id.mtv_storeName)
    MyTextView mtv_storeName;

    @BindView(R.id.miv_goods_pic)
    MyImageView miv_goods_pic;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_price)
    MyTextView mtv_price;

    @BindView(R.id.mtv_attribute)
    MyTextView mtv_attribute;

    @BindView(R.id.mtv_attributes)
    MyTextView mtv_attributes;

    @BindView(R.id.mtv_shifu)
    MyTextView mtv_shifu;

    @BindView(R.id.rv_trade)
    RecyclerView rv_trade;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private MessageCountManager messageCountManager;


    public static void startAct(Context context,String orderId) {
        Intent intent = new Intent(context, PhoneOrderDetailAct.class);
        intent.putExtra("orderId", orderId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_phone_order_detail;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.pink_color);
//        setStatusBarFontDark();
        EventBus.getDefault().register(this);

        String orderId = getIntent().getStringExtra("orderId");
        PPhoneOrder pPhoneOrder = new PPhoneOrder(this, this, orderId);
        pPhoneOrder.detail();

        //设置圆角背景
        GradientDrawable copyBackground = (GradientDrawable) mtv_copy.getBackground();
        copyBackground.setColor(getColorResouce(R.color.white));//设置填充色
//        float[] floats={30,30,30,30,30,30,30,30};//每两个数值代表一个角，左上，右上，右下，左下
//        goodBackground.setCornerRadii(floats);
//        goodBackground.setCornerRadius(30);//数值近似px

        //设置边线,但是圆角的弧度不能统一，不建议使用
//        goodBackground.setStroke(10,Color.parseColor("#858585"));
        copyBackground.setStroke(TransformUtil.dip2px(this, 0.5f), getColorResouce(R.color.new_gray));

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


    @Override
    protected void initListener() {
        super.initListener();
        mtv_copy.setOnClickListener(this);
        rl_more.setOnClickListener(this);
    }

    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.mtv_copy:
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mtv_number.getText()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                Common.staticToast("复制成功");
                break;
            case R.id.rl_more:
                quick_actions.setVisibility(View.VISIBLE);
                quick_actions.order();
                break;
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
        mtv_state.setText(phoneOrderDetailEntity.status_name);
        mtv_number.setText(phoneOrderDetailEntity.order_sn);
        mtv_storeName.setText(phoneOrderDetailEntity.store_name);
        GlideUtils.getInstance().loadImageZheng(this, miv_goods_pic, phoneOrderDetailEntity.image);
        mtv_title.setText(phoneOrderDetailEntity.card_addr);
        mtv_attribute.setText(phoneOrderDetailEntity.card_number);
        mtv_attributes.setText(phoneOrderDetailEntity.face_price);
        mtv_price.setText(phoneOrderDetailEntity.payment_money);
        mtv_shifu.setText(phoneOrderDetailEntity.payment_money);

        rv_trade.setLayoutManager(new LinearLayoutManager(this));
        rv_trade.setAdapter(new PhoneDetailAdapter(this, phoneOrderDetailEntity.trade));
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
