package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;

/**
 * Created by Administrator on 2018/3/26.
 */

public interface IExperienceDetailView extends IView {

    void setAdapter(BaseRecyclerAdapter adapter);

    /**
     * 是否显示软键盘
     * @param hint
     */
    void showorhideKeyboard(String hint);

    /**
     * 删除提示
     */
    void delPrompt();
}
