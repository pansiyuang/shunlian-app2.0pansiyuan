package com.shunlian.app.newchat.event;

/**
 * Created by Administrator on 2017/11/8.
 */

public class UnReadCountEvent extends BaseEvent {
    /**
     * 构造
     *
     * @param type
     */

    private int count;

    public UnReadCountEvent(EventType type) {
        super(type);
    }

    public int getCount() {
        return count;
    }

    public UnReadCountEvent setCount(int count) {
        this.count = count;
        return this;
    }
}
