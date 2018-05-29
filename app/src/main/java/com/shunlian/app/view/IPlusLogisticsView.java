package com.shunlian.app.view;

import com.shunlian.app.bean.OrderLogisticsEntity;

/**
 * Created by Administrator on 2018/5/28.
 */

public interface IPlusLogisticsView extends IView {

    void getLogistics(OrderLogisticsEntity logisticsEntity);
}
