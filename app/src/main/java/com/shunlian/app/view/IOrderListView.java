package com.shunlian.app.view;

import com.shunlian.app.bean.MyOrderEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public interface IOrderListView extends IView {

    /**
     * 订单列表
     * @param orders
     * @param page
     * @param allPage
     */
    void orderList(List<MyOrderEntity.Orders> orders,int page,int allPage);

    /**
     * 通知刷新列表
     */
    void notifRefreshList(int status);

    /**
     * 刷新订单
     * @param orders
     */
    void refreshOrder(MyOrderEntity.Orders orders);

    void getUserId(String userId);
}
