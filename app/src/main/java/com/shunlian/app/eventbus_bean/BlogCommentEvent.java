package com.shunlian.app.eventbus_bean;

import com.shunlian.app.bean.FindCommentListEntity;

public class BlogCommentEvent {

    public static final int PRAISE_TYPE = 1; //点赞
    public static final int DEL_TYPE = 2; //删除评论
    public static final int ADD_TYPE = 3; //新增评论
    public static final int REJECTED_TYPE = 4; //驳回
    public static final int RETRACT_TYPE = 5;//撤回评论
    public static final int VERIFY_TYPE = 6;//审核评论

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
