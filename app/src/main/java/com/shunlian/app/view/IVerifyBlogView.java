package com.shunlian.app.view;

import com.shunlian.app.bean.HotBlogsEntity;

public interface IVerifyBlogView extends IView {
    void getBlogList(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage,int count);

    void blogVerifyPass(int position,int count);

    void blogVerifyPass(int count);

    void blogVerifyFail(int position,int count);

    void blogVerifyFail(int count);

    void blogWithDraw(int position,int count);

    void focusUser(int type,int position);

    /**
     * 刷新完成
     */
    void refreshFinish();
}
