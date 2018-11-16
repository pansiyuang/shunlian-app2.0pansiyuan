package com.shunlian.app.eventbus_bean;

/**
 * Created by Administrator on 2018/11/9.
 */

public class RefreshBlogEvent {

    public final static int PRAISE_TYPE = 1001;
    public final static int DOWNLOAD_TYPE = 1002;
    public final static int ATTENITON_TYPE = 1003;
    public final static int SHARE_TYPE = 1004;
    public BlogData mData;
    public int mType;

    public RefreshBlogEvent(BlogData data, int type) {
        mData = data;
        this.mType = type;
    }

    public static class BlogData {
        public String blogId;
        public String memberId;
        public int is_focus;
        public int is_praise;

        public BlogData() {
        }
    }
}
