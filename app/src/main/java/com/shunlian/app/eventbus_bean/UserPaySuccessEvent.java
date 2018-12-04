package com.shunlian.app.eventbus_bean;

/**
 * Created by Administrator on 2018/5/2.
 */

public class UserPaySuccessEvent {
    public boolean isSuccess; //需要变更的消息数量
    public boolean isFragmet; //需要变更的消息数量
    public UserPaySuccessEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
        isFragmet = false;
    }
    public UserPaySuccessEvent(boolean isSuccess,boolean isFragment) {
        this.isSuccess = isSuccess;
        this.isFragmet = isFragment;
    }
}
