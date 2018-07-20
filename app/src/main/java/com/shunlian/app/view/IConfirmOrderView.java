package com.shunlian.app.view;

import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;

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
     * 平台优惠券
     * @param user_stage_voucher 1 表示使用平台优惠券时最优
     * @param stage_voucher
     */
    void stageVoucher(String user_stage_voucher,List<ConfirmOrderEntity.Voucher> stage_voucher);
}
