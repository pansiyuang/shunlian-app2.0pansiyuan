package com.shunlian.app.view;

import com.shunlian.app.bean.OrderdetailEntity;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface OrderdetailView extends IView {
    void setOrder(OrderdetailEntity orderdetailEntity);
    void getUserId(String userId);
}
