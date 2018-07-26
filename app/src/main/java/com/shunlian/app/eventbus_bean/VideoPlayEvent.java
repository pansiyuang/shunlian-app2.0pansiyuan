package com.shunlian.app.eventbus_bean;

/**
 * Created by Administrator on 2018/7/24.
 */

public class VideoPlayEvent {
    public static final int FinishAction = 100001;
    public static final int MoreAction = 100002;
    public static final int CompleteAction = 100003;

    public int action;

    public VideoPlayEvent(int i) {
        action = i;
    }
}
