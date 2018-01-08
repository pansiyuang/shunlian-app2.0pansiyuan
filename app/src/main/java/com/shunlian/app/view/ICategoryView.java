package com.shunlian.app.view;

import com.shunlian.app.bean.SearchGoodsEntity;

/**
 * Created by Administrator on 2018/1/5.
 */

public interface ICategoryView extends IView {
    void getSearchGoods(SearchGoodsEntity goodsEntity);
}
