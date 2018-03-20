package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;

/**
 * Created by Administrator on 2018/3/19.
 */

public interface IFindCommentView extends IView {

    /**
     * 评论总数
     * @param count
     */
    void setCommentAllCount(String count);

    /**
     * 设置adapter
     * @param adapter
     */
    void setAdapter(BaseRecyclerAdapter adapter);
    /**
     * 删除提示
     */
    void delPrompt();

    /**
     * 软键盘处理
     */
    void showorhideKeyboard(String hint);
}
