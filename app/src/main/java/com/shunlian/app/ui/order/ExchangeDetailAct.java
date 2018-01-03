package com.shunlian.app.ui.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ExchangeDetailMsgAdapter;
import com.shunlian.app.adapter.ExchangeDetailOptAdapter;
import com.shunlian.app.adapter.OrderGoodAdapter;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.presenter.ExchangeDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ExchangeDetailView;
import com.shunlian.app.widget.CustomerGoodsView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class ExchangeDetailAct extends BaseActivity implements View.OnClickListener, ExchangeDetailView {
    @BindView(R.id.mtv_state)
    MyTextView mtv_state;

    @BindView(R.id.mtv_wuliu)
    MyTextView mtv_wuliu;

    @BindView(R.id.mtv_time)
    MyTextView mtv_time;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_phone)
    MyTextView mtv_phone;

    @BindView(R.id.mtv_name)
    MyTextView mtv_name;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_title1)
    MyTextView mtv_title1;

    @BindView(R.id.mtv_title2)
    MyTextView mtv_title2;

    @BindView(R.id.mtv_title3)
    MyTextView mtv_title3;

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

    @BindView(R.id.rv_msg)
    RecyclerView rv_msg;

    @BindView(R.id.rv_opt)
    RecyclerView rv_opt;

    private ExchangeDetailPresenter exchangeDetailPresenter;
    private String refund_id = "53";

    public static void startAct(Context context, String refund_id) {
        Intent intent = new Intent(context, ExchangeDetailAct.class);
        intent.putExtra("refund_id", refund_id);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_exchange_detail;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_title.setText("退款详情");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("refund_id"))) {
            refund_id = getIntent().getStringExtra("refund_id");
        }

        exchangeDetailPresenter = new ExchangeDetailPresenter(this, this, refund_id);
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
        mtv_title1.setOnClickListener(this);
        mtv_title2.setOnClickListener(this);
        mtv_title3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (FastClickListener.isFastClick()) {
            return;
        }
        CharSequence text = null;
        switch (view.getId()) {
//            case R.id.mtv_title1:
//                text = mtv_title1.getText();
//                if (getString(R.string.contact_seller).equals(text)) {//联系商家
//
//                } else if (getString(R.string.remind_send).equals(text)) {//提醒发货
//                    orderDetailPresenter.remindseller(orderId);
//                } else if (getString(R.string.extend_the_collection).equals(text)) {//延长收货
//                    extendTheCollection();
//                }
//
//                break;

        }
    }

    @Override
    public void setData(RefundDetailEntity refundDetailEntity) {
        RefundDetailEntity.RefundDetail refundDetail = refundDetailEntity.refund_detail;
        GlideUtils.getInstance().loadImage(this, ctgv_goods.getGoodsIcon(), refundDetail.thumb);
        ctgv_goods.setLabelName(refundDetail.store_name, true);
        ctgv_goods.setGoodsTitle(refundDetail.title);
        ctgv_goods.setGoodsParams(refundDetail.sku_desc);
        ctgv_goods.setGoodsCount("x"+refundDetail.qty);
        ctgv_goods.setGoodsPrice(getStringResouce(R.string.common_yuan)+refundDetail.price);
        mtv_reason.setText("退款原因："+refundDetail.buyer_message);
        mtv_money.setText("退款金额："+refundDetail.refund_amount);
        mtv_amount.setText("退款数量："+refundDetail.goods_num);
        mtv_applyTime.setText("申请时间："+refundDetail.add_time);
        mtv_order.setText("退款编号："+refundDetail.refund_sn);
        mtv_phone.setText("联系电话："+"null");
        mtv_name.setText("收件人："+"null");
        mtv_address.setText("null");
        mtv_wuliu.setText("null");
        LinearLayoutManager managerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_msg.setLayoutManager(managerV);
        rv_msg.setNestedScrollingEnabled(false);
        rv_msg.setAdapter(new ExchangeDetailMsgAdapter(this,false, refundDetail.msg_list));
        LinearLayoutManager managerH = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_opt.setLayoutManager(managerH);
        rv_opt.setNestedScrollingEnabled(false);
        rv_opt.setAdapter(new ExchangeDetailOptAdapter(this,false, refundDetail.opt_list));
        mtv_state.setText(refundDetail.status_desc);
        mtv_time.setText(refundDetail.time_desc);
//        web_desc.loadData("测试", "text/html; charset=UTF-8", null);//解决加载html代码乱码
    }
}
