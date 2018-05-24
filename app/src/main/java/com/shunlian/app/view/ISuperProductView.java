package com.shunlian.app.view;

import com.shunlian.app.bean.SuperProductEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/24.
 */

public interface ISuperProductView extends IView {

    void getProductList(List<SuperProductEntity.SuperProduct> list);
}
