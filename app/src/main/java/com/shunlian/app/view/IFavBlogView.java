package com.shunlian.app.view;

/**
 * Created by Administrator on 2018/10/29.
 */

public interface IFavBlogView extends IView {

    void  favoBlog(int type,String blogId);

    void  removeBlog(String blogId);

    void  blogWithDraw(String blogId);
}
