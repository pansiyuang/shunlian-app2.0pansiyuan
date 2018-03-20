package com.shunlian.app.view;

import com.shunlian.app.bean.FindCommentListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface IFindCommentListView extends IView {

    void setCommentList(List<FindCommentListEntity.ItemComment> itemComments,int hotCommentCount,int page,int allPage);

    /**
     * 刷新item
     * @param itemComment
     */
    void refreshItem(FindCommentListEntity.ItemComment itemComment);


    /**
     * 设置点赞数量
     * @param num
     */
    void setPointFabulous(String num);

    /**
     * 删除成功
     */
    void delSuccess();

    /**
     * 设置评论总数
     * @param count
     */
    void setCommentAllCount(String count);
}
