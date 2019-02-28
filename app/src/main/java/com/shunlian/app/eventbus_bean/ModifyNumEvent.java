package com.shunlian.app.eventbus_bean;

/**
 * Created by zhanghe on 2019/1/23.
 * 修改确认订单数量通知
 */
public class ModifyNumEvent {
    public String num;

    public ModifyNumEvent(String num) {
        this.num = num;
    }
}
