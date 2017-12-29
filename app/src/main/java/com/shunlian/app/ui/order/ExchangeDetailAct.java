package com.shunlian.app.ui.order;

import android.content.ClipboardManager;
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
import com.shunlian.app.adapter.OrderGoodAdapter;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.presenter.ExchangeDetailPresenter;
import com.shunlian.app.presenter.OrderDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.confirm_order.OrderLogisticsActivity;
import com.shunlian.app.ui.my_comment.CreatCommentActivity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ExchangeDetailView;
import com.shunlian.app.view.OrderdetailView;
import com.shunlian.app.widget.CustomerGoodsView;
import com.shunlian.app.widget.DiscountListDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ExchangeDetailAct extends BaseActivity implements View.OnClickListener, ExchangeDetailView {
    @BindView(R.id.mtv_state)
    MyTextView mtv_state;

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

    @BindView(R.id.mtv_history)
    MyTextView mtv_history;

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

    @BindView(R.id.mtv_ceshi)
    MyTextView mtv_ceshi;

    @BindView(R.id.web_desc)
    WebView web_desc;

    private ExchangeDetailPresenter exchangeDetailPresenter;
    private String refund_id="53";

    private  int pink_color;
    private  int new_gray;
    private  int strokeWidth;

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
        if (!TextUtils.isEmpty(getIntent().getStringExtra("refund_id"))) {
            refund_id = getIntent().getStringExtra("refund_id");
        }
        //设置圆角背景
        GradientDrawable copyBackground = (GradientDrawable) mtv_history.getBackground();
        copyBackground.setColor(getColorResouce(R.color.white));//设置填充色
//        float[] floats={30,30,30,30,30,30,30,30};//每两个数值代表一个角，左上，右上，右下，左下
//        goodBackground.setCornerRadii(floats);
//        goodBackground.setCornerRadius(30);//数值近似px

        //设置边线,但是圆角的弧度不能统一，不建议使用
//        goodBackground.setStroke(10,Color.parseColor("#858585"));
        pink_color = getColorResouce(R.color.pink_color);
        new_gray = getColorResouce(R.color.new_gray);
        strokeWidth = TransformUtil.dip2px(this, 0.5f);
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
//            case R.id.mtv_title2:
//                text = mtv_title2.getText();
//                if (getString(R.string.cancel_order).equals(text)) {//取消订单
//                    cancleOrder();
//                } else if (getString(R.string.order_wuliu).equals(text)) {//物流
//                    //MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
//                    OrderLogisticsActivity.startAct(this,orderId);
//                }
//                break;
//            case R.id.mtv_title3:
//                text = mtv_title3.getText();
//                if (getString(R.string.order_fukuan).equals(text)) {//付款
//
//                } else if (getString(R.string.confirm_goods).equals(text)) {//确认收货
//                    confirmreceipt();
//                } else if (getString(R.string.comment).equals(text)) {//评价
//                    //MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
//                    List<ReleaseCommentEntity> entities = new ArrayList<>();
//                    List<OrderdetailEntity.Good> order_goods = orderdetailEntity.order_goods;
//                    for (int i = 0; i < order_goods.size(); i++) {
//                        OrderdetailEntity.Good bean = order_goods.get(i);
//                        ReleaseCommentEntity entity = new ReleaseCommentEntity(orderId,
//                                bean.thumb, bean.title, bean.price, bean.goods_id);
//                        entities.add(entity);
//                    }
//                    CreatCommentActivity.startAct(this, entities, CreatCommentActivity.CREAT_COMMENT);
//
//                } else if (getString(R.string.append_comment).equals(text)) {//追评
//
//                    //MyOrderEntity.Orders orders = lists.get(getAdapterPosition());
//                    List<ReleaseCommentEntity> entities = new ArrayList<>();
//                    List<OrderdetailEntity.Good> order_goods = orderdetailEntity.order_goods;
//                    for (int i = 0; i < order_goods.size(); i++) {
//                        OrderdetailEntity.Good bean = order_goods.get(i);
//                        ReleaseCommentEntity entity = new ReleaseCommentEntity(bean.thumb,
//                                bean.title, bean.price, bean.comment_id);
//                        entities.add(entity);
//                    }
//                    CreatCommentActivity.startAct(this, entities, CreatCommentActivity.APPEND_COMMENT);
//                }
//                break;
        }
    }
    private String fmtString(String str){
        String notice = "";
        try{
            notice = URLEncoder.encode(str, "utf-8");
        }catch(UnsupportedEncodingException ex){

        }
        return notice;
    }
    private String blank = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private String notice1 = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>" +
            "<h3 align='center'>关于关闭电话银行语音系统查询动态密码功能的公告</h3></head>"
            + "<body>"
            + "<p align='center'><i>发布日期：2011-04-25</i>"
            + "</p><p>尊敬的客户："
            + "</p><p>" + blank +
            "根据优化电话银行相关服务功能的整体安排，我行决定自4月25日起，在95595电话银行语音系统中，停止受理查询手机动态密码功能。给您带来的不便之处敬请谅解。如有问题，请致电我行24小时服务热线95595。"
            + "</p><p>" + blank +
            "感谢您长期以来对我行的关注、支持与厚爱!"
            + "</p><p>" + blank +
            "特此公告。"
            + "</p><p align='right'>中国光大银行"
            + "</p><p align='right'>2011年4月25日</p></body></html>";
    @Override
    public void setData(RefundDetailEntity refundDetailEntity) {
        RefundDetailEntity.RefundDetail refundDetail =refundDetailEntity.refund_detail;
        GlideUtils.getInstance().loadImage(this, ctgv_goods.getGoodsIcon(), refundDetail.thumb);
        ctgv_goods.setLabelName(refundDetail.store_name,true);
        ctgv_goods.setGoodsTitle(refundDetail.title);
        ctgv_goods.setGoodsParams(refundDetail.sku_desc);
        ctgv_goods.setGoodsCount(refundDetail.qty);
        ctgv_goods.setGoodsPrice(refundDetail.price);
        mtv_reason.setText(refundDetail.buyer_message);
        mtv_money.setText(refundDetail.refund_amount);
        mtv_amount.setText(refundDetail.goods_num);
        mtv_applyTime.setText(refundDetail.add_time);
        mtv_order.setText(refundDetail.refund_sn);
        mtv_ceshi.setText(Html.fromHtml(refundDetail.html_description, null, null));
        web_desc.getSettings().setDefaultTextEncodingName("utf-8");
        web_desc.loadData(fmtString(notice1), "text/html; charset=UTF-8", null);
//        web_desc.loadData(fmtString("你好"), "text/html", "utf-8");
//        web_desc.loadUrl("https://lanhuapp.com/web/#!/item/board/annotation?imgId=ab9ceb6c-82f9-4276-8904-367018644e7c&pid=68165c01-751f-4471-a485-8436c57d5ecd");
    }
}
