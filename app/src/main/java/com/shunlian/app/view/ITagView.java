package com.shunlian.app.view;

import com.shunlian.app.bean.TagEntity;

/**
 * Created by Administrator on 2017/12/16.
 */

public interface ITagView extends IView {

    void getOrderHistory(TagEntity tagEntity);

    void delSuccess();

    void delFail(String errorStr);
}
