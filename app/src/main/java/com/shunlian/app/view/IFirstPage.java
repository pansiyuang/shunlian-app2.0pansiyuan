package com.shunlian.app.view;

import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface IFirstPage extends IView {
    void setTab(GetMenuEntity getMenuEntiy);
    void setContent(GetDataEntity getDataEntity);
    void setGoods(List<GoodsDeatilEntity.Goods> mDatas , int page, int allPage);
}
