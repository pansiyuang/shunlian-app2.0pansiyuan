package com.shunlian.app.eventbus_bean;

/**
 * Created by Administrator on 2018/5/16.
 */

public class CommentEvent {
    public static final int SUCCESS_APPEND_STATUS = 1;
    public static final int SUCCESS_CHANGE_STATUS = 2;

    private int mStatus;

    public CommentEvent(int status) {
        mStatus = status;
    }

    public int getStatus() {
        return mStatus;
    }
}
