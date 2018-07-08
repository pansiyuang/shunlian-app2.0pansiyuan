package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;

/**
 * Created by Administrator on 2017/12/11.
 */

public interface IMyCommentListView extends IView {

    /**
     * 设置昵称和头像
     * @param nickname
     * @param avatar
     */
    void setNicknameAndAvatar(String nickname,String avatar);

    /**
     * 设置adapter
     * @param adapter
     */
    void setAdapter(BaseRecyclerAdapter adapter);
}
