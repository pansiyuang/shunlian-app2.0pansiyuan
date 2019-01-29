package com.shunlian.app.eventbus_bean;

import com.shunlian.app.bean.FindCommentListEntity;

public class BlogCommentEvent {

    public static final int PRAISE_TYPE = 1;
    public static final int DEL_TYPE = 2;
    public static final int ADD_TYPE = 3;
    public int sendType;
    public String mCommentId, mParentCommentId, mBlogId;
    public FindCommentListEntity.ItemComment mComment;

    public BlogCommentEvent(int type, String commentId, String parentId, String blogId) {
        sendType = type;
        mCommentId = commentId;
        mParentCommentId = parentId;
        mBlogId = blogId;
    }

    public BlogCommentEvent(int type, FindCommentListEntity.ItemComment comment) {
        sendType = type;
        mComment = comment;
    }
}
