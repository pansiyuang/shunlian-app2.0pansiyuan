package com.shunlian.app.view;

import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/22.
 */

public interface IHotExpertView extends IView {

    void getHotExpertList(List<BigImgEntity.Blog> hotBlogList);

    void focusUser(int isFocus, String memberId);

    void praiseBlog(String blogId);
}
