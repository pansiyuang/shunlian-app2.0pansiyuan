package com.shunlian.app.view;

import com.shunlian.app.adapter.BaseRecyclerAdapter;

/**
 * Created by Administrator on 2018/3/16.
 */

public interface IGuanzhuView extends IView {

    /**
     * 设置adapter
     * @param adapter
     */
    void setAdapter(BaseRecyclerAdapter adapter);

    /**
     * 刷新完成
     */
    void refreshFinish();
}
