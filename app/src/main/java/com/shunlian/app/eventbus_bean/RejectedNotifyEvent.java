package com.shunlian.app.eventbus_bean;

/**
 * Created by zhanghe on 2019/1/25.
 */

public class RejectedNotifyEvent {

    public RejectedNotifyEvent(boolean rejectedSuccess,String id,String parentId) {
        this.rejectedSuccess = rejectedSuccess;
        this.commentId = id;
        this.parentCommentId = parentId;
    }

    public boolean rejectedSuccess;//驳回成功
    public String commentId;
    public String parentCommentId;
}
