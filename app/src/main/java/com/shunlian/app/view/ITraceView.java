package com.shunlian.app.view;

import com.shunlian.app.bean.OrderLogisticsEntity;

/**
 * Created by Administrator on 2017/12/14.
 */

public interface ITraceView extends IView {

    void getLogistics(OrderLogisticsEntity logisticsEntity);
}
