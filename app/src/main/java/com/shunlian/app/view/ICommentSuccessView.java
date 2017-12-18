package com.shunlian.app.view;

import com.shunlian.app.bean.CommentSuccessEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public interface ICommentSuccessView extends IView {

    /**
     * 其他评价列表
     * @param comment
     * @param append
     */
    void otherCommentList(List<CommentSuccessEntity.Comment> comment,
                          List<CommentSuccessEntity.Comment> append);
}
