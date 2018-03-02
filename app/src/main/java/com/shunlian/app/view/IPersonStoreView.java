package com.shunlian.app.view;

import com.shunlian.app.bean.PersonShopEntity;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface IPersonStoreView extends IView {

    void getShopDetail(PersonShopEntity personShopEntity);

    void getFairishNums(String count,boolean isDel);
}
