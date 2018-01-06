package com.shunlian.app.view;

import com.shunlian.app.bean.PayListEntity;
import com.shunlian.app.bean.PayOrderEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */

public interface IPayListView extends IView{

    /**
     * 支付列表
     * @param payTypes
     */
    void payList(List<PayListEntity.PayTypes> payTypes);

    /**
     * 支付订单
     * @param entity
     */
    void payOrder(PayOrderEntity entity);
}