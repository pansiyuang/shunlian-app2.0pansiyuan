package com.shunlian.app.eventbus_bean;

/**
 * Created by zhanghe on 2018/8/30.
 */

public class MessageReadSuccessEvent {
    public boolean isSuccess;//是否分享成功

    public MessageReadSuccessEvent(boolean isSuccess){
        this.isSuccess = isSuccess;
    }
}
