package com.shunlian.app.view;

import com.shunlian.app.bean.HotBlogsEntity;

/**
 * Created by Administrator on 2018/10/16.
 */

public interface IHotBlogView extends IView {

    void getBlogList(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage);

    void focusUser(int isFocus,String memberId);

    void praiseBlog(String blogId);

    void downCountSuccess(String blogId);

    void shareGoodsSuccess(String blogId,String goodsId);

    /**
     *
     * 刷新完成
     */
    void refreshFinish();
}
