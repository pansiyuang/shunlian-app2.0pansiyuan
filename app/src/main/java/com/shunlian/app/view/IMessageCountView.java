package com.shunlian.app.view;

import com.shunlian.app.bean.AllMessageCountEntity;

/**
 * Created by Administrator on 2018/4/8.
 */

public interface IMessageCountView extends IView {
    void getMessageCount(AllMessageCountEntity messageCountEntity);

    void getMessageCountFail();
}
