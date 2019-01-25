package com.shunlian.app.eventbus_bean;

/**
 * Created by zhanghe on 2019/1/25.
 */

public class RejectedNotifyEvent {

    public RejectedNotifyEvent(boolean rejectedSuccess) {
        this.rejectedSuccess = rejectedSuccess;
    }

    public boolean rejectedSuccess;//驳回成功
}
