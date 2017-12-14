package com.shunlian.app.view;

import com.shunlian.app.bean.CommentListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public interface IMyCommentListView extends IView {

    /**
     * 评价列表
     * @param lists
     */
    void commentList(List<CommentListEntity.Data> lists,int currentPage,int allPage);

    /**
     * 设置昵称和头像
     * @param nickname
     * @param avatar
     */
    void setNicknameAndAvatar(String nickname,String avatar);
}
