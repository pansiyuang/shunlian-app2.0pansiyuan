package com.shunlian.app.view;

import com.shunlian.app.bean.HotBlogsEntity;

/**
 * Created by Administrator on 2018/10/16.
 */

public interface IHotVideoBlogView extends IView {

    void focusUser(int isFocus, String memberId);

    void parseBlog(int isAttent, String memberId);

    void downCountSuccess();
}
