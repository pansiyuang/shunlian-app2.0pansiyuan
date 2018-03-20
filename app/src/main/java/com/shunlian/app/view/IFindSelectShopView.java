package com.shunlian.app.view;

import com.shunlian.app.bean.FindSelectShopEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public interface IFindSelectShopView extends IView {

    void setList(List<FindSelectShopEntity.StoreList> storeLists);
}
