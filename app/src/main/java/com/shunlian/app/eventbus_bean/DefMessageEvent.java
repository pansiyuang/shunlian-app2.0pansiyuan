package com.shunlian.app.eventbus_bean;

/**
 * Created by Administrator on 2018/3/21.
 * 默认eventbus消息通知，只适用于少了数量通知
 */

public class DefMessageEvent {

    public boolean isRefGuanzhu;//是否刷新关注页面
    public boolean loginSuccess;//登录成功

    public int praisePosition = -1;//点赞的条目位置

    public int followStoreState;//商品详情页关注店铺状态
}
