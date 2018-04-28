package com.shunlian.app.view;

import com.shunlian.app.bean.MyOrderEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface IChatOrderView extends IView {

    void getOrderList(List<MyOrderEntity.Orders> ordersList,int currentPage,int totalPage);

    void refreshFinish();
}
