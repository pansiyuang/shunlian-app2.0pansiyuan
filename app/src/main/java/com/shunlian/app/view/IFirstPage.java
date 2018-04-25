package com.shunlian.app.view;

import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface IFirstPage extends IView {
    void setTab(GetMenuEntity getMenuEntiy);
    void setContent(GetDataEntity getDataEntity);
}
