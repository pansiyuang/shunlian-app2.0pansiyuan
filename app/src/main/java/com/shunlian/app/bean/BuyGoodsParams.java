package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zhanghe on 2018/8/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyGoodsParams {
    public String shop_goods;//商品参数
    public String addressId;//地址id
    public String order_id;//订单id
    public String price;//价格
    public String stage_voucher_id;//平台优惠券id
    public String anonymous;//是否匿名
    public String phoneNum;//手机号
    public String face_price;//手机充值售价
    public String product_id;//plus 支付id
    public String sku_id;//属性id
    public String use_egg;//是否使用金蛋 1是 0否
}
