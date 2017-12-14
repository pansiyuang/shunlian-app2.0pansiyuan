package com.shunlian.app.ui.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MyOrderAdapter;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.presenter.OrderListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.view.IOrderListView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/13.
 */

public class MyOrderAct extends BaseActivity implements IOrderListView {


    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.mtv_all)
    MyTextView mtv_all;

    @BindView(R.id.view_all)
    View view_all;

    @BindView(R.id.mtv_wait_pay)
    MyTextView mtv_wait_pay;

    @BindView(R.id.view_wait_pay)
    View view_wait_pay;

    @BindView(R.id.mtv_wait_send)
    MyTextView mtv_wait_send;

    @BindView(R.id.view_wait_send)
    View view_wait_send;

    @BindView(R.id.mtv_wait_receive)
    MyTextView mtv_wait_receive;

    @BindView(R.id.view_wait_receive)
    View view_wait_receive;

    @BindView(R.id.mtv_wait_comment)
    MyTextView mtv_wait_comment;

    @BindView(R.id.view_wait_comment)
    View view_wait_comment;
    private int pink_color;
    private int new_text;
    private OrderListPresenter presenter;
    private List<BaseFragment> fragments;
    public static final int ALL = 0;
    public static final int PAY = 1;
    public static final int SEND = 2;
    public static final int RECEIVE = 3;
    public static final int COMMENT = 4;
    private int currentPage = ALL;//当前所在页面


    public static void startAct(Context context) {
        context.startActivity(new Intent(context, MyOrderAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_my_order;
    }

    @Override
    protected void initListener() {
        super.initListener();
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("onPageSelected============"+position);
                setStatus(position + 1);
                currentPage = position;
                if (presenter != null){
                    switch (position){
                        case 0:
                            presenter.orderListAll();
                            break;
                        case 1:
                            presenter.orderListPay();
                            break;
                        case 2:
                            presenter.orderListSend();
                            break;
                        case 3:
                            presenter.orderListReceive();
                            break;
                        case 4:
                            presenter.orderListComment();
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        pink_color = getResources().getColor(R.color.pink_color);
        new_text = getResources().getColor(R.color.new_text);
        fragments = new ArrayList<>();
        fragments.add(new AllFrag());
        fragments.add(new PayFrag());
        fragments.add(new SendFrag());
        fragments.add(new ReceiveFrag());
        fragments.add(new CommentFrag());

        viewpager.setAdapter(new MyOrderAdapter(getSupportFragmentManager(), fragments));

        presenter = new OrderListPresenter(this,this);


    }

    /**
     * 全部
     */
    @OnClick(R.id.ll_all)
    public void orderAll() {
        setStatus(1);
        viewpager.setCurrentItem(0);
    }

    /**
     * 待付款
     */
    @OnClick(R.id.ll_wait_pay)
    public void orderPay() {
        setStatus(2);
        viewpager.setCurrentItem(1);
    }

    /**
     * 待发货
     */
    @OnClick(R.id.ll_wait_send)
    public void orderSend() {
        setStatus(3);
        viewpager.setCurrentItem(2);
    }

    /**
     * 待收货
     */
    @OnClick(R.id.ll_wait_receive)
    public void orderReceive() {
        setStatus(4);
        viewpager.setCurrentItem(3);
    }

    /**
     * 待评价
     */
    @OnClick(R.id.ll_wait_comment)
    public void orderComment() {
        setStatus(5);
        viewpager.setCurrentItem(4);
    }

    /**
     * 1 全部 2 待付款 3 待发货 4 待收货 5 待评价
     *
     * @param status
     */
    private void setStatus(int status) {
        mtv_all.setTextColor(status == 1 ? pink_color : new_text);
        view_all.setVisibility(status == 1 ? View.VISIBLE : View.INVISIBLE);

        mtv_wait_pay.setTextColor(status == 2 ? pink_color : new_text);
        view_wait_pay.setVisibility(status == 2 ? View.VISIBLE : View.INVISIBLE);

        mtv_wait_send.setTextColor(status == 3 ? pink_color : new_text);
        view_wait_send.setVisibility(status == 3 ? View.VISIBLE : View.INVISIBLE);

        mtv_wait_receive.setTextColor(status == 4 ? pink_color : new_text);
        view_wait_receive.setVisibility(status == 4 ? View.VISIBLE : View.INVISIBLE);

        mtv_wait_comment.setTextColor(status == 5 ? pink_color : new_text);
        view_wait_comment.setVisibility(status == 5 ? View.VISIBLE : View.INVISIBLE);
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

    /**
     * 订单列表
     *
     * @param orders
     * @param page
     * @param allPage
     */
    @Override
    public void orderList(List<MyOrderEntity.Orders> orders, int page, int allPage) {
        switch (currentPage){
            case ALL:
                ((AllFrag)fragments.get(0)).setOrderList(orders,page,allPage);
                break;
            case PAY:
                ((PayFrag)fragments.get(1)).setOrderList(orders,page,allPage);
                break;
            case SEND:
                ((SendFrag)fragments.get(2)).setOrderList(orders,page,allPage);
                break;
            case RECEIVE:
                ((ReceiveFrag)fragments.get(3)).setOrderList(orders,page,allPage);
                break;
            case COMMENT:
                ((CommentFrag)fragments.get(4)).setOrderList(orders,page,allPage);
                break;
        }
    }
}