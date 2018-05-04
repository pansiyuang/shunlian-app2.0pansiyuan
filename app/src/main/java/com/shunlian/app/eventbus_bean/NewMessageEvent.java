package com.shunlian.app.eventbus_bean;

/**
 * Created by Administrator on 2018/5/2.
 */

public class NewMessageEvent {
    public int msg_count; //需要变更的消息数量

    public NewMessageEvent(int count) {
        this.msg_count = count;
    }
}
