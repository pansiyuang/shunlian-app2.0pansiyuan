package com.shunlian.app.view;

import com.shunlian.app.bean.MyCommentListEntity;

/**
 * Created by Administrator on 2017/12/11.
 */

public interface IMyCommentListView extends IView {

    /**
     * 评价列表
     * @param entity
     */
    void commentList(MyCommentListEntity entity);
}
