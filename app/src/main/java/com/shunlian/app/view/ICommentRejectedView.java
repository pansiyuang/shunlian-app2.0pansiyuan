package com.shunlian.app.view;

import android.support.v7.widget.RecyclerView;

import com.shunlian.app.bean.CommentRejectedEntity;

/**
 * Created by zhanghe on 2019/1/23.
 */

public interface ICommentRejectedView extends IView {

    default void setAdapter(RecyclerView.Adapter adapter){}

    default void selectRejectedContent(CommentRejectedEntity.RejectedList list){}
}
