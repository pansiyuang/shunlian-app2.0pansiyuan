package com.shunlian.app.view;

import com.shunlian.app.bean.HotBlogsEntity;

/**
 * Created by Administrator on 2018/11/5.
 */

public interface IMyPageView extends IView {

    void setSignature(String signature);

    void getFocusblogs(HotBlogsEntity hotBlogsEntity);

    void refreshFinish();
}
