package com.shunlian.app.ui.collection;

import com.shunlian.app.ui.BaseFragment;

/**
 * Created by Administrator on 2018/1/22.
 */

public abstract class CollectionFrag extends BaseFragment {

    /**
     * 删除
     */
    public abstract void delete();

    /**
     *选择所有
     */
    public abstract void selectAll();

    /**
     *操作管理
     */
    public abstract void operationMange();

    /**
     *完成管理
     */
    public abstract void finishManage();

    /**
     * 管理是否可点击
     * @return
     */
    public abstract boolean isClickManage();

}
