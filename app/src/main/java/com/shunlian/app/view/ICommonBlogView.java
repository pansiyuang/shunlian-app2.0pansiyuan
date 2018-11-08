package com.shunlian.app.view;

import com.shunlian.app.bean.HotBlogsEntity;

/**
 * Created by Administrator on 2018/10/22.
 */

public interface ICommonBlogView extends IView {

    void getFocusblogs(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage);

    void focusUser(int isFocus, String memberId);

    void praiseBlog(String blogId);

    void downCountSuccess(String blogId);

    void refreshFinish();
}
