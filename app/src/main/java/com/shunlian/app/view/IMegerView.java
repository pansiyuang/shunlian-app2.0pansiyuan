package com.shunlian.app.view;

import com.shunlian.app.bean.MergeOrderEntity;

/**
 * Created by Administrator on 2017/12/8.
 */

public interface IMegerView extends IView {

    void getMegerOrder(MergeOrderEntity mergeOrderEntity);

    void refreshFinish();
}
