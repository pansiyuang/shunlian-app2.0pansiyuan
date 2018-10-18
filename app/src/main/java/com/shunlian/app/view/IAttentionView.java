package com.shunlian.app.view;

import com.shunlian.app.bean.HotBlogsEntity;

/**
 * Created by Administrator on 2018/10/17.
 */

public interface IAttentionView extends IView {

    void getFocusblogs(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage);

    /**
     * 刷新完成
     */
    void refreshFinish();
}
