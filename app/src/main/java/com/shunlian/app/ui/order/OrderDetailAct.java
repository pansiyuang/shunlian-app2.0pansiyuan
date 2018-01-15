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
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.presenter.OrderDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.confirm_order.OrderLogisticsActivity;
import com.shunlian.app.ui.my_comment.CreatCommentActivity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.OrderdetailView;
import com.shunlian.app.widget.DiscountListDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pay.PayListActivity;

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

    @BindView(R.id.mtv_fukuanshijian)
    MyTextView mtv_fukuanshijian;

    @BindView(R.id.mtv_fahuojian)
    MyTextView mtv_fahuojian;

    @BindView(R.id.mtv_chengjiaoshijian)
    MyTextView mtv_chengjiaoshijian;

    @BindView(R.id.mllayout_store)
    MyLinearLayout mllayout_store;

    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.mtv_title1)
    MyTextView mtv_title1;

    @BindView(R.id.mtv_title2)
    MyTextView mtv_title2;

    @BindView(R.id.mtv_title3)
    MyTextView mtv_title3;

    @BindView(R.id.view_message)
    View view_message;

    @BindView(R.id.mrlayout_youhuiquan)
    MyRelativeLayout mrlayout_youhuiquan;

    @BindView(R.id.mrlayout_cuxiao)
    MyRelativeLayout mrlayout_cuxiao;

    private OrderDetailPresenter orderDetailPresenter;
    private String storeId, orderId = "54";

    private  int pink_color;
    private  int new_gray;
    private  int strokeWidth;
    private OrderdetailEntity orderdetailEntity;

    public static void startAct(Context context, String orderId) {
        Intent intent = new Intent(context, OrderDetailAct.class);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (orderDetailPresenter!=null){
            orderDetailPresenter.initApiData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_order_detail;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("orderId"))) {
            orderId = getIntent().getStringExtra("orderId");
        }
        //设置圆角背景
        GradientDrawable copyBackground = (GradientDrawable) mtv_copy.getBackground();
        copyBackground.setColor(getColorResouce(R.color.white));//设置填充色
//        float[] floats={30,30,30,30,30,30,30,30};//每两个数值代表一个角，左上，右上，右下，左下
//        goodBackground.setCornerRadii(floats);
//        goodBackground.setCornerRadius(30);//数值近似px

        //设置边线,但是圆角的弧度不能统一，不建议使用
//        goodBackground.setStroke(10,Color.parseColor("#858585"));
        pink_color = getColorResouce(R.color.pink_color);
        new_gray = getColorResouce(R.color.new_gray);
        strokeWidth = TransformUtil.dip2px(this, 0.5f);
        orderDetailPresenter = new OrderDetailPresenter(this, this, orderId);
    }

    @Override
    public void setOrder(OrderdetailEntity orderdetailEntity) {
        this.orderdetailEntity=orderdetailEntity;
        mtv_state.setText(orderdetailEntity.notice_status.status_text);
        mtv_time.setText(orderdetailEntity.notice_status.status_small);
        mtv_number.setText("订单号：" + orderdetailEntity.order_sn);
        mtv_phone.setText("联系电话："+orderdetailEntity.receipt_address.mobile);
        mtv_name.setText("收件人："+orderdetailEntity.receipt_address.realname);
        mtv_address.setText("收货地址："+orderdetailEntity.receipt_address.receipt_address);
        if (TextUtils.isEmpty(orderdetailEntity.remark)){
            mtv_message.setVisibility(View.INVISIBLE);
            view_message.setVisibility(View.INVISIBLE);
        }else {
            mtv_message.setText("用户留言：" + orderdetailEntity.remark);
            mtv_message.setVisibility(View.VISIBLE);
            view_message.setVisibility(View.VISIBLE);
        }
        mtv_storeName.setText(orderdetailEntity.store_name);
        mtv_zongjia.setText(getStringResouce(R.string.common_yuan) + orderdetailEntity.goods_amount);
        mtv_yunfei.setText(getStringResouce(R.string.common_yuan) + orderdetailEntity.shipping_fee);
        if (TextUtils.isEmpty(orderdetailEntity.promotion)&&Integer.parseInt(orderdetailEntity.promotion)>0){
            mtv_cuxiao.setText("-" + getStringResouce(R.string.common_yuan) +orderdetailEntity.promotion);
            mrlayout_cuxiao.setVisibility(View.VISIBLE);
        }else {
            mrlayout_cuxiao.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(orderdetailEntity.voucher_amount)&&Integer.parseInt(orderdetailEntity.voucher_amount)>0){
            mtv_youhuiquan.setText("-" + getStringResouce(R.string.common_yuan) + orderdetailEntity.voucher_amount);
            mrlayout_youhuiquan.setVisibility(View.VISIBLE);
        }else {
            mrlayout_youhuiquan.setVisibility(View.GONE);
        }
        mtv_shifu.setText(getStringResouce(R.string.common_yuan) + orderdetailEntity.total_amount);
        if (TextUtils.isEmpty(orderdetailEntity.paytype)){
            mtv_zhifufangshi.setVisibility(View.GONE);
        }else {
            mtv_zhifufangshi.setVisibility(View.VISIBLE);
            mtv_zhifufangshi.setText("支付方式：" + orderdetailEntity.paytype);
        }
        if (TextUtils.isEmpty(orderdetailEntity.create_time)){
            mtv_xiadanshijian.setVisibility(View.GONE);
        }else {
            mtv_xiadanshijian.setVisibility(View.VISIBLE);
            mtv_xiadanshijian.setText("下单时间：" + orderdetailEntity.create_time);
        }
        if (TextUtils.isEmpty(orderdetailEntity.pay_time)){
            mtv_fukuanshijian.setVisibility(View.GONE);
        }else {
            mtv_fukuanshijian.setVisibility(View.VISIBLE);
            mtv_fukuanshijian.setText("付款时间：" + orderdetailEntity.pay_time);
        }
        if (TextUtils.isEmpty(orderdetailEntity.send_time)){
            mtv_fahuojian.setVisibility(View.GONE);
        }else {
            mtv_fahuojian.setVisibility(View.VISIBLE);
            mtv_fahuojian.setText("发货时间：" + orderdetailEntity.send_time);
        }
        if (TextUtils.isEmpty(orderdetailEntity.receive_time)){
            mtv_chengjiaoshijian.setVisibility(View.GONE);
        }else {
            mtv_chengjiaoshijian.setVisibility(View.VISIBLE);
            mtv_chengjiaoshijian.setText("成交时间：" + orderdetailEntity.receive_time);
        }
        storeId = orderdetailEntity.store_id;
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_goods.setLayoutManager(manager);
        rv_goods.setNestedScrollingEnabled(false);
        rv_goods.setAdapter(new OrderGoodAdapter(this, orderdetailEntity.order_goods));
        //-1取消状态，0待支付，1待发货, 2待收货, 3待评价, 4已评价
        GradientDrawable t1Dackground;
        GradientDrawable t2Dackground;
        GradientDrawable t3Dackground;
        mtv_time.setVisibility(View.GONE);
        switch (orderdetailEntity.status) {
            case "-1":
                mtv_time.setVisibility(View.VISIBLE);
                miv_logo.setImageResource(R.mipmap.img_orderdetails_close);
                mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth, new_gray);
                mtv_title1.setTextColor(new_gray);
                mtv_title1.setText(getString(R.string.contact_seller));

                mtv_title2.setVisibility(View.GONE);
                mtv_title3.setVisibility(View.GONE);
                break;
            case "0":
                mtv_time.setVisibility(View.VISIBLE);
                miv_logo.setImageResource(R.mipmap.img_orderdetails_daifukuan);
                mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth, new_gray);
                mtv_title1.setTextColor(new_gray);
                mtv_title1.setText(getString(R.string.contact_seller));

                mtv_title2.setVisibility(View.VISIBLE);
                t2Dackground = (GradientDrawable) mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth, new_gray);
                mtv_title2.setTextColor(new_gray);
                mtv_title2.setText(getString(R.string.cancel_order));

                mtv_title3.setVisibility(View.VISIBLE);
                t3Dackground = (GradientDrawable) mtv_title3.getBackground();
                t3Dackground.setStroke(strokeWidth, pink_color);
                mtv_title3.setTextColor(pink_color);
                mtv_title3.setText(getString(R.string.order_fukuan));
                break;
            case "1":
                miv_logo.setImageResource(R.mipmap.img_orderdetails_daifahuo);
                mtv_title1.setVisibility(View.VISIBLE);
                t1Dackground = (GradientDrawable) mtv_title1.getBackground();
                t1Dackground.setStroke(strokeWidth, new_gray);
                mtv_title1.setTextColor(new_gray);
                mtv_title1.setText(getString(R.string.remind_send));

                mtv_title2.setVisibility(View.GONE);
                mtv_title3.setVisibility(View.GONE);
                break;
            case "2":
                miv_logo.setImageResource(R.mipmap.img_orderdetails_daishouhuo);
                if ("1".equals(orderdetailEntity.is_postpone)){
                    mtv_title1.setVisibility(View.GONE);
                }else {
                    mtv_title1.setVisibility(View.VISIBLE);
                    t1Dackground = (GradientDrawable) mtv_title1.getBackground();
                    t1Dackground.setStroke(strokeWidth, new_gray);
                    mtv_title1.setTextColor(new_gray);
                    mtv_title1.setText(getString(R.string.extend_the_collection));
                }

                mtv_title2.setVisibility(View.VISIBLE);
                t2Dackground = (GradientDrawable) mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth, new_gray);
                mtv_title2.setTextColor(new_gray);
                mtv_title2.setText(getString(R.string.order_wuliu));

                mtv_title3.setVisibility(View.VISIBLE);
                t3Dackground = (GradientDrawable) mtv_title3.getBackground();
                t3Dackground.setStroke(strokeWidth, pink_color);
                mtv_title3.setTextColor(pink_color);
                mtv_title3.setText(getString(R.string.confirm_goods));
                break;
            case "3":
                miv_logo.setImageResource(R.mipmap.img_orderdetails_success);

                mtv_title1.setVisibility(View.GONE);

                mtv_title2.setVisibility(View.VISIBLE);
                t2Dackground = (GradientDrawable) mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth, new_gray);
                mtv_title2.setTextColor(new_gray);
                mtv_title2.setText(getString(R.string.order_wuliu));

                mtv_title3.setVisibility(View.VISIBLE);
                t3Dackground = (GradientDrawable) mtv_title3.getBackground();
                t3Dackground.setStroke(strokeWidth, pink_color);
                mtv_title3.setTextColor(pink_color);
                mtv_title3.setText(getString(R.string.comment));
                break;
            case "4":
                miv_logo.setImageResource(R.mipmap.img_orderdetails_common);
                mtv_title1.setVisibility(View.GONE);

                t2Dackground = (GradientDrawable) mtv_title2.getBackground();
                t2Dackground.setStroke(strokeWidth, new_gray);
                mtv_title2.setTextColor(new_gray);
                mtv_title2.setText(getString(R.string.order_wuliu));

                if ("1".equals(orderdetailEntity.is_append)) {
                    mtv_title3.setVisibility(View.VISIBLE);
                    t3Dackground = (GradientDrawable) mtv_title3.getBackground();
                    t3Dackground.setStroke(strokeWidth, pink_color);
                    mtv_title3.setTextColor(pink_color);
                    mtv_title3.setText(getString(R.string.append_comment));
                } else {
                    mtv_title3.setVisibility(View.GONE);
                }
                break;
        }
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
        mtv_title1.setOnClickListener(this);
        mtv_title2.setOnClickListener(this);
        mtv_title3.setOnClickListener(this);
    }
    /**
     * 确认收货
     */
    public void confirmreceipt() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener("要确认收货吗？", "确认收货后卖家将收到您的货款", "确认收货", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderDetailPresenter != null) {
                    orderDetailPresenter.confirmreceipt(orderId);
                }
                promptDialog.dismiss();
            }
        }, "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.dismiss();
            }
        }).show();
    }

    /*
        取消订单
     */
    public void cancleOrder() {
        DiscountListDialog dialog = new DiscountListDialog(this);
        dialog.setSelectReason();
        dialog.show();
        dialog.setSelectListener(new DiscountListDialog.ISelectListener() {
            @Override
            public void onSelect(int position) {
                if (orderDetailPresenter != null) {
                    orderDetailPresenter.cancleOrder(orderId,position + 1);
                }
            }
        });
    }

    /*
    延长收货
     */
    public void extendTheCollection() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener("确认延长收货时间？", "每笔订单只能延迟一次哦", "确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderDetailPresenter != null) {
                    orderDetailPresenter.postpone(orderId);
                }
                promptDialog.dismiss();
            }
        }, "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.dismiss();
            }
        }).show();
    }
    @Override
    public void onClick(View view) {
        if (FastClickListener.isFastClick()) {
            return;
        }
        CharSequence text = null;
        switch (view.getId()) {
            case R.id.mtv_copy:
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mtv_number.getText()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                Common.staticToast("复制成功");
                break;
            case R.id.mllayout_store:
                StoreAct.startAct(this, storeId);
                break;
            case R.id.mtv_title1:
                text = mtv_title1.getText();
                if (getString(R.string.contact_seller).equals(text)) {//联系商家

                } else if (getString(R.string.remind_send).equals(text)) {//提醒发货
                    orderDetailPresenter.remindseller(orderId);
                } else if (getString(R.string.extend_the_collection).equals(text)) {//延长收货
                    extendTheCollection();
                }

                break;
            case R.id.mtv_title2:
                text = mtv_title2.getText();
                if (getString(R.string.cancel_order).equals(text)) {//取消订单
                    cancleOrder();
                } else if (getString(R.string.order_wuliu).equals(text)) {//物流
                    //MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
                    OrderLogisticsActivity.startAct(this,orderId);
                }
                break;
            case R.id.mtv_title3:
                text = mtv_title3.getText();
                if (getString(R.string.order_fukuan).equals(text)) {//付款
                    PayListActivity.startAct(this, null,null,orderId,orderdetailEntity.total_amount);
                } else if (getString(R.string.confirm_goods).equals(text)) {//确认收货
                    confirmreceipt();
                } else if (getString(R.string.comment).equals(text)) {//评价
                    //MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
                    List<ReleaseCommentEntity> entities = new ArrayList<>();
                    List<OrderdetailEntity.Good> order_goods = orderdetailEntity.order_goods;
                    for (int i = 0; i < order_goods.size(); i++) {
                        OrderdetailEntity.Good bean = order_goods.get(i);
                        ReleaseCommentEntity entity = new ReleaseCommentEntity(orderId,
                                bean.thumb, bean.title, bean.price, bean.goods_id);
                        entities.add(entity);
                    }
                    CreatCommentActivity.startAct(this, entities, CreatCommentActivity.CREAT_COMMENT);

                } else if (getString(R.string.append_comment).equals(text)) {//追评

                    //MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
                    List<ReleaseCommentEntity> entities = new ArrayList<>();
                    List<OrderdetailEntity.Good> order_goods = orderdetailEntity.order_goods;
                    for (int i = 0; i < order_goods.size(); i++) {
                        OrderdetailEntity.Good bean = order_goods.get(i);
                        ReleaseCommentEntity entity = new ReleaseCommentEntity(bean.thumb,
                                bean.title, bean.price, bean.comment_id);
                        entities.add(entity);
                    }
                    CreatCommentActivity.startAct(this, entities, CreatCommentActivity.APPEND_COMMENT);
                }
                break;
        }
    }
}
