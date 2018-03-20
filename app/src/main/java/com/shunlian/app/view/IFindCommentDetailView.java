package com.shunlian.app.view;

import com.shunlian.app.bean.FindCommentListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public interface IFindCommentDetailView extends IView {

    /**
     * 评价详情列表
     * @param likesBeans
     */
    void commentDetailList(List<FindCommentListEntity.ItemComment> likesBeans, int page, int allpage);

    /**
     * 刷新item
     * @param itemComment
     */
    void refreshItem(FindCommentListEntity.ItemComment itemComment);

    /**
     * 删除成功
     */
    void delSuccess();

    /**
     * 评论总数
     * @param count
     */
    void setCommentAllCount(String count);

    /**
     * 点赞
     * @param new_likes
     */
    void setPointFabulous(String new_likes);
}
