package com.shunlian.app.ui.order;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.OrderGoodAdapter;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.presenter.OrderDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.view.OrderdetailView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class OrderDetailAct extends BaseActivity implements View.OnClickListener, OrderdetailView {
    @BindView(R.id.mtv_state)
    MyTextView mtv_state;

    @BindView(R.id.mtv_time)
    MyTextView mtv_time;

    @BindView(R.id.miv_logo)
    MyImageView miv_logo;

    @BindView(R.id.mtv_number)
    MyTextView mtv_number;

    @BindView(R.id.mtv_copy)
    MyTextView mtv_copy;

    @BindView(R.id.mtv_phone)
    MyTextView mtv_phone;

    @BindView(R.id.mtv_name)
    MyTextView mtv_name;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_message)
    MyTextView mtv_message;

    @BindView(R.id.mtv_storeName)
    MyTextView mtv_storeName;

    @BindView(R.id.mtv_zongjia)
    MyTextView mtv_zongjia;

    @BindView(R.id.mtv_yunfei)
    MyTextView mtv_yunfei;

    @BindView(R.id.mtv_cuxiao)
    MyTextView mtv_cuxiao;

    @BindView(R.id.mtv_youhuiquan)
    MyTextView mtv_youhuiquan;

    @BindView(R.id.mtv_shifu)
    MyTextView mtv_shifu;

    @BindView(R.id.mtv_zhifufangshi)
    MyTextView mtv_zhifufangshi;

    @BindView(R.id.mtv_xiadanshijian)
    MyTextView mtv_xiadanshijian;

    @BindView(R.id.mtv_wuliu)
    MyTextView mtv_wuliu;

    @BindView(R.id.mllayout_store)
    MyLinearLayout mllayout_store;

    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;


    private OrderDetailPresenter orderDetailPresenter;
    private String storeId, orderId;

    public static void startAct(Context context, String orderId) {
        Intent intent = new Intent(context, OrderDetailAct.class);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_order_detail;
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("orderId"))) {
            storeId = getIntent().getStringExtra("orderId");
        }
        orderDetailPresenter = new OrderDetailPresenter(this, this, "54");
    }

    @Override
    public void setOrder(OrderdetailEntity orderdetailEntity) {
        //设置圆角背景
        GradientDrawable goodBackground = (GradientDrawable) mtv_copy.getBackground();
        goodBackground.setColor(getColorResouce(R.color.white));//设置填充色
//        float[] floats={30,30,30,30,30,30,30,30};//每两个数值代表一个角，左上，右上，右下，左下
//        goodBackground.setCornerRadii(floats);
//        goodBackground.setCornerRadius(30);//数值近似px

        //设置边线,但是圆角的弧度不能统一，不建议使用
//        goodBackground.setStroke(10,Color.parseColor("#858585"));

        mtv_state.setText(orderdetailEntity.notice_status.status_text);
        mtv_time.setText(orderdetailEntity.notice_status.status_small);
        mtv_number.setText("订单号：" + orderdetailEntity.order_sn);
        mtv_phone.setText(orderdetailEntity.receipt_address.mobile);
        mtv_name.setText(orderdetailEntity.receipt_address.realname);
        mtv_address.setText(orderdetailEntity.receipt_address.receipt_address);
        mtv_message.setText("用户留言：" + orderdetailEntity.remark);
        mtv_storeName.setText(orderdetailEntity.store_name);
        mtv_zongjia.setText(getStringResouce(R.string.common_yuan) + orderdetailEntity.goods_amount);
        mtv_yunfei.setText(getStringResouce(R.string.common_yuan) + orderdetailEntity.shipping_fee);
        mtv_cuxiao.setText(orderdetailEntity.promotion);
        mtv_youhuiquan.setText("-" + getStringResouce(R.string.common_yuan) + orderdetailEntity.voucher_amount);
        mtv_shifu.setText(getStringResouce(R.string.common_yuan) + orderdetailEntity.total_amount);
        mtv_zhifufangshi.setText("支付方式：" + orderdetailEntity.paytype);
        mtv_xiadanshijian.setText("下单时间：" + orderdetailEntity.create_time);
        storeId = orderdetailEntity.store_id;
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_goods.setLayoutManager(manager);
        rv_goods.setAdapter(new OrderGoodAdapter(this, orderdetailEntity.order_goods));
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
        mtv_copy.setOnClickListener(this);
        mllayout_store.setOnClickListener(this);
        mtv_wuliu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (FastClickListener.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.mtv_copy:
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mtv_number.getText()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                Common.staticToast("复制成功");
                break;
            case R.id.mllayout_store:
                StoreAct.startAct(this, storeId);
                break;
            case R.id.mtv_wuliu:
                StoreAct.startAct(this, storeId);
                break;
        }
    }
}
