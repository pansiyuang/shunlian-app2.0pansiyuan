package com.shunlian.app.view;

import com.shunlian.app.bean.StoreIndexEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface StoreView extends IView {
    void storeFirst(List<StoreIndexEntity.Body> bodies);
    void storeHeader(StoreIndexEntity.Head head);
}
