package com.shunlian.app.view;

import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */

public interface IConfirmOrderView extends IView{

    /**
     * 订单页所有商品
     * @param enabled
     * @param disabled
     */
    void confirmOrderAllGoods(List<ConfirmOrderEntity.Enabled> enabled,
                              List<GoodsDeatilEntity.Goods> disabled,ConfirmOrderEntity.Address address);

    /**
     * 商品总价和总数量
     * @param count
     * @param price
     */
    void goodsTotalPrice(String count,String price);

    /**
     * 支付列表
     * @param payTypes
     */
    void payList(ArrayList<ConfirmOrderEntity.PayTypes> payTypes);

}
